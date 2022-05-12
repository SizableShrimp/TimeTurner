package me.sizableshrimp.timeturner;

import com.mojang.logging.LogUtils;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;

@Mod(TimeTurnerMod.MODID)
public class TimeTurnerMod {
    public static final String MODID = "timeturner";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final int MAX_REVERT_TICKS = 15 * 20;

    public TimeTurnerMod() {}
}
