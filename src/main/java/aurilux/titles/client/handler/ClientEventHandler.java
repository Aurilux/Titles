package aurilux.titles.client.handler;

import aurilux.titles.api.Title;
import aurilux.titles.client.Keybinds;
import aurilux.titles.client.gui.TitleSelectionScreen;
import aurilux.titles.common.TitlesMod;
import aurilux.titles.common.core.TitleManager;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.moddiscovery.ModInfo;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;
import java.util.stream.Collectors;

@Mod.EventBusSubscriber(modid = TitlesMod.MOD_ID, value = Dist.CLIENT)
public class ClientEventHandler {
    @SubscribeEvent
    public static void onClientTick(final TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) {
            return;
        }

        if (Keybinds.openTitleSelection.isPressed()) {
            PlayerEntity player = Minecraft.getInstance().player;
            if (player != null) {
                TitleManager.getCapability(player).ifPresent(cap -> {
                    Minecraft.getInstance().displayGuiScreen(new TitleSelectionScreen(player, cap));
                });
            }
        }
    }

    @SubscribeEvent
    public static void onClientReceivedChat(ClientChatReceivedEvent event) {
        IFormattableTextComponent component = event.getMessage().copyRaw();
        if (component instanceof TranslationTextComponent) {
            TranslationTextComponent textComponent = (TranslationTextComponent) component;
            if (textComponent.getKey().startsWith("chat.type.advancement.")) {
                // I wish there was a more flexible, elegant way to identify the correct sub-components
                ITextComponent targetPlayerName = ((ITextComponent) textComponent.getFormatArgs()[0]).getSiblings().get(0);
                TitlesMod.LOG.info(component.toString());
                Title unlockedTitle = processKey(((TranslationTextComponent)((TranslationTextComponent) textComponent.getFormatArgs()[1])
                        .getFormatArgs()[0]).getKey());
                PlayerEntity clientPlayer = Minecraft.getInstance().player;
                if (!unlockedTitle.isNull() && clientPlayer != null) {
                    TitleManager.doIfPresent(clientPlayer, cap -> {
                        IFormattableTextComponent formattedTitle = TitleManager.getFormattedTitle(unlockedTitle, cap.getGenderSetting());
                        if (clientPlayer.getName().getString().equals(targetPlayerName.getUnformattedComponentText())) {
                            formattedTitle.modifyStyle(s -> s.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/titles display " + unlockedTitle.getID().toString())));
                        }
                        component.appendSibling(new TranslationTextComponent("chat.advancement.append", formattedTitle));
                        event.setMessage(component);
                    });
                }
            }
        }
    }

    private static Title processKey(String key) {
        List<String> keyParts = new ArrayList<>(Arrays.asList(key.split("[/.:]")));
        TitlesMod.LOG.info(Arrays.toString(keyParts.toArray()));
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
                .map(ModInfo::getModId)
                .filter(id -> !(id.equals("forge") || id.equals("FML") || id.equals("mcp")))
                .collect(Collectors.toList());

        // See if one of the parts is a mod id.
        String possibleModId = "";
        for (String part : keyParts) {
            if (modList.contains(part)) {
                possibleModId = part;
                break;
            }
        }

        String titleString = "";
        if (!possibleModId.equals("")) {
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