package aurilux.titles.common.command.argument;

import com.google.gson.JsonObject;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.commands.synchronization.ArgumentSerializer;
import net.minecraft.network.FriendlyByteBuf;

public class TitleArgumentSerializer implements ArgumentSerializer<TitleArgument> {
    public void serializeToNetwork(TitleArgument argument, FriendlyByteBuf byteBuf) {
        byteBuf.writeUtf(argument.getPredicate().equals(TitleArgument.ANY) ? "any" : "display");
    }

    public TitleArgument deserializeFromNetwork(FriendlyByteBuf byteBuf) {
        if ("any".equals(byteBuf.readUtf())) {
            return TitleArgument.any();
        }
        return TitleArgument.display();
    }

    public void serializeToJson(TitleArgument argument, JsonObject json) {
        json.addProperty("type", argument.getPredicate().equals(TitleArgument.ANY) ? "any" : "display");
    }
}