package nadiendev.ultimateenchants.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public class ServerConfig {

    public static final ModConfigSpec SPEC;

    public static final ModConfigSpec.BooleanValue MENDING_OVERRIDE;
    public static final ModConfigSpec.BooleanValue FROST_WALKER_OVERRIDE;
    public static final ModConfigSpec.BooleanValue FROST_WALKER_LAVA;

    static {
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();

        builder.push("overrides");

        MENDING_OVERRIDE = builder
                .comment("If true, Mending is replaced by Preservation: XP no longer repairs the item, but it can be repaired for free at an Anvil (repair only, no upgrades).")
                .define("mendingOverride", false);

        FROST_WALKER_OVERRIDE = builder
                .comment("If true, Frost Walker also works on Horse Armor.")
                .define("frostWalkerOverride", true);

        FROST_WALKER_LAVA = builder
                .comment("If true (and frostWalkerOverride is true), Frost Walker also freezes Lava into Magma Blocks for players and horses.")
                .define("frostWalkerLava", false);

        builder.pop();

        SPEC = builder.build();
    }
}
