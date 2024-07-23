package aurilux.titles.client.handler;

import aurilux.titles.api.Title;
import aurilux.titles.client.Keybinds;
import aurilux.titles.client.gui.TitleSelectionScreen;
import aurilux.titles.common.TitlesMod;
import aurilux.titles.common.core.TitleManager;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.*;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.forgespi.language.IModInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Mod.EventBusSubscriber(modid = TitlesMod.MOD_ID, value = Dist.CLIENT)
public class ClientEventHandler {
    @SubscribeEvent
    public static void onClientTick(final TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) {
            return;
        }

        if (Keybinds.openTitleSelection.consumeClick()) {
            Player player = Minecraft.getInstance().player;
            if (player != null) {
                TitleManager.getCapability(player).ifPresent(cap ->
                        Minecraft.getInstance().setScreen(new TitleSelectionScreen(player, cap)));
            }
        }
    }

    @SubscribeEvent
    public static void onClientReceivedChat(ClientChatReceivedEvent.System event) {
        MutableComponent component = event.getMessage().copy();
        ComponentContents componentContents = component.getContents();

        if (componentContents instanceof TranslatableContents translatableContents
                && translatableContents.getKey().startsWith("chat.type.advancement.")) {
            // I wish there was a more flexible, elegant way to identify the correct subcomponents
            Component targetPlayerName = ((Component) translatableContents.getArgs()[0]).getSiblings().get(0);
            MutableComponent componentArg = (MutableComponent) ((TranslatableContents) ((MutableComponent) translatableContents.getArgs()[1]).getContents()).getArgs()[0];
            String advancementKey = ((TranslatableContents) componentArg.getContents()).getKey();

            // We have to check if it's the correct type of text component because some advancements use plain text
            // instead of a translatable entry.
            Title unlockedTitle = processKey(advancementKey);
            Player clientPlayer = Minecraft.getInstance().player;
            if (!unlockedTitle.isNull() && clientPlayer != null) {
                TitleManager.doIfPresent(clientPlayer, cap -> {
                    MutableComponent formattedTitle = unlockedTitle.getTextComponent(cap.getGenderSetting()).copy();
                    if (clientPlayer.getName().getString().equals(targetPlayerName.getString())) {
                        formattedTitle.withStyle(s -> s.withUnderlined(true)
                                .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.literal("Click to set as \ndisplay title.")))
                                .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/titles display " + unlockedTitle.getID().toString())));
                    }
                    component.append(Component.translatable("chat.advancement.append", formattedTitle));
                    event.setMessage(component);
                });
            }
        }
    }

    private static Title processKey(String key) {
        List<String> keyParts = new ArrayList<>(Arrays.asList(key.split("[/.:]")));
        keyParts = keyParts.stream()
                // Mod authors alternate using "advancement" or "advancements" for their advancement lang keys. Also
                // remove common suffixes to the lang key such as "title" and "name".
                .filter(part -> !(part.startsWith("advancement") || part.equals("title") || part.equals("name")))
                // A little more processing to protect from an edge case, such as in Botania, where their advancements
                // are in the format "spark_craft", but their lang entries are "sparkCraft"
                .map(part -> {
                    if (part.chars().anyMatch(Character::isUpperCase)) {
                        List<String> words = new ArrayList<>(Arrays.asList(part.split("(?=\\p{javaUpperCase})")));
                        words.replaceAll(String::toLowerCase);
                        return String.join("_", words);
                    }
                    else {
                        return part;
                    }
                })
                .collect(Collectors.toList());

        // Get a list of all mod id's, removing those that shouldn't have title data.
        List<String> modList = ModList.get().getMods().stream()
                .map(IModInfo::getModId)
                .filter(id -> !(id.equals("forge") || id.equals("FML") || id.equals("mcp"))).toList();

        // See if one of the parts is a mod id.
        String possibleModId = "";
        for (String part : keyParts) {
            if (modList.contains(part)) {
                possibleModId = part;
                break;
            }
        }

        String titleString = "";
        if (!possibleModId.isEmpty()) {
            keyParts.remove(possibleModId);
            titleString = possibleModId + ":" + String.join("/", keyParts);
        }
        else {
            for (String modId : modList) {
                ResourceLocation testKey = new ResourceLocation(modId + ":" + String.join("/", keyParts));
                if (TitleManager.getTitlesOfType(Title.AwardType.ADVANCEMENT).containsKey(testKey)) {
                    titleString = testKey.toString();
                    break;
                }
            }
        }
        return TitleManager.getTitle(titleString);
    }
}