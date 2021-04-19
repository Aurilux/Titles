package aurilux.titles.client.handler;

import aurilux.titles.api.TitlesAPI;
import aurilux.titles.api.capability.ITitles;
import aurilux.titles.client.Keybinds;
import aurilux.titles.client.gui.TitleSelectionScreen;
import aurilux.titles.common.TitlesMod;
import aurilux.titles.common.core.TitleRegistry;
import aurilux.titles.common.impl.TitlesCapImpl;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.moddiscovery.ModInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Mod.EventBusSubscriber(modid = TitlesAPI.MOD_ID, value = Dist.CLIENT)
public class ClientEventHandler {
    @SubscribeEvent
    public static void onClientTick(final TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) {
            return;
        }

        if (Keybinds.openTitleSelection.isPressed()) {
            PlayerEntity player = Minecraft.getInstance().player;
            if (player != null) {
                TitlesAPI.getCapability(player).ifPresent(cap -> {
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
            // Check if the lang key is of the advancement variety (task/challenge/goal).
            if (textComponent.getKey().contains("chat.type.advancement.")) {
                // Go through the args of this text component so we can find the TranslationTextComponent with the
                // key for the advancement that was just unlocked.
                String playerName = ((ITextComponent) textComponent.getFormatArgs()[0]).getSiblings().get(0).getUnformattedComponentText();
                PlayerEntity player = Minecraft.getInstance().player.world.getPlayers().stream()
                        .filter(pe -> pe.getName().getUnformattedComponentText().equals(playerName))
                        .findFirst().orElse(null);
                ITitles playerCap = TitlesAPI.getCapability(player).orElse(new TitlesCapImpl());
                TranslationTextComponent advancementComp = (TranslationTextComponent) textComponent.getFormatArgs()[1];
                Arrays.stream(advancementComp.getFormatArgs())
                        .filter(tc -> tc instanceof TranslationTextComponent)
                        .map(tc -> ((TranslationTextComponent) tc).getKey())
                        .map(ClientEventHandler::processKey)
                        .map(TitlesAPI.internal()::getTitle)
                        .filter(title -> !title.isNull())
                        .findFirst().ifPresent(title -> {
                            component.append(new TranslationTextComponent("chat.advancement.append",
                                    TitlesAPI.getFormattedTitle(title, playerCap.getGenderSetting())));
                            event.setMessage(component);
                        });
            }
        }
    }

    // Once we get the lang key for the advancement that was sent with the text component, we need to do some
    // processing and find an applicable title, if any.
    // TODO might be better for this to return a title directly
    private static String processKey(String key) {
        List<String> keyParts = new ArrayList<>(Arrays.asList(key.split("[/.:]")));

        // Mod authors alternate using "advancement" or "advancements" for their advancement lang keys. Also remove
        // common suffixes to the lang key such as "title" and "name".
        keyParts.removeIf(part -> part.startsWith("advancement") || part.equals("title") || part.equals("name"));

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

        if (!possibleModId.equals("")) {
            keyParts.remove(possibleModId);
            return possibleModId + ":" + String.join("/", keyParts);
        }
        else {
            for (String modId : modList) {
                String testKey = modId + ":" + String.join("/", keyParts);
                if (TitleRegistry.INSTANCE.getAdvancementTitles().containsKey(testKey)) {
                    return testKey;
                }
            }
        }

        // Follows a pattern we're not familiar with.
        TitlesMod.LOG.debug("Advancement key follows an unfamiliar pattern");
        return "";
    }
}