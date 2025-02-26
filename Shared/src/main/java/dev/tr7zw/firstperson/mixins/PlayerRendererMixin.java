package dev.tr7zw.firstperson.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import dev.tr7zw.firstperson.FirstPersonModelCore;
import dev.tr7zw.firstperson.api.FirstPersonAPI;
import dev.tr7zw.firstperson.api.PlayerOffsetHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.world.phys.Vec3;

/**
 * Offset the player behind the camera
 * 
 * @author tr7zw
 *
 */
@Mixin(PlayerRenderer.class)
public class PlayerRendererMixin {

    private static Minecraft fpm_mc = Minecraft.getInstance();

    @Inject(method = "getRenderOffset", at = @At("RETURN"), cancellable = true)
    public void getRenderOffset(AbstractClientPlayer entity, float f, CallbackInfoReturnable<Vec3> ci) {
        if (entity == fpm_mc.cameraEntity && FirstPersonModelCore.isRenderingPlayer) {
            FirstPersonModelCore.getWrapper().updatePositionOffset(entity, Vec3.ZERO);

            Vec3 offset = ci.getReturnValue().add(FirstPersonModelCore.getWrapper().getOffset());
            
            for(PlayerOffsetHandler handler : FirstPersonAPI.getPlayerOffsetHandlers()) {
                offset = handler.applyOffset(entity, f, ci.getReturnValue(), offset);
            }

            ci.setReturnValue(offset);
        }
    }

}
