package nadiendev.ultimateenchants.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public class ClientConfig {

    public static final ModConfigSpec SPEC;

    public static final ModConfigSpec.BooleanValue SHOW_CURSE_WARNINGS;

    static {
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();

        builder.push("display");

        SHOW_CURSE_WARNINGS = builder
                .comment("If true, shows an action bar warning when equipping an item with a curse from this mod.")
                .define("showCurseWarnings", true);

        builder.pop();

        SPEC = builder.build();
    }
}
