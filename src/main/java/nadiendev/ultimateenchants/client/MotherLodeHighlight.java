package nadiendev.ultimateenchants.client;

import net.minecraft.util.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import nadiendev.ultimateenchants.util.EnchantUtil;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;


public final class MotherLodeHighlight {
    private MotherLodeHighlight() {}

    public static final TagKey<Block> ORES_TAG =
            TagKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath("minecraft", "ores"));
    public static final TagKey<Block> C_ORES_TAG =
            TagKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath("c", "ores"));

    private static final int RADIUS = 12;
    private static final int RESCAN_INTERVAL_TICKS = 10; // medio segundo

    
    public static volatile Set<BlockPos> highlighted = Collections.emptySet();

    private static boolean scanning = false;
    private static int cooldown = 0;
    private static BlockPos lastScanCenter = null;

    public static void onClientTick(ClientTickEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        Level level = mc.level;

        if (player == null || level == null || !isActive(player)) {
            if (!highlighted.isEmpty()) highlighted = Collections.emptySet();
            cooldown = 0;
            lastScanCenter = null;
            return;
        }

        BlockPos center = player.blockPosition();
        boolean moved = lastScanCenter == null || lastScanCenter.distSqr(center) > 4 * 4;

        if (cooldown > 0) {
            cooldown--;
            if (!moved) return;
        }
        cooldown = RESCAN_INTERVAL_TICKS;

        if (scanning) return;
        scanning = true;
        lastScanCenter = center;

        Util.backgroundExecutor().execute(() -> {
            try {
                Set<BlockPos> found = new HashSet<>();
                for (BlockPos pos : BlockPos.betweenClosed(
                        center.offset(-RADIUS, -RADIUS, -RADIUS), center.offset(RADIUS, RADIUS, RADIUS))) {
                    if (level.getBlockState(pos).is(ORES_TAG) || level.getBlockState(pos).is(C_ORES_TAG)) {
                        found.add(pos.immutable());
                    }
                }
                highlighted = found;
            } finally {
                scanning = false;
            }
        });
    }

    private static boolean isActive(Player player) {
        ItemStack offhand = player.getOffhandItem();
        return offhand.is(Items.SHIELD)
                && EnchantUtil.has(player, offhand, EnchantUtil.MOTHER_LODE_BLESSING)
                && player.isUsingItem();
    }
}