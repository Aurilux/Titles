package aurilux.titles.common.network.messages;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public abstract class AbstractPacket<REQ extends IMessage> implements IMessage, IMessageHandler<REQ, REQ> {
    @Override
    public REQ onMessage(REQ message, MessageContext ctx) {
        Minecraft.getMinecraft().addScheduledTask(new Runnable() {
            // Use anon. Lambda causes classloading issues
            @Override
            public void run() {
                if (ctx.side == Side.SERVER) {
                    handleServerSide(message, ctx.getServerHandler().player);
                }
                else {
                    handleClientSide(message, FMLClientHandler.instance().getClient().player);
                }
            }
        });
        return null;
    }

    /**
     * Handle a packet on the client side. Note this occurs after decoding has completed.
     *
     * @param message
     * @param receiver the player reference
     */
    public abstract void handleClientSide(REQ message, EntityPlayer receiver);

    /**
     * Handle a packet on the server side. Note this occurs after decoding has completed.
     *
     * @param message
     * @param receiver the player reference
     */
    public abstract void handleServerSide(REQ message, EntityPlayer receiver);
}