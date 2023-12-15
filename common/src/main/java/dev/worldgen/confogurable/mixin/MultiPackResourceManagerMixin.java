package dev.worldgen.confogurable.mixin;

import dev.worldgen.confogurable.access.MultiPackResourceManagerAccess;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.MultiPackResourceManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(MultiPackResourceManager.class)
public class MultiPackResourceManagerMixin implements MultiPackResourceManagerAccess {
    @Unique
    private PackType packType = null;

    @Inject(
        method = "<init>(Lnet/minecraft/server/packs/PackType;Ljava/util/List;)V",
        at = @At("RETURN")
    )
    private void confogurable$getPackType(PackType $$0, List<?> $$1, CallbackInfo ci) {
        this.packType = $$0;
    }

    @Override
    public PackType getPackType() {
        return packType;
    }
}
