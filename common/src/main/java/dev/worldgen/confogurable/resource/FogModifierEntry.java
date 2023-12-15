package dev.worldgen.confogurable.resource;

import dev.worldgen.confogurable.fog.FogModifier;
import net.minecraft.resources.ResourceLocation;

public record FogModifierEntry(ResourceLocation id, FogModifier fogModifier) {
}
