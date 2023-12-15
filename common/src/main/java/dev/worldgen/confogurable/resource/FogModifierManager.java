package dev.worldgen.confogurable.resource;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mojang.serialization.JsonOps;
import dev.worldgen.confogurable.ConfogurableCommon;
import dev.worldgen.confogurable.fog.FogModifier;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.Unit;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;

import java.io.BufferedReader;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class FogModifierManager implements PreparableReloadListener {
    private static LinkedHashMap<ResourceLocation, FogModifierEntry> fogModifiers;
    private static final FileToIdConverter FINDER = FileToIdConverter.json("fogs");
    private static final Gson GSON = new Gson();

    public void reload(ResourceManager manager) {
        ConfogurableCommon.LOGGER.info("Loading fog definitions");

        // Get all .json files from "fogs" resource pack directory
        Iterator<Map.Entry<ResourceLocation, Resource>> resourceEntries = FINDER.listMatchingResources(manager).entrySet().iterator();


        // Get map of resource pack names to map of fog modifier entries (empty at start)
        LinkedHashMap<String, Map<ResourceLocation, FogModifier>> packs = new LinkedHashMap<>();
        Minecraft.getInstance().getResourcePackRepository().getSelectedIds().forEach(pack -> packs.put(pack, new LinkedHashMap<>()));


        // For each .json file from "fogs" resource pack directory:
        while (resourceEntries.hasNext()) {
            Map.Entry<ResourceLocation, Resource> entry = resourceEntries.next();
            ResourceLocation resourceId = FINDER.fileToId(entry.getKey());

            Map<ResourceLocation, FogModifier> pack = packs.get(entry.getValue().sourcePackId());
            try (BufferedReader bufferedReader = entry.getValue().openAsReader()) {
                JsonObject fogModifierJson = GsonHelper.fromJson(GSON, bufferedReader, JsonObject.class);
                FogModifier fogModifier = Util.getOrThrow(FogModifier.CODEC.parse(JsonOps.INSTANCE, fogModifierJson), JsonParseException::new);
                pack.put(resourceId, fogModifier);
            } catch (Exception e) {
                ConfogurableCommon.LOGGER.error("Error occurred while loading fog definition " + resourceId + ".\n", e);
            }
        }

        // Add fog modifiers in pack priority order
        fogModifiers = new LinkedHashMap<>();
        packs.forEach((name, pack) -> {
            List<Map.Entry<ResourceLocation, FogModifier>> entries = new ArrayList<>(pack.entrySet());
            entries.sort(Comparator.comparingInt(fogModifierEntry -> fogModifierEntry.getValue().priority()));
            entries.forEach(entry -> {
                fogModifiers.put(entry.getKey(), new FogModifierEntry(entry.getKey(), entry.getValue()));
            });
        });

        // Inverse fogModifiers
        List<Map.Entry<ResourceLocation, FogModifierEntry>> entryList = new ArrayList<>(fogModifiers.entrySet().stream().toList());
        Collections.reverse(entryList);
        fogModifiers = new LinkedHashMap<>();
        entryList.forEach(entry -> fogModifiers.put(entry.getKey(), entry.getValue()));

        fogModifiers.forEach((id, fogModifier) -> ConfogurableCommon.LOGGER.info(id.toString() + " " + fogModifier.fogModifier().priority()));
    }

    public static Map<ResourceLocation, FogModifierEntry> getFogModifiers() {
        return fogModifiers;
    }

    public static Optional<FogModifierEntry> getFogModifier(ResourceKey<Biome> biome, ResourceKey<Level> dimension, BlockPos position, int time) {
        for (FogModifierEntry entry : fogModifiers.values()) {
            if (entry.fogModifier().condition().evaluate(biome, dimension, position, time)) return Optional.of(entry);
        }
        return Optional.empty();
    }

    @Override
    public CompletableFuture<Void> reload(PreparationBarrier preparationBarrier, ResourceManager resourceManager, ProfilerFiller profilerFiller, ProfilerFiller profilerFiller1, Executor executor, Executor executor1) {
        return preparationBarrier.wait(Unit.INSTANCE).thenRunAsync(() -> this.reload(resourceManager));
    }
}
