package aurilux.titles.client.handler;

import aurilux.titles.api.TitleInfo;
import aurilux.titles.api.TitlesAPI;
import aurilux.titles.client.ModKeybindings;
import aurilux.titles.client.gui.GuiTitleSelection;
import aurilux.titles.common.Titles;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.relauncher.Side;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.StreamSupport;

@Mod.EventBusSubscriber(value = Side.CLIENT, modid = Titles.MOD_ID)
public class ClientEventHandler {
    @SubscribeEvent
    public static void onKeyInput(InputEvent.KeyInputEvent event) {
        if (ModKeybindings.OPEN_TITLE_SELECTION.isPressed()) {
            EntityPlayer player = Minecraft.getMinecraft().player;
            FMLClientHandler.instance().displayGuiScreen(player, new GuiTitleSelection(player));
        }
    }

    @SubscribeEvent
    // All this just to append the title to the advancement chat message...
    public static void onClientReceivedChat(ClientChatReceivedEvent event) {
        ITextComponent component = event.getMessage().createCopy();
        if (component instanceof TextComponentTranslation) {
            TextComponentTranslation textComponent = (TextComponentTranslation) component;
            // Check if the lang key is of the advancement variety (task/challenge/goal)
            if (textComponent.getKey().contains("chat.type.advancement.")) {
                // Go through the siblings of this text component so we can find the one with the information on the
                // advancement that was just unlocked
                StreamSupport.stream(textComponent.spliterator(), false)
                        .flatMap(tc -> tc.getSiblings().stream())
                        .filter(tc -> tc instanceof TextComponentTranslation)
                        .map(tc -> ((TextComponentTranslation) tc).getKey())
                        .map(key -> {
                            key = key.replaceAll("[/W:]", ".");
                            if (key.startsWith("advancement")) {
                                key = key.substring(key.indexOf(".") + 1);
                            }
                            if (key.endsWith(".title")) { // This is mostly just for vanilla minecraft's lang entries
                                key = key.substring(0, key.indexOf(".title"));
                            }
                            return key;
                        })
                        .findFirst().ifPresent(advancementKey -> {
                            // Once we get the lang key for the advancement that was sent to the text component, we need
                            // to do some processing and find an applicable title, if any
                            Loader.instance().getActiveModList().stream()
                                    .map(ModContainer::getModId)
                                    .filter(id -> !(id.equals("forge") || id.equals("FML") || id.equals("mcp")))
                                    .forEach(id -> {
                                        String titleKey;
                                        // First check if the next section of the advancement key string is a mod id...
                                        int dotIndex = advancementKey.indexOf(".");
                                        if (dotIndex != -1 && advancementKey.substring(0, dotIndex).equals(id)) {
                                            titleKey = advancementKey.replaceFirst(".", ":");
                                        }
                                        else { // ...if not, add the mod id to it and see if that works
                                            titleKey = id + ":" + advancementKey;
                                        }

                                        TitleInfo title = TitlesAPI.getTitleFromKey(titleKey);
                                        if (!title.equals(TitleInfo.NULL_TITLE)) {
                                            component.appendText(" and earned the title [" +
                                                    TitlesAPI.internalHandler.getFormattedTitle(title) + "]");
                                            event.setMessage(component);
                                        }
                                    });
                        });
            }
        }
    }
}