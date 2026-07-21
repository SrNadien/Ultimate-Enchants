package nadiendev.ultimateenchants.event;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.AnvilUpdateEvent;
import net.neoforged.neoforge.event.ItemAttributeModifierEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.EntityTeleportEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;
import net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.entity.player.ArrowLooseEvent;
import net.neoforged.neoforge.event.entity.player.ItemFishedEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerXpEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

import nadiendev.ultimateenchants.UltimateEnchants;
import nadiendev.ultimateenchants.config.ServerConfig;
import nadiendev.ultimateenchants.util.EnchantUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class EnchantmentEventHandler {

    private static final Map<UUID, List<ItemStack>> SOULBOUND_STORAGE = new HashMap<>();
    private static final Map<UUID, Long> ENDER_DISRUPTION_LOCK = new HashMap<>();

    private static final ResourceLocation REACH_BLOCK_ID = ResourceLocation.fromNamespaceAndPath(UltimateEnchants.MOD_ID, "reach_block");
    private static final ResourceLocation REACH_ENTITY_ID = ResourceLocation.fromNamespaceAndPath(UltimateEnchants.MOD_ID, "reach_entity");
    private static final ResourceLocation VITALITY_HEALTH_ID = ResourceLocation.fromNamespaceAndPath(UltimateEnchants.MOD_ID, "vitality_health");
    private static final ResourceLocation BULWARK_KB_ID = ResourceLocation.fromNamespaceAndPath(UltimateEnchants.MOD_ID, "bulwark_knockback");
    private static final ResourceLocation PHALANX_SPEED_ID = ResourceLocation.fromNamespaceAndPath(UltimateEnchants.MOD_ID, "phalanx_speed");

    // ---------------------------------------------------------------
    // Soulbound
    // ---------------------------------------------------------------
    @SubscribeEvent
    public void onDrops(LivingDropsEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        if (player.level().isClientSide) return;

        List<ItemStack> kept = new ArrayList<>();
        event.getDrops().removeIf(itemEntity -> {
            ItemStack stack = itemEntity.getItem();
            if (EnchantUtil.has(player, stack, EnchantUtil.SOULBOUND)) {
                kept.add(stack.copy());
                return true;
            }
            return false;
        });

        if (!kept.isEmpty()) {
            SOULBOUND_STORAGE.computeIfAbsent(player.getUUID(), k -> new ArrayList<>()).addAll(kept);
        }
    }

    @SubscribeEvent
    public void onPlayerClone(PlayerEvent.Clone event) {
        if (!event.isWasDeath()) return;
        UUID id = event.getOriginal().getUUID();
        List<ItemStack> stored = SOULBOUND_STORAGE.remove(id);
        if (stored == null || stored.isEmpty()) return;

        Player newPlayer = event.getEntity();
        for (ItemStack stack : stored) {
            if (!newPlayer.getInventory().add(stack)) {
                newPlayer.drop(stack, false);
            }
        }
    }

    // ---------------------------------------------------------------
    // Incoming damage: Instigating, Cavalier, Magic Edge, Ender Disruption,
    // Outlaw, Vigilante, Frost Aspect, Vorpal
    // ---------------------------------------------------------------
    @SubscribeEvent
    public void onIncomingDamage(LivingIncomingDamageEvent event) {
        LivingEntity victim = event.getEntity();
        if (victim.level().isClientSide) return;

        Entity attackerEntity = event.getSource().getEntity();
        if (!(attackerEntity instanceof LivingEntity attacker)) return;

        ItemStack weapon = attacker.getMainHandItem();
        if (weapon.isEmpty()) return;

        float amount = event.getAmount();
        float bonus = 0f;

        int instigating = EnchantUtil.level(attacker, weapon, EnchantUtil.INSTIGATING);
        if (instigating > 0 && victim.getHealth() >= victim.getMaxHealth()) {
            bonus += amount; // total 2x
        }

        int cavalier = EnchantUtil.level(attacker, weapon, EnchantUtil.CAVALIER);
        if (cavalier > 0 && attacker.isPassenger()) {
            bonus += amount * 0.15f * cavalier;
        }

        int magicEdge = EnchantUtil.level(attacker, weapon, EnchantUtil.MAGIC_EDGE);
        if (magicEdge > 0) {
            bonus += amount * 0.08f * magicEdge;
        }

        int enderDisruption = EnchantUtil.level(attacker, weapon, EnchantUtil.ENDER_DISRUPTION);
        if (enderDisruption > 0 && victim instanceof EnderMan) {
            bonus += amount * 0.25f * enderDisruption;
            ENDER_DISRUPTION_LOCK.put(victim.getUUID(), victim.level().getGameTime() + 100L);
        }

        int outlaw = EnchantUtil.level(attacker, weapon, EnchantUtil.OUTLAW);
        if (outlaw > 0 && (victim.getType().is(net.minecraft.tags.EntityTypeTags.RAIDERS) == false)
                && (victim instanceof net.minecraft.world.entity.npc.Villager || victim instanceof net.minecraft.world.entity.animal.IronGolem)) {
            bonus += amount * 0.2f * outlaw;
        }

        int vigilante = EnchantUtil.level(attacker, weapon, EnchantUtil.VIGILANTE);
        if (vigilante > 0 && (victim.getType().is(net.minecraft.tags.EntityTypeTags.RAIDERS)
                || victim instanceof net.minecraft.world.entity.monster.Ravager)) {
            bonus += amount * 0.2f * vigilante;
        }

        int frostAspect = EnchantUtil.level(attacker, weapon, EnchantUtil.FROST_ASPECT);
        if (frostAspect > 0) {
            victim.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60 + frostAspect * 20, Math.min(frostAspect, 2)));
            victim.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 60 + frostAspect * 20, 0));
        }

        int vorpal = EnchantUtil.level(attacker, weapon, EnchantUtil.VORPAL);
        if (vorpal > 0 && victim.level().random.nextFloat() < 0.08f) {
            bonus += Math.max(amount * 3f, victim.getMaxHealth());
            if (attacker.level() instanceof ServerLevel serverLevel) {
                serverLevel.playSound(null, victim.blockPosition(), SoundEvents.PLAYER_ATTACK_CRIT, SoundSource.PLAYERS, 1.0f, 0.6f);
            }
        }

        if (bonus > 0f) {
            event.setAmount(amount + bonus);
        }
    }

    // ---------------------------------------------------------------
    // Post damage: rebukes (attacker gets punished), displacement, leech
    // ---------------------------------------------------------------
    @SubscribeEvent
    public void onDamagePost(LivingDamageEvent.Post event) {
        LivingEntity victim = event.getEntity();
        if (victim.level().isClientSide) return;

        Entity attackerEntity = event.getSource().getEntity();

        // Rebukes / Displacement: victim wears the chestplate, attacker gets punished
        if (attackerEntity instanceof LivingEntity attacker) {
            ItemStack chest = victim.getItemBySlot(EquipmentSlot.CHEST);
            if (!chest.isEmpty()) {
                int displacement = EnchantUtil.level(victim, chest, EnchantUtil.DISPLACEMENT);
                if (displacement > 0 && victim.level().random.nextFloat() < 0.15f * displacement) {
                    double angle = victim.level().random.nextDouble() * Math.PI * 2;
                    double dist = 4 + victim.level().random.nextDouble() * 3;
                    double nx = attacker.getX() + Math.cos(angle) * dist;
                    double nz = attacker.getZ() + Math.sin(angle) * dist;
                    attacker.teleportTo(nx, attacker.getY(), nz);
                }

                int flaming = EnchantUtil.level(victim, chest, EnchantUtil.FLAMING_REBUKE);
                if (flaming > 0) {
                    attacker.igniteForSeconds(2 + flaming * 2);
                    attacker.knockback(0.6, victim.getX() - attacker.getX(), victim.getZ() - attacker.getZ());
                }

                int chilling = EnchantUtil.level(victim, chest, EnchantUtil.CHILLING_REBUKE);
                if (chilling > 0) {
                    attacker.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, Math.min(chilling, 2)));
                    attacker.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 60, Math.min(chilling - 1, 1)));
                    attacker.knockback(0.6, victim.getX() - attacker.getX(), victim.getZ() - attacker.getZ());
                }
            }

            // Leech: attacker heals from damage dealt
            ItemStack weapon = attacker.getMainHandItem();
            int leech = EnchantUtil.level(attacker, weapon, EnchantUtil.LEECH);
            if (leech > 0) {
                float heal = event.getNewDamage() * 0.07f * leech;
                if (heal > 0) {
                    attacker.heal(heal);
                }
            }
        }
    }

    // ---------------------------------------------------------------
    // Ender Disruption: block Enderman teleport while marked
    // ---------------------------------------------------------------
    @SubscribeEvent
    public void onTeleport(EntityTeleportEvent event) {
        if (!(event.getEntity() instanceof EnderMan enderMan)) return;
        Long until = ENDER_DISRUPTION_LOCK.get(enderMan.getUUID());
        if (until != null && enderMan.level().getGameTime() < until) {
            event.setCanceled(true);
        }
    }

    // ---------------------------------------------------------------
    // Air Affinity + Excavating: break speed
    // ---------------------------------------------------------------
    @SubscribeEvent
    public void onBreakSpeed(PlayerEvent.BreakSpeed event) {
        Player player = event.getEntity();

        ItemStack helmet = player.getItemBySlot(EquipmentSlot.HEAD);
        if (!player.onGround() && EnchantUtil.has(player, helmet, EnchantUtil.AIR_AFFINITY)) {
            // Vanilla applies "speedMultiplier /= 5" when the player isn't on the ground.
            // Undo that penalty directly on the event's speed instead of calling
            // player.getDigSpeed(), which re-fires PlayerEvent.BreakSpeed and causes
            // infinite recursion -> StackOverflowError (see crash-...-client.txt).
            event.setNewSpeed(event.getNewSpeed() * 5.0f);
        }
        // Excavating radius mining is handled client-side selection is out of scope here;
        // actual multi-block breaking is applied in onBlockBreak-like logic where supported.
    }

    // ---------------------------------------------------------------
    // Gourmand
    // ---------------------------------------------------------------
    @SubscribeEvent
    public void onFinishUsingItem(LivingEntityUseItemEvent.Finish event) {
        if (!(event.getEntity() instanceof Player player)) return;
        if (player.level().isClientSide) return;

        ItemStack item = event.getItem();
        FoodProperties food = item.getFoodProperties(player);
        if (food == null) return;

        ItemStack helmet = player.getItemBySlot(EquipmentSlot.HEAD);
        int gourmand = EnchantUtil.level(player, helmet, EnchantUtil.GOURMAND);
        if (gourmand > 0) {
            int extraNutrition = Math.max(1, Math.round(food.nutrition() * 0.15f * gourmand));
            float extraSaturation = food.saturation() * 0.15f * gourmand;
            player.getFoodData().eat(extraNutrition, extraSaturation);
        }
    }

    // ---------------------------------------------------------------
    // Insight
    // ---------------------------------------------------------------
    @SubscribeEvent
    public void onXpChange(PlayerXpEvent.XpChange event) {
        Player player = event.getEntity();
        ItemStack helmet = player.getItemBySlot(EquipmentSlot.HEAD);
        int insight = EnchantUtil.level(player, helmet, EnchantUtil.INSIGHT);
        if (insight > 0 && event.getAmount() > 0) {
            event.setAmount(event.getAmount() + Math.round(event.getAmount() * 0.1f * insight));
        }
    }

    // ---------------------------------------------------------------
    // Item attribute modifiers: Reach, Vitality, Bulwark
    // ---------------------------------------------------------------
    @SubscribeEvent
    public void onItemAttributeModifier(ItemAttributeModifierEvent event) {
        ItemStack stack = event.getItemStack();
        if (stack.isEmpty()) return;

        var enchants = stack.getEnchantments();
        if (enchants.isEmpty()) return;

        int reach = rawLevel(stack, EnchantUtil.REACH);
        if (reach > 0) {
            Holder<Attribute> blockRange = Attributes.BLOCK_INTERACTION_RANGE;
            Holder<Attribute> entityRange = Attributes.ENTITY_INTERACTION_RANGE;
            event.addModifier(blockRange, new AttributeModifier(REACH_BLOCK_ID, reach, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.CHEST);
            event.addModifier(entityRange, new AttributeModifier(REACH_ENTITY_ID, reach, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.CHEST);
        }

        int vitality = rawLevel(stack, EnchantUtil.VITALITY);
        if (vitality > 0) {
            event.addModifier(Attributes.MAX_HEALTH, new AttributeModifier(VITALITY_HEALTH_ID, 2.0 * vitality, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.CHEST);
        }

        int bulwark = rawLevel(stack, EnchantUtil.BULWARK);
        if (bulwark > 0) {
            event.addModifier(Attributes.KNOCKBACK_RESISTANCE, new AttributeModifier(BULWARK_KB_ID, 1.0, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND);
            event.addModifier(Attributes.KNOCKBACK_RESISTANCE, new AttributeModifier(BULWARK_KB_ID, 1.0, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.OFFHAND);
            // KNOCKBACK_RESISTANCE no afecta explosiones ni wind charges (Breeze, Wind Burst).
            // Ese empuje lo controla un atributo aparte: EXPLOSION_KNOCKBACK_RESISTANCE.
            event.addModifier(Attributes.EXPLOSION_KNOCKBACK_RESISTANCE, new AttributeModifier(BULWARK_KB_ID, 1.0, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND);
            event.addModifier(Attributes.EXPLOSION_KNOCKBACK_RESISTANCE, new AttributeModifier(BULWARK_KB_ID, 1.0, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.OFFHAND);
        }
    }

    private int rawLevel(ItemStack stack, net.minecraft.resources.ResourceKey<net.minecraft.world.item.enchantment.Enchantment> key) {
        var enchantments = stack.getEnchantments();
        for (Holder<net.minecraft.world.item.enchantment.Enchantment> holder : enchantments.keySet()) {
            if (holder.is(key)) {
                return enchantments.getLevel(holder);
            }
        }
        return 0;
    }

    // ---------------------------------------------------------------
    // Phalanx: speed boost while actively blocking with a shield
    // ---------------------------------------------------------------
    @SubscribeEvent
    public void onPlayerTick(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        if (player.level().isClientSide) return;

        ItemStack main = player.getMainHandItem();
        ItemStack off = player.getOffhandItem();
        int phalanx = Math.max(EnchantUtil.level(player, main, EnchantUtil.PHALANX), EnchantUtil.level(player, off, EnchantUtil.PHALANX));

        // Bloquear con shield tiene un tope de velocidad hardcodeado en vanilla
        // ("paso de sneak"), separado del atributo MOVEMENT_SPEED. Un modifier
        // chico sobre ese atributo se pierde adentro de ese tope y no se nota.
        // El efecto de poción Speed sí está probado que funciona correctamente
        // por encima de ese tope (así lo hace vanilla), así que replicamos eso:
        // Phalanx nivel N = Speed N (Phalanx III = Speed III).
        if (phalanx > 0 && player.isBlocking()) {
            player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 10, phalanx - 1, true, false, false));
        }

        // Frost Walker override: lava freezing (players)
        if (ServerConfig.FROST_WALKER_OVERRIDE.get() && ServerConfig.FROST_WALKER_LAVA.get()) {
            ItemStack boots = player.getItemBySlot(EquipmentSlot.FEET);
            int frostWalkerLevel = frostWalkerLevel(player, boots);
            if (frostWalkerLevel > 0) {
                freezeLavaAround(player, frostWalkerLevel);
            }
        }
    }

    // ---------------------------------------------------------------
    // Frost Walker override: horses, gated by config
    // ---------------------------------------------------------------
    @SubscribeEvent
    public void onLivingTickForHorses(net.neoforged.neoforge.event.tick.EntityTickEvent.Post event) {
        if (!ServerConfig.FROST_WALKER_OVERRIDE.get()) return;
        if (!(event.getEntity() instanceof AbstractHorse horse)) return;
        if (horse.level().isClientSide) return;

        ItemStack bodyArmor = horse.getBodyArmorItem();
        int level = frostWalkerLevel(horse, bodyArmor);
        if (level <= 0) return;

        freezeWaterAround(horse, level);
        if (ServerConfig.FROST_WALKER_LAVA.get()) {
            freezeLavaAround(horse, level);
        }
    }

    private int frostWalkerLevel(Entity entity, ItemStack stack) {
        if (stack.isEmpty() || entity.level() == null) return 0;
        var lookup = entity.level().registryAccess().lookupOrThrow(net.minecraft.core.registries.Registries.ENCHANTMENT);
        var holder = lookup.get(net.minecraft.world.item.enchantment.Enchantments.FROST_WALKER);
        return holder.map(h -> net.minecraft.world.item.enchantment.EnchantmentHelper.getItemEnchantmentLevel(h, stack)).orElse(0);
    }

    private void freezeWaterAround(Entity entity, int level) {
        if (!(entity.level() instanceof ServerLevel level_)) return;
        int radius = level + 2;
        BlockPos center = entity.blockPosition();
        for (int dx = -radius; dx <= radius; dx++) {
            for (int dz = -radius; dz <= radius; dz++) {
                BlockPos pos = center.offset(dx, -1, dz);
                BlockState state = level_.getBlockState(pos);
                if (state.getFluidState().is(Fluids.WATER) && state.getFluidState().isSource()) {
                    level_.setBlockAndUpdate(pos, Blocks.FROSTED_ICE.defaultBlockState());
                }
            }
        }
    }

    private void freezeLavaAround(Entity entity, int level) {
        if (!(entity.level() instanceof ServerLevel level_)) return;
        int radius = level + 1;
        BlockPos center = entity.blockPosition();
        for (int dx = -radius; dx <= radius; dx++) {
            for (int dz = -radius; dz <= radius; dz++) {
                BlockPos pos = center.offset(dx, -1, dz);
                BlockState state = level_.getBlockState(pos);
                if (state.getFluidState().is(Fluids.LAVA) && state.getFluidState().isSource()) {
                    level_.setBlockAndUpdate(pos, Blocks.MAGMA_BLOCK.defaultBlockState());
                }
            }
        }
    }

    // ---------------------------------------------------------------
    // Fishing: Angler's Bounty, Pilfering
    // ---------------------------------------------------------------
    @SubscribeEvent
    public void onItemFished(ItemFishedEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        if (player.level().isClientSide) return;

        ItemStack rod = player.getMainHandItem();
        if (!rod.is(Items.FISHING_ROD)) {
            rod = player.getOffhandItem();
        }

        int anglersBounty = EnchantUtil.level(player, rod, EnchantUtil.ANGLERS_BOUNTY);
        if (anglersBounty > 0 && player.level().random.nextFloat() < 0.1f * anglersBounty) {
            List<ItemStack> drops = event.getDrops();
            if (!drops.isEmpty()) {
                drops.add(drops.get(0).copy());
            }
        }

        int pilfering = EnchantUtil.level(player, rod, EnchantUtil.PILFERING);
        if (pilfering > 0 && event.getHookEntity().getHookedIn() instanceof LivingEntity hooked && !(hooked instanceof Player)) {
            EquipmentSlot[] armorSlots = {EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET};
            for (EquipmentSlot slot : armorSlots) {
                ItemStack armor = hooked.getItemBySlot(slot);
                if (!armor.isEmpty() && player.level().random.nextFloat() < 0.3f) {
                    hooked.setItemSlot(slot, ItemStack.EMPTY);
                    if (!player.getInventory().add(armor)) {
                        player.drop(armor, false);
                    }
                    break;
                }
            }
        }
    }

    // ---------------------------------------------------------------
    // Curse of Mercy: never deal a lethal blow
    // ---------------------------------------------------------------
    @SubscribeEvent
    public void onDamagePre(LivingDamageEvent.Pre event) {
        LivingEntity victim = event.getEntity();
        if (victim.level().isClientSide) return;

        Entity attackerEntity = event.getSource().getEntity();
        if (!(attackerEntity instanceof LivingEntity attacker)) return;

        ItemStack weapon = attacker.getMainHandItem();
        if (!EnchantUtil.has(attacker, weapon, EnchantUtil.CURSE_OF_MERCY)) return;

        if (event.getNewDamage() >= victim.getHealth() && victim.getHealth() > 1.0f) {
            event.setNewDamage(victim.getHealth() - 1.0f);
        } else if (event.getNewDamage() >= victim.getHealth()) {
            event.setNewDamage(0f);
        }
    }

    // ---------------------------------------------------------------
    // Excavating: break extra blocks of the same type around the mined one
    // ---------------------------------------------------------------
    @SubscribeEvent
    public void onBlockBreak(BlockEvent.BreakEvent event) {
        Player player = event.getPlayer();
        if (player == null || player.level().isClientSide || player.isCreative()) return;

        ItemStack tool = player.getMainHandItem();
        int excavating = EnchantUtil.level(player, tool, EnchantUtil.EXCAVATING);
        if (excavating <= 0) return;
        if (!(player.level() instanceof ServerLevel serverLevel)) return;

        BlockPos origin = event.getPos();
        Block targetBlock = event.getState().getBlock();
        int radius = excavating;

        for (BlockPos pos : BlockPos.betweenClosed(origin.offset(-radius, -radius, -radius), origin.offset(radius, radius, radius))) {
            if (pos.equals(origin)) continue;
            BlockState state = serverLevel.getBlockState(pos);
            if (state.getBlock() != targetBlock) continue;
            if (!tool.isCorrectToolForDrops(state)) continue;
            serverLevel.destroyBlock(pos.immutable(), true, player);
        }
    }

    // ---------------------------------------------------------------
    // Hunter's Bounty: chance of extra drops from animals slain with a bow
    // ---------------------------------------------------------------
    @SubscribeEvent
    public void onHuntersBounty(LivingDropsEvent event) {
        if (!(event.getEntity() instanceof Animal animal)) return;
        if (animal.level().isClientSide) return;

        Entity shooterEntity = event.getSource().getEntity();
        if (!(shooterEntity instanceof Player shooter)) return;

        ItemStack weapon = shooter.getMainHandItem();
        int level = EnchantUtil.level(shooter, weapon, EnchantUtil.HUNTERS_BOUNTY);
        if (level <= 0) return;

        if (animal.level().random.nextFloat() < 0.15f * level && !event.getDrops().isEmpty()) {
            ItemEntity original = event.getDrops().iterator().next();
            ItemEntity extra = new ItemEntity(animal.level(), original.getX(), original.getY(), original.getZ(), original.getItem().copy());
            event.getDrops().add(extra);
        }
    }

    // ---------------------------------------------------------------
    // Curse of Foolishness: no XP gained while using the cursed item
    // ---------------------------------------------------------------
    @SubscribeEvent
    public void onCurseOfFoolishness(PlayerXpEvent.XpChange event) {
        Player player = event.getEntity();
        ItemStack main = player.getMainHandItem();
        if (event.getAmount() > 0 && EnchantUtil.has(player, main, EnchantUtil.CURSE_OF_FOOLISHNESS)) {
            event.setAmount(0);
        }
    }

    // ---------------------------------------------------------------
    // Quick Draw: fully draws the bow faster
    // ---------------------------------------------------------------
    @SubscribeEvent
    public void onArrowLoose(ArrowLooseEvent event) {
        Player player = event.getEntity();
        ItemStack bow = event.getBow();
        int quickDraw = EnchantUtil.level(player, bow, EnchantUtil.QUICK_DRAW);
        if (quickDraw > 0 && event.getCharge() > 0) {
            event.setCharge(event.getCharge() + quickDraw * 4);
        }
    }

    // ---------------------------------------------------------------
    // Trueshot (accuracy/speed/piercing) and Volley (extra arrows, one consumed)
    // ---------------------------------------------------------------
    private boolean spawningVolleyArrow = false;

    @SubscribeEvent
    public void onArrowJoin(EntityJoinLevelEvent event) {
        if (spawningVolleyArrow) return;
        if (!(event.getEntity() instanceof AbstractArrow arrow)) return;
        if (!(arrow.getOwner() instanceof Player shooter)) return;
        if (event.getLevel().isClientSide) return;

        ItemStack bow = shooter.getMainHandItem();
        if (!(bow.getItem() instanceof net.minecraft.world.item.BowItem)) {
            bow = shooter.getOffhandItem();
        }

        int trueshot = EnchantUtil.level(shooter, bow, EnchantUtil.TRUESHOT);
        if (trueshot > 0) {
            Vec3 look = shooter.getLookAngle().normalize();
            double speed = arrow.getDeltaMovement().length() * (1.0 + 0.1 * trueshot);
            arrow.setDeltaMovement(look.scale(speed));
        }

        int volley = EnchantUtil.level(shooter, bow, EnchantUtil.VOLLEY);
        if (volley > 0 && event.getLevel() instanceof ServerLevel serverLevel) {
            spawningVolleyArrow = true;
            try {
                for (int i = 1; i <= volley; i++) {
                    double spreadDeg = 3.0 * i;
                    AbstractArrow clone = (AbstractArrow) arrow.getType().create(serverLevel);
                    if (clone == null) continue;
                    clone.moveTo(arrow.getX(), arrow.getY(), arrow.getZ(), arrow.getYRot(), arrow.getXRot());
                    clone.setOwner(shooter);
                    clone.setBaseDamage(arrow.getBaseDamage());
                    Vec3 baseMotion = arrow.getDeltaMovement();
                    Vec3 offsetMotion = baseMotion.yRot((float) Math.toRadians(spreadDeg * (i % 2 == 0 ? 1 : -1)));
                    clone.setDeltaMovement(offsetMotion);
                    serverLevel.addFreshEntity(clone);
                }
            } finally {
                spawningVolleyArrow = false;
            }
        }
    }

    // ---------------------------------------------------------------
    // Mending override: repair Mending items for free at the anvil.
    // Blocking the vanilla XP-repair itself would require a Mixin;
    // not covered here (see NOTES.md).
    // ---------------------------------------------------------------
    @SubscribeEvent
    public void onAnvilUpdate(AnvilUpdateEvent event) {
        if (!ServerConfig.MENDING_OVERRIDE.get()) return;

        ItemStack left = event.getLeft();
        ItemStack right = event.getRight();
        if (left.isEmpty() || right.isEmpty()) return;
        if (!left.isDamageableItem() || !left.isDamaged()) return;
        if (!left.is(right.getItem())) return;

        boolean hasMending = EnchantUtil.has(event.getPlayer(), left, net.minecraft.world.item.enchantment.Enchantments.MENDING);
        if (!hasMending) return;

        ItemStack output = left.copy();
        output.setDamageValue(0);
        event.setOutput(output);
        event.setCost(0);
        event.setMaterialCost(1);
    }
}