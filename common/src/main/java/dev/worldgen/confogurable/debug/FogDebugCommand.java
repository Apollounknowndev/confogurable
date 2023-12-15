package dev.worldgen.confogurable.debug;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import dev.worldgen.confogurable.fog.FogModifier;
import dev.worldgen.confogurable.resource.FogModifierManager;
import dev.worldgen.confogurable.util.TimeFixer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.util.RandomSource;

import static com.mojang.brigadier.builder.LiteralArgumentBuilder.literal;
import static com.mojang.brigadier.builder.RequiredArgumentBuilder.argument;

public class FogDebugCommand {
    private static final String[] quotes = {"The fog is coming.", "It came From The Fog", "I FOGor 💀", "Well fog that ig", "So much fog, you can't see a thing!", "Foggy thoughts", "AMONG US!!!", "Configures your fog cutely uwu"};

    public static void register(CommandDispatcher dispatcher) {
        dispatcher.register(literal("fogdebug").then(literal("list").executes(context -> {
            sendMessage();
            return 1;
        })).then(literal("hud").then(argument("enable", BoolArgumentType.bool()).executes(context -> {
            FogDebugHud.enabled(BoolArgumentType.getBool(context, "enable"));
            return 1;
        }))));
    }

    private static void sendMessage() {
        Minecraft client = Minecraft.getInstance();
        Component message = fogDebugCommand(client.player);
        client.gui.getChat().addMessage(message);

    }

    private static Component fogDebugCommand(LocalPlayer clientPlayerEntity) {
        MutableComponent result = Component.literal(fogDebugQuote()).setStyle(Style.EMPTY.withColor(TextColor.parseColor("white")));
        result.append("\n-----------------------------------------------------");

        FogModifierManager.getFogModifiers().forEach((id, fogModifierEntry) -> {
            FogModifier fogModifier = fogModifierEntry.fogModifier();
            boolean conditionMet = clientPlayerEntity.level().getBiome(clientPlayerEntity.getOnPos()).unwrapKey().filter(registryKey -> fogModifier.condition().evaluate(registryKey, clientPlayerEntity.clientLevel.dimension(), clientPlayerEntity.getOnPos(), TimeFixer.fix(clientPlayerEntity.clientLevel.getDayTime()))).isPresent();
            result.append("\n- ");
            result.append(Component.literal(id.toString()).setStyle(conditionMet ? Style.EMPTY.withColor(TextColor.parseColor("green")) : Style.EMPTY));
            result.append("\ncondition: ").append(fogModifier.condition().toString());
            result.append("\ncolor: ");
            result.append(Component.literal("■").setStyle(Style.EMPTY.withColor(fogModifier.color())));
            result.append(String.valueOf(fogModifier.color())).setStyle(Style.EMPTY.withColor(TextColor.parseColor("white")));
            result.append("\nfog_start: ").append(fogModifier.fogStart().toString());
            result.append("\nfog_end: ").append(fogModifier.fogEnd().toString());
            result.append("\n-----------------------------------------------------");
        });

        return result;
    }

    private static String fogDebugQuote() {
        return quotes[RandomSource.create().nextInt(quotes.length)];
    }
}
