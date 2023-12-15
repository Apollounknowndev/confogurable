package dev.worldgen.confogurable.mixin.fabric;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dev.worldgen.confogurable.util.FogColorManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(FogRenderer.class)
public class BackgroundRendererMixin {
    @ModifyExpressionValue(method = "method_24873(Lnet/minecraft/client/multiplayer/ClientLevel;Lnet/minecraft/world/level/biome/BiomeManager;FIII)Lnet/minecraft/world/phys/Vec3;", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/DimensionSpecialEffects;getBrightnessDependentFogColor(Lnet/minecraft/world/phys/Vec3;F)Lnet/minecraft/world/phys/Vec3;"))
    private static Vec3 injectCustomFogColor(Vec3 original) {
        FogColorManager.passVanillaFog(original);
        return FogColorManager.getFogColor(Minecraft.getInstance().getFrameTime());
    }
}
