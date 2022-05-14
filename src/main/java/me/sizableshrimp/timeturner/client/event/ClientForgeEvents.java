package me.sizableshrimp.timeturner.client.event;

import me.sizableshrimp.timeturner.TimeTurnerMod;
import me.sizableshrimp.timeturner.client.ClientTimeTurner;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.Input;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.MovementInputUpdateEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = TimeTurnerMod.MODID, value = Dist.CLIENT)
public class ClientForgeEvents {
    @SubscribeEvent
    public static void onMovementInputUpdate(MovementInputUpdateEvent event) {
        if (!ClientTimeTurner.isTimeReverting())
            return;

        Input input = event.getInput();

        input.up = false;
        input.down = false;
        input.left = false;
        input.right = false;
        input.forwardImpulse = 0;
        input.leftImpulse = 0;
        input.jumping = false;
        input.shiftKeyDown = false;
    }

    @SubscribeEvent
    public static void onCameraSetup(EntityViewRenderEvent.CameraSetup event) {
        if (!ClientTimeTurner.isTimeReverting())
            return;

        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null)
            return;

        Entity cameraEntity = mc.getCameraEntity();
        if (cameraEntity == null)
            cameraEntity = mc.player;

        // These conditions determine whether the normal camera rotations are applied
        if (mc.options.getCameraType().isFirstPerson() && cameraEntity == mc.player && (!(cameraEntity instanceof LivingEntity livingEntity) || !livingEntity.isSleeping())) {
            float partialTicks = (float) event.getPartialTicks();

            Camera camera = event.getCamera();
            camera.setRotation(Mth.lerp(partialTicks, mc.player.yHeadRot, mc.player.yHeadRotO), Mth.lerp(partialTicks, mc.player.getXRot(), mc.player.xRotO));
        }
    }
}
