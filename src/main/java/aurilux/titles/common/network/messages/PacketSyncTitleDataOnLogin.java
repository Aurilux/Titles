package aurilux.titles.common.network.messages;

import aurilux.titles.common.TitleInfo;
import aurilux.titles.common.TitleManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.ByteBufUtils;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class PacketSyncTitleDataOnLogin extends AbstractPacket<PacketSyncTitleDataOnLogin> {
    private UUID playerUUID;
    private Set<TitleInfo> playerTitleData;
    private Map<UUID, TitleInfo> selectedTitleData;

    private Gson gson = new Gson();

    public PacketSyncTitleDataOnLogin() {}

    public PacketSyncTitleDataOnLogin(EntityPlayer player) {
        this.playerUUID = player.getUniqueID();
        this.playerTitleData = TitleManager.getObtainedTitles(player.getUniqueID());
        this.selectedTitleData = TitleManager.getAllSelectedTitles();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, playerUUID.toString());
        ByteBufUtils.writeUTF8String(buf, gson.toJson(playerTitleData));
        ByteBufUtils.writeUTF8String(buf, gson.toJson(selectedTitleData));
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        playerUUID = UUID.fromString(ByteBufUtils.readUTF8String(buf));
        Type type1 = new TypeToken<Set<TitleInfo>>(){}.getType();
        playerTitleData = gson.fromJson(ByteBufUtils.readUTF8String(buf), type1);
        Type type2 = new TypeToken<Map<UUID, TitleInfo>>(){}.getType();
        selectedTitleData = gson.fromJson(ByteBufUtils.readUTF8String(buf), type2);
    }

    @Override
    public void handleClientSide(PacketSyncTitleDataOnLogin message, EntityPlayer player) {
        TitleManager.setObtainedTitles(message.playerUUID, message.playerTitleData);
        TitleManager.setAllSelectedTitles(message.selectedTitleData);
    }

    @Override
    public void handleServerSide(PacketSyncTitleDataOnLogin message, EntityPlayer player) {
        //NOOP
    }
}