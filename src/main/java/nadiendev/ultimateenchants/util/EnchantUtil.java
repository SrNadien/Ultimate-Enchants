package nadiendev.ultimateenchants.util;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import nadiendev.ultimateenchants.UltimateEnchants;

import java.util.Optional;


public final class EnchantUtil {

    private EnchantUtil() {}

    public static ResourceKey<Enchantment> key(String path) {
        return ResourceKey.create(Registries.ENCHANTMENT, Identifier.fromNamespaceAndPath(UltimateEnchants.MOD_ID, path));
    }

    public static final ResourceKey<Enchantment> SOULBOUND = key("soulbound");
    public static final ResourceKey<Enchantment> MAGIC_PROTECTION = key("magic_protection");
    public static final ResourceKey<Enchantment> DISPLACEMENT = key("displacement");
    public static final ResourceKey<Enchantment> FLAMING_REBUKE = key("flaming_rebuke");
    public static final ResourceKey<Enchantment> CHILLING_REBUKE = key("chilling_rebuke");
    public static final ResourceKey<Enchantment> REACH = key("reach");
    public static final ResourceKey<Enchantment> VITALITY = key("vitality");
    public static final ResourceKey<Enchantment> AIR_AFFINITY = key("air_affinity");
    public static final ResourceKey<Enchantment> GOURMAND = key("gourmand");
    public static final ResourceKey<Enchantment> INSIGHT = key("insight");
    public static final ResourceKey<Enchantment> CAVALIER = key("cavalier");
    public static final ResourceKey<Enchantment> ENDER_DISRUPTION = key("ender_disruption");
    public static final ResourceKey<Enchantment> FROST_ASPECT = key("frost_aspect");
    public static final ResourceKey<Enchantment> LEECH = key("leech");
    public static final ResourceKey<Enchantment> INSTIGATING = key("instigating");
    public static final ResourceKey<Enchantment> MAGIC_EDGE = key("magic_edge");
    public static final ResourceKey<Enchantment> OUTLAW = key("outlaw");
    public static final ResourceKey<Enchantment> VIGILANTE = key("vigilante");
    public static final ResourceKey<Enchantment> VORPAL = key("vorpal");
    public static final ResourceKey<Enchantment> EXCAVATING = key("excavating");
    public static final ResourceKey<Enchantment> HUNTERS_BOUNTY = key("hunters_bounty");
    public static final ResourceKey<Enchantment> QUICK_DRAW = key("quick_draw");
    public static final ResourceKey<Enchantment> TRUESHOT = key("trueshot");
    public static final ResourceKey<Enchantment> VOLLEY = key("volley");
    public static final ResourceKey<Enchantment> ANGLERS_BOUNTY = key("anglers_bounty");
    public static final ResourceKey<Enchantment> PILFERING = key("pilfering");
    public static final ResourceKey<Enchantment> BULWARK = key("bulwark");
    public static final ResourceKey<Enchantment> PHALANX = key("phalanx");
    public static final ResourceKey<Enchantment> CURSE_OF_FOOLISHNESS = key("curse_of_foolishness");
    public static final ResourceKey<Enchantment> CURSE_OF_MERCY = key("curse_of_mercy");
    public static final ResourceKey<Enchantment> TELEKINESIS = key("telekinesis");
    public static final ResourceKey<Enchantment> ANT_REPELLENT = key("ant_repellent");
    public static final ResourceKey<Enchantment> LIFE_STEAL = key("life_steal");
    public static final ResourceKey<Enchantment> LET_THERE_BE_LIGHT = key("let_there_be_light");
    public static final ResourceKey<Enchantment> UNBREAKABLE_PLUS = key("unbreakable_plus");
    public static final ResourceKey<Enchantment> MOTHER_LODE_BLESSING = key("mother_lode_blessing");
    public static final ResourceKey<Enchantment> SOARING = key("soaring");
    public static final ResourceKey<Enchantment> DECAY = key("decay");
    public static final ResourceKey<Enchantment> WITHERING = key("withering");
    public static final ResourceKey<Enchantment> SHIMMER = key("shimmer");
    public static final ResourceKey<Enchantment> PULVERIZE = key("pulverize");
    public static final ResourceKey<Enchantment> HEALTH_PLUS = key("health_plus");
    public static final ResourceKey<Enchantment> FLIM_FLAM = key("flim_flam");
    public static final ResourceKey<Enchantment> LAST_STAND = key("last_stand");
    public static final ResourceKey<Enchantment> UNSTABLE = key("unstable");

    public static int level(Entity entity, ItemStack stack, ResourceKey<Enchantment> enchant) {
        if (stack.isEmpty() || entity == null || entity.level() == null) return 0;
        HolderLookup.RegistryLookup<Enchantment> lookup = entity.level().registryAccess().lookupOrThrow(Registries.ENCHANTMENT);
        Optional<Holder.Reference<Enchantment>> holder = lookup.get(enchant);
        return holder.map(h -> EnchantmentHelper.getItemEnchantmentLevel(h, stack)).orElse(0);
    }

    public static boolean has(Entity entity, ItemStack stack, ResourceKey<Enchantment> enchant) {
        return level(entity, stack, enchant) > 0;
    }
}