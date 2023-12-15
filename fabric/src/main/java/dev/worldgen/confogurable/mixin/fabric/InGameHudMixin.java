package dev.worldgen.confogurable.mixin.fabric;

import dev.worldgen.confogurable.debug.FogDebugHud;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public class InGameHudMixin {
    @Inject(method = "render(Lnet/minecraft/client/gui/GuiGraphics;F)V", at = @At("TAIL"))
    private void renderFogDebugHud(GuiGraphics context, float tickDelta, CallbackInfo ci) {
        FogDebugHud.render((Gui)(Object)this, context, tickDelta);
    }
}
