package dev.worldgen.confogurable.mixin;

import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(FogRenderer.class)
public interface BackgroundRendererInvoker {
    @Invoker("getPriorityFogFunction")
    static FogRenderer.MobEffectFogFunction invokeGetPriorityFogFunction(Entity entity, float tickDelta) {
        throw new UnsupportedOperationException();
    }
}
