package nadiendev.ultimateenchants.mixin;

import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Player;
import nadiendev.ultimateenchants.config.ServerConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(ExperienceOrb.class)
public class ExperienceOrbMixin {

    @Inject(method = "repairPlayerItems", at = @At("HEAD"), cancellable = true)
    private void ultimateenchants$blockMendingRepair(Player player, int healAmount, CallbackInfoReturnable<Integer> cir) {
        if (ServerConfig.MENDING_OVERRIDE.get()) {
            cir.setReturnValue(healAmount);
        }
    }
}