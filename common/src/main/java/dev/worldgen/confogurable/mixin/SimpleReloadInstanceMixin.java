package dev.worldgen.confogurable.mixin;

import dev.worldgen.confogurable.access.MultiPackResourceManagerAccess;
import dev.worldgen.confogurable.resource.FogModifierManager;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.*;
import net.minecraft.util.Unit;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Mixin(SimpleReloadInstance.class)
public class SimpleReloadInstanceMixin {
    @Unique
    private static PackType reloadType;

    @Inject(
        method = "create(Lnet/minecraft/server/packs/resources/ResourceManager;Ljava/util/List;Ljava/util/concurrent/Executor;Ljava/util/concurrent/Executor;Ljava/util/concurrent/CompletableFuture;Z)Lnet/minecraft/server/packs/resources/ReloadInstance;",
        at = @At("HEAD")
    )
    private static void confogurable$getPackType(ResourceManager $$0, List<PreparableReloadListener> $$1, Executor $$2, Executor $$3, CompletableFuture<Unit> $$4, boolean $$5, CallbackInfoReturnable<ReloadInstance> cir) {
        if ($$0 instanceof MultiPackResourceManager resourceManager) {
            reloadType = ((MultiPackResourceManagerAccess)(Object)resourceManager).getPackType();
        } else {
            reloadType = null;
        }
    }

    @SuppressWarnings("all")
    @ModifyArg(
        method = "create(Lnet/minecraft/server/packs/resources/ResourceManager;Ljava/util/List;Ljava/util/concurrent/Executor;Ljava/util/concurrent/Executor;Ljava/util/concurrent/CompletableFuture;Z)Lnet/minecraft/server/packs/resources/ReloadInstance;",
        index = 1,
        at = @At(value = "INVOKE", target = "Lnet/minecraft/server/packs/resources/SimpleReloadInstance;of(Lnet/minecraft/server/packs/resources/ResourceManager;Ljava/util/List;Ljava/util/concurrent/Executor;Ljava/util/concurrent/Executor;Ljava/util/concurrent/CompletableFuture;)Lnet/minecraft/server/packs/resources/SimpleReloadInstance;")
    )
    private static List<PreparableReloadListener> confogurable$injectFogModifierReloadListener(List<PreparableReloadListener> reloadListeners) {
        List<PreparableReloadListener> updatedReloadListeners = new ArrayList<>(reloadListeners);
        if (reloadType == PackType.CLIENT_RESOURCES) {
            updatedReloadListeners.add(new FogModifierManager());
        }
        return updatedReloadListeners;
    }
}
