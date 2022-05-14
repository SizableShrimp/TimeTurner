package me.sizableshrimp.timeturner.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import me.sizableshrimp.timeturner.TimeTurnerMod;
import me.sizableshrimp.timeturner.core.TimeTurnerCore;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

public class TimeTurnerCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("timeturner")
                .requires(s -> s.hasPermission(Commands.LEVEL_GAMEMASTERS))
                .then(Commands.argument("revertTicks", IntegerArgumentType.integer(1, TimeTurnerMod.MAX_REVERT_TICKS))
                        .executes(ctx -> {
                            int revertTicks = IntegerArgumentType.getInteger(ctx, "revertTicks");

                            TimeTurnerCore.revertTime(ctx.getSource().getServer(), revertTicks);

                            return Command.SINGLE_SUCCESS;
                        })));
    }
}
