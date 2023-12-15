package dev.worldgen.confogurable.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.worldgen.confogurable.ConfogurableCommon;
import dev.worldgen.confogurable.util.DarkSkyManager;
import dev.worldgen.confogurable.util.FogColorManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(LevelRenderer.class)
public abstract class WorldRendererMixin {

    @ModifyVariable(method = "renderSky(Lcom/mojang/blaze3d/vertex/PoseStack;Lorg/joml/Matrix4f;FLnet/minecraft/client/Camera;ZLjava/lang/Runnable;)V", at = @At("STORE"), ordinal = 0)
    private Vec3 confogurable$injectCustomSkyColor(Vec3 original) {
        FogColorManager.passVanillaSky(original);
        return FogColorManager.getSkyColor(Minecraft.getInstance().getFrameTime());
    }

    @WrapOperation(
        method = "renderSky(Lcom/mojang/blaze3d/vertex/PoseStack;Lorg/joml/Matrix4f;FLnet/minecraft/client/Camera;ZLjava/lang/Runnable;)V",
        at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderColor(FFFF)V"),
        slice = @Slice(
            from = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;defaultBlendFunc()V"),
            to = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/ClientLevel$ClientLevelData;getHorizonHeight(Lnet/minecraft/world/level/LevelHeightAccessor;)D")
        )
    )
    private void confogurable$fixDarkSky(float f, float g, float h, float i, Operation<Void> original) {
        Vec3 fogColor = FogColorManager.getFogColor(Minecraft.getInstance().getFrameTime());
        Vec3 voidColor = FogColorManager.lerp(fogColor, Vec3.ZERO, DarkSkyManager.getDarkSky(Minecraft.getInstance().getFrameTime()));
        original.call((float)voidColor.x, (float)voidColor.y, (float)voidColor.z, i);
    }
}
