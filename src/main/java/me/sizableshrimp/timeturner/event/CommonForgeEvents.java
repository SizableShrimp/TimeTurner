package me.sizableshrimp.timeturner.event;

import com.mojang.brigadier.CommandDispatcher;
import me.sizableshrimp.timeturner.TimeTurnerMod;
import me.sizableshrimp.timeturner.command.TimeTurnerCommand;
import net.minecraft.commands.CommandSourceStack;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = TimeTurnerMod.MODID)
public class CommonForgeEvents {
    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();

        TimeTurnerCommand.register(dispatcher);
    }
}
