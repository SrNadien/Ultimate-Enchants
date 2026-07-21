package nadiendev.ultimateenchants;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.fml.config.ModConfig;
import nadiendev.ultimateenchants.config.ClientConfig;
import nadiendev.ultimateenchants.config.ServerConfig;
import nadiendev.ultimateenchants.event.EnchantmentEventHandler;

@Mod(UltimateEnchants.MOD_ID)
public class UltimateEnchants {

    public static final String MOD_ID = "ultimateenchants";

    public UltimateEnchants(IEventBus modEventBus, ModContainer modContainer) {
        modContainer.registerConfig(ModConfig.Type.CLIENT, ClientConfig.SPEC, MOD_ID + "-client.toml");
        modContainer.registerConfig(ModConfig.Type.SERVER, ServerConfig.SPEC, MOD_ID + "-server.toml");

        NeoForge.EVENT_BUS.register(new EnchantmentEventHandler());
    }
}