package dev.worldgen.confogurable.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dev.worldgen.confogurable.util.FogColorManager;
import dev.worldgen.confogurable.util.FogDistanceManager;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.material.FogType;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(FogRenderer.class)
public class BackgroundRendererMixin {
    @ModifyExpressionValue(method = "setupColor(Lnet/minecraft/client/Camera;FLnet/minecraft/client/multiplayer/ClientLevel;IF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/ClientLevel;getSkyColor(Lnet/minecraft/world/phys/Vec3;F)Lnet/minecraft/world/phys/Vec3;"))
    private static Vec3 injectCustomSkyColor(Vec3 original) {
        FogColorManager.passVanillaSky(original);
        return FogColorManager.getSkyColor(Minecraft.getInstance().getFrameTime());
    }

    @Inject(method = "setupFog(Lnet/minecraft/client/Camera;Lnet/minecraft/client/renderer/FogRenderer$FogMode;FZF)V", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderFogStart(F)V", shift = At.Shift.BEFORE), locals = LocalCapture.CAPTURE_FAILHARD)
    private static void injectCustomFogDistance(Camera camera, FogRenderer.FogMode fogType, float viewDistance, boolean thickFog, float tickDelta, CallbackInfo ci, FogType cameraSubmersionType, Entity entity, FogRenderer.FogData fogData) {
        FogRenderer.MobEffectFogFunction statusEffectFogModifier = BackgroundRendererInvoker.invokeGetPriorityFogFunction(entity, tickDelta);
        if (camera.getFluidInCamera() == FogType.NONE && fogType != FogRenderer.FogMode.FOG_SKY && statusEffectFogModifier == null) {
            FogDistanceManager.passVanillaFogStart(fogData.start);
            fogData.start = FogDistanceManager.getFogStart(Minecraft.getInstance().getFrameTime());
            FogDistanceManager.passVanillaFogEnd(fogData.end);
            fogData.end = FogDistanceManager.getFogEnd(Minecraft.getInstance().getFrameTime());
        }
    }
}
