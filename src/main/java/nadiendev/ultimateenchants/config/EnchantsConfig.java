package nadiendev.ultimateenchants.config;

import net.neoforged.neoforge.common.ModConfigSpec;


public class EnchantsConfig {

    public static final ModConfigSpec SPEC;

    public static final ModConfigSpec.BooleanValue ENABLE_SOULBOUND;
    public static final ModConfigSpec.BooleanValue ENABLE_MAGIC_PROTECTION;
    public static final ModConfigSpec.BooleanValue ENABLE_DISPLACEMENT;
    public static final ModConfigSpec.BooleanValue ENABLE_FLAMING_REBUKE;
    public static final ModConfigSpec.BooleanValue ENABLE_CHILLING_REBUKE;
    public static final ModConfigSpec.BooleanValue ENABLE_REACH;
    public static final ModConfigSpec.BooleanValue ENABLE_VITALITY;
    public static final ModConfigSpec.BooleanValue ENABLE_AIR_AFFINITY;
    public static final ModConfigSpec.BooleanValue ENABLE_GOURMAND;
    public static final ModConfigSpec.BooleanValue ENABLE_INSIGHT;
    public static final ModConfigSpec.BooleanValue ENABLE_CAVALIER;
    public static final ModConfigSpec.BooleanValue ENABLE_ENDER_DISRUPTION;
    public static final ModConfigSpec.BooleanValue ENABLE_FROST_ASPECT;
    public static final ModConfigSpec.BooleanValue ENABLE_LEECH;
    public static final ModConfigSpec.BooleanValue ENABLE_INSTIGATING;
    public static final ModConfigSpec.BooleanValue ENABLE_MAGIC_EDGE;
    public static final ModConfigSpec.BooleanValue ENABLE_OUTLAW;
    public static final ModConfigSpec.BooleanValue ENABLE_VIGILANTE;
    public static final ModConfigSpec.BooleanValue ENABLE_VORPAL;
    public static final ModConfigSpec.BooleanValue ENABLE_EXCAVATING;
    public static final ModConfigSpec.BooleanValue ENABLE_HUNTERS_BOUNTY;
    public static final ModConfigSpec.BooleanValue ENABLE_QUICK_DRAW;
    public static final ModConfigSpec.BooleanValue ENABLE_TRUESHOT;
    public static final ModConfigSpec.BooleanValue ENABLE_VOLLEY;
    public static final ModConfigSpec.BooleanValue ENABLE_ANGLERS_BOUNTY;
    public static final ModConfigSpec.BooleanValue ENABLE_PILFERING;
    public static final ModConfigSpec.BooleanValue ENABLE_BULWARK;
    public static final ModConfigSpec.BooleanValue ENABLE_PHALANX;
    public static final ModConfigSpec.BooleanValue ENABLE_CURSE_OF_FOOLISHNESS;
    public static final ModConfigSpec.BooleanValue ENABLE_CURSE_OF_MERCY;
    public static final ModConfigSpec.BooleanValue ENABLE_TELEKINESIS;
    public static final ModConfigSpec.BooleanValue ENABLE_ANT_REPELLENT;
    public static final ModConfigSpec.BooleanValue ENABLE_LIFE_STEAL;
    public static final ModConfigSpec.BooleanValue ENABLE_LET_THERE_BE_LIGHT;
    public static final ModConfigSpec.BooleanValue ENABLE_UNBREAKABLE_PLUS;
    public static final ModConfigSpec.BooleanValue ENABLE_MOTHER_LODE_BLESSING;
    public static final ModConfigSpec.BooleanValue ENABLE_SOARING;
    public static final ModConfigSpec.BooleanValue ENABLE_DECAY;
    public static final ModConfigSpec.BooleanValue ENABLE_WITHERING;
    public static final ModConfigSpec.BooleanValue ENABLE_SHIMMER;
    public static final ModConfigSpec.BooleanValue ENABLE_PULVERIZE;
    public static final ModConfigSpec.BooleanValue ENABLE_HEALTH_PLUS;
    public static final ModConfigSpec.BooleanValue ENABLE_FLIM_FLAM;
    public static final ModConfigSpec.BooleanValue ENABLE_LAST_STAND;
    public static final ModConfigSpec.BooleanValue ENABLE_UNSTABLE;

    static {
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();

        builder.push("enchants");

        ENABLE_SOULBOUND = builder
                .comment("Activa o desactiva el encantamiento Soulbound.")
                .define("enableSoulbound", true);

        ENABLE_MAGIC_PROTECTION = builder
                .comment("Activa o desactiva el encantamiento Magic Protection.")
                .define("enableMagicProtection", true);

        ENABLE_DISPLACEMENT = builder
                .comment("Activa o desactiva el encantamiento Displacement.")
                .define("enableDisplacement", true);

        ENABLE_FLAMING_REBUKE = builder
                .comment("Activa o desactiva el encantamiento Flaming Rebuke.")
                .define("enableFlamingRebuke", true);

        ENABLE_CHILLING_REBUKE = builder
                .comment("Activa o desactiva el encantamiento Chilling Rebuke.")
                .define("enableChillingRebuke", true);

        ENABLE_REACH = builder
                .comment("Activa o desactiva el encantamiento Reach.")
                .define("enableReach", true);

        ENABLE_VITALITY = builder
                .comment("Activa o desactiva el encantamiento Vitality.")
                .define("enableVitality", true);

        ENABLE_AIR_AFFINITY = builder
                .comment("Activa o desactiva el encantamiento Air Affinity.")
                .define("enableAirAffinity", true);

        ENABLE_GOURMAND = builder
                .comment("Activa o desactiva el encantamiento Gourmand.")
                .define("enableGourmand", true);

        ENABLE_INSIGHT = builder
                .comment("Activa o desactiva el encantamiento Insight.")
                .define("enableInsight", true);

        ENABLE_CAVALIER = builder
                .comment("Activa o desactiva el encantamiento Cavalier.")
                .define("enableCavalier", true);

        ENABLE_ENDER_DISRUPTION = builder
                .comment("Activa o desactiva el encantamiento Ender Disruption.")
                .define("enableEnderDisruption", true);

        ENABLE_FROST_ASPECT = builder
                .comment("Activa o desactiva el encantamiento Frost Aspect.")
                .define("enableFrostAspect", true);

        ENABLE_LEECH = builder
                .comment("Activa o desactiva el encantamiento Leech.")
                .define("enableLeech", true);

        ENABLE_INSTIGATING = builder
                .comment("Activa o desactiva el encantamiento Instigating.")
                .define("enableInstigating", true);

        ENABLE_MAGIC_EDGE = builder
                .comment("Activa o desactiva el encantamiento Magic Edge.")
                .define("enableMagicEdge", true);

        ENABLE_OUTLAW = builder
                .comment("Activa o desactiva el encantamiento Outlaw.")
                .define("enableOutlaw", true);

        ENABLE_VIGILANTE = builder
                .comment("Activa o desactiva el encantamiento Vigilante.")
                .define("enableVigilante", true);

        ENABLE_VORPAL = builder
                .comment("Activa o desactiva el encantamiento Vorpal.")
                .define("enableVorpal", true);

        ENABLE_EXCAVATING = builder
                .comment("Activa o desactiva el encantamiento Excavating.")
                .define("enableExcavating", true);

        ENABLE_HUNTERS_BOUNTY = builder
                .comment("Activa o desactiva el encantamiento Hunters Bounty.")
                .define("enableHuntersBounty", true);

        ENABLE_QUICK_DRAW = builder
                .comment("Activa o desactiva el encantamiento Quick Draw.")
                .define("enableQuickDraw", true);

        ENABLE_TRUESHOT = builder
                .comment("Activa o desactiva el encantamiento Trueshot.")
                .define("enableTrueshot", true);

        ENABLE_VOLLEY = builder
                .comment("Activa o desactiva el encantamiento Volley.")
                .define("enableVolley", true);

        ENABLE_ANGLERS_BOUNTY = builder
                .comment("Activa o desactiva el encantamiento Anglers Bounty.")
                .define("enableAnglersBounty", true);

        ENABLE_PILFERING = builder
                .comment("Activa o desactiva el encantamiento Pilfering.")
                .define("enablePilfering", true);

        ENABLE_BULWARK = builder
                .comment("Activa o desactiva el encantamiento Bulwark.")
                .define("enableBulwark", true);

        ENABLE_PHALANX = builder
                .comment("Activa o desactiva el encantamiento Phalanx.")
                .define("enablePhalanx", true);

        ENABLE_CURSE_OF_FOOLISHNESS = builder
                .comment("Activa o desactiva el encantamiento Curse of Foolishness.")
                .define("enableCurseOfFoolishness", true);

        ENABLE_CURSE_OF_MERCY = builder
                .comment("Activa o desactiva el encantamiento Curse of Mercy.")
                .define("enableCurseOfMercy", true);

        ENABLE_TELEKINESIS = builder
                .comment("Activa o desactiva el encantamiento Telekinesis.")
                .define("enableTelekinesis", true);

        ENABLE_ANT_REPELLENT = builder
                .comment("Activa o desactiva el encantamiento Repelente de Hormigas.")
                .define("enableAntRepellent", true);

        ENABLE_LIFE_STEAL = builder
                .comment("Activa o desactiva el encantamiento Robo de Vida.")
                .define("enableLifeSteal", true);

        ENABLE_LET_THERE_BE_LIGHT = builder
                .comment("Activa o desactiva el encantamiento Hagase la Luz.")
                .define("enableLetThereBeLight", true);

        ENABLE_UNBREAKABLE_PLUS = builder
                .comment("Activa o desactiva el encantamiento Unbreakable Plus.")
                .define("enableUnbreakablePlus", true);

        ENABLE_MOTHER_LODE_BLESSING = builder
                .comment("Activa o desactiva el encantamiento Bendicion de la Madre Mina.")
                .define("enableMotherLodeBlessing", true);

        ENABLE_SOARING = builder
                .comment("Activa o desactiva el encantamiento Soaring.")
                .define("enableSoaring", true);

        ENABLE_DECAY = builder
                .comment("Activa o desactiva el encantamiento Decay.")
                .define("enableDecay", true);

        ENABLE_WITHERING = builder
                .comment("Activa o desactiva el encantamiento Withering.")
                .define("enableWithering", true);

        ENABLE_SHIMMER = builder
                .comment("Activa o desactiva el encantamiento Shimmer.")
                .define("enableShimmer", true);

        ENABLE_PULVERIZE = builder
                .comment("Activa o desactiva el encantamiento Pulverize.")
                .define("enablePulverize", true);

        ENABLE_HEALTH_PLUS = builder
                .comment("Activa o desactiva el encantamiento Vida+.")
                .define("enableHealthPlus", true);

        ENABLE_FLIM_FLAM = builder
                .comment("Activa o desactiva el encantamiento Flim Flam.")
                .define("enableFlimFlam", true);

        ENABLE_LAST_STAND = builder
                .comment("Activa o desactiva el encantamiento Last Stand.")
                .define("enableLastStand", true);

        ENABLE_UNSTABLE = builder
                .comment("Activa o desactiva el encantamiento Unstable.")
                .define("enableUnstable", true);

        builder.pop();

        SPEC = builder.build();
    }
}