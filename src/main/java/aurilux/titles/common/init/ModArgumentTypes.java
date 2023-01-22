package aurilux.titles.common.init;

import aurilux.titles.common.TitlesMod;
import aurilux.titles.common.command.argument.TitleArgument;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.commands.synchronization.ArgumentTypeInfos;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModArgumentTypes {
    private static final DeferredRegister<ArgumentTypeInfo<?, ?>> ARGUMENT_TYPES =
            DeferredRegister.create(ForgeRegistries.COMMAND_ARGUMENT_TYPES, TitlesMod.MOD_ID);

    public static final RegistryObject<TitleArgument.Info> TITLE_ARGUMENT = ARGUMENT_TYPES.register("title_argument",
            () -> ArgumentTypeInfos.registerByClass(TitleArgument.class, new TitleArgument.Info()));

    public static void register(IEventBus eventBus) {
        ARGUMENT_TYPES.register(eventBus);
    }
}