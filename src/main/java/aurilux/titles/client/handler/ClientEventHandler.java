package aurilux.titles.client.handler;

import aurilux.titles.api.Title;
import aurilux.titles.api.TitlesAPI;
import aurilux.titles.client.Keybinds;
import aurilux.titles.client.gui.GuiTitleSelection;
import aurilux.titles.common.TitlesMod;
import aurilux.titles.common.core.TitleRegistry;
import aurilux.titles.common.util.SideUtil;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.advancements.Advancement;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.advancements.AdvancementEntryGui;
import net.minecraft.client.gui.advancements.AdvancementTabGui;
import net.minecraft.client.gui.advancements.AdvancementsScreen;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.RenderNameplateEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.loading.moddiscovery.ModInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_T;

@Mod.EventBusSubscriber(modid = TitlesMod.ID, value = Dist.CLIENT)
public class ClientEventHandler {
    @SubscribeEvent
    public static void onClientTick(final TickEvent.ClientTickEvent event) {
        if (Keybinds.openTitleSelection.isPressed()) {
            PlayerEntity player = Minecraft.getInstance().player;
            TitlesAPI.instance().getCapability(player).ifPresent(cap -> {
                Minecraft.getInstance().displayGuiScreen(new GuiTitleSelection(player, cap));
            });
        }
    }

    @SubscribeEvent
    //TODO incomplete
    public static void onDrawScreen(GuiScreenEvent.DrawScreenEvent.Post event) {
        if (!(event.getGui() instanceof AdvancementsScreen)) {
            return;
        }

        AdvancementsScreen screen = (AdvancementsScreen) event.getGui();
        AdvancementTabGui tabGui = screen.selectedTab;
        Minecraft mc = screen.getMinecraft();
        for (Map.Entry<Advancement, AdvancementEntryGui> entry : tabGui.guis.entrySet()) {
            Advancement advancement = entry.getKey();
            String advancementId = advancement.getId().toString();
            AdvancementEntryGui entryGui = entry.getValue();
            Title title = TitleRegistry.INSTANCE.getTitle(advancementId);
            if (!title.isNull()) {
                mc.fontRenderer.drawString(event.getMatrixStack(), advancementId, entryGui.getX() + MathHelper.floor(tabGui.scrollX),
                        entryGui.getY() + MathHelper.floor(tabGui.scrollY), 0xFFFFFF);
            }
        }
    }

    @SubscribeEvent
    public static void onRenderNameplate(RenderNameplateEvent event) {
        if (!(event.getEntity() instanceof PlayerEntity)) return;

        /*
        TODO Come back to this later. Might have to use Neat's method instead.
        PlayerEntity player = (PlayerEntity) event.getEntity();
        TitlesAPI.instance().getCapability(player).ifPresent(c -> title = c.getDisplayTitle());
        if (title.isNull()) return;
        event.setResult(Event.Result.DENY);

        EntityRendererManager rendererManager = event.getEntityRenderer().getRenderManager();
        String displayName = event.getContent();
        double distance = rendererManager.squareDistanceTo(player);
        if (!(distance > 4096.0D)) {
            boolean notDiscrete = !player.isDiscrete();
            float startHeight = player.getHeight() + 0.5F;
            int deadmau5Offset = "deadmau5".equals(displayName) ? -10 : 0;
            MatrixStack matrixStack = event.getMatrixStack();
            matrixStack.push();
            matrixStack.translate(0.0D, startHeight, 0.0D);
            matrixStack.rotate(rendererManager.getCameraOrientation());
            matrixStack.scale(-0.025F, -0.025F, 0.025F);
            Matrix4f matrix4f = matrixStack.getLast().getMatrix();
            float f1 = Minecraft.getInstance().gameSettings.getTextBackgroundOpacity(0.25F);
            int j = (int)(f1 * 255.0F) << 24;
            FontRenderer fontrenderer = event.getEntityRenderer().getFontRendererFromRenderManager();
            float f2 = (float)(-fontrenderer.getStringWidth(displayName) / 2);
            fontrenderer.renderString(displayName, f2, (float)deadmau5Offset, 553648127, false, matrix4f, event.getRenderTypeBuffer(), notDiscrete, j, event.getPackedLight());
            if (notDiscrete) {
                fontrenderer.renderString(displayName, f2, (float)deadmau5Offset, -1, false, matrix4f, event.getRenderTypeBuffer(), false, 0, event.getPackedLight());
            }
            matrixStack.pop();
        }
         */
    }
    
    @SubscribeEvent
    public static void onClientReceivedChat(ClientChatReceivedEvent event) {
        IFormattableTextComponent component = event.getMessage().deepCopy();
        if (component instanceof TranslationTextComponent) {
            TranslationTextComponent textComponent = (TranslationTextComponent) component;
            // Check if the lang key is of the advancement variety (task/challenge/goal)
            if (textComponent.getKey().contains("chat.type.advancement.")) {
                // Go through the siblings of this text component so we can find the one with the information on the
                // advancement that was just unlocked
                textComponent.getSiblings().stream()
                        .flatMap(tc -> tc.getSiblings().stream())
                        .filter(tc -> tc instanceof TranslationTextComponent)
                        .map(tc -> ((TranslationTextComponent) tc).getKey())
                        .map(ClientEventHandler::processKey)
                        .map(TitleRegistry.INSTANCE::getTitle)
                        .filter(title -> !title.isNull())
                        .findFirst().ifPresent(title -> {
                            component.append(new TranslationTextComponent(SideUtil.translate("chat.advancement.append",
                                    TitlesAPI.instance().getFormattedTitle(title, false))));
                            event.setMessage(component);
                        });
            }
        }
    }

    // Once we get the lang key for the advancement that was sent to the text component, we need
    // to do some processing and find an applicable title, if any
    private static String processKey(String key) {
        List<String> keyParts = new ArrayList<>(Arrays.asList(key.split("[/.:]")));

        // Mod authors alternate using "advancement" or "advancements"
        keyParts.removeIf(part -> part.startsWith("advancement") || part.equals("title") || part.equals("name"));

        List<String> modList = ModList.get().getMods().stream()
                .map(ModInfo::getModId)
                .filter(id -> !(id.equals("forge") || id.equals("FML") || id.equals("mcp")))
                .collect(Collectors.toList());

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
                if (TitleRegistry.INSTANCE.getTitlesMap().containsKey(testKey)) {
                    return testKey;
                }
            }
        }

        // Follows a pattern we're not familiar with.
        return "";
    }
}