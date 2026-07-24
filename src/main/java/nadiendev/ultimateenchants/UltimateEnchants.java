package nadiendev.ultimateenchants;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import nadiendev.ultimateenchants.config.ClientConfig;
import nadiendev.ultimateenchants.config.ServerConfig;
import nadiendev.ultimateenchants.config.EnchantsConfig;
import nadiendev.ultimateenchants.event.EnchantmentEventHandler;
import nadiendev.ultimateenchants.client.MotherLodeHighlight;
import nadiendev.ultimateenchants.client.MotherLodeRenderer;


@Mod(UltimateEnchants.MOD_ID)
public class UltimateEnchants {

    public static final String MOD_ID = "ultimateenchants";

    public UltimateEnchants(IEventBus modEventBus, ModContainer modContainer) {
        modContainer.registerConfig(ModConfig.Type.CLIENT, ClientConfig.SPEC, MOD_ID + "-client.toml");
        modContainer.registerConfig(ModConfig.Type.SERVER, ServerConfig.SPEC, MOD_ID + "-server.toml");
        modContainer.registerConfig(ModConfig.Type.SERVER, EnchantsConfig.SPEC, MOD_ID + "enchantments-toggler.toml");

    
        if (FMLEnvironment.getDist().isClient()) {
            NeoForge.EVENT_BUS.addListener(ClientTickEvent.Post.class, MotherLodeHighlight::onClientTick);
            NeoForge.EVENT_BUS.addListener(MotherLodeRenderer::onRenderLevel);
        }

        NeoForge.EVENT_BUS.register(new EnchantmentEventHandler());
    }
}