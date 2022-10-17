package com.minerarcana.naming.screen;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.player.Input;
import net.minecraft.client.player.LocalPlayer;
import org.jetbrains.annotations.NotNull;

public class ManualKeyboardInput {
    public static void handle(@NotNull LocalPlayer player, @NotNull Options options) {
        Input input = player.input;
        input.up = isKeyClicked(options.keyUp);
        input.down = isKeyClicked(options.keyDown);
        input.left = isKeyClicked(options.keyLeft);
        input.right = isKeyClicked(options.keyRight);
        input.forwardImpulse = calculateImpulse(input.up, input.down);
        input.leftImpulse = calculateImpulse(input.left, input.right);
        input.jumping = isKeyClicked(options.keyJump);
        input.shiftKeyDown = isKeyClicked(options.keyShift);
        if (player.isMovingSlowly()) {
            input.leftImpulse *= 0.3F;
            input.forwardImpulse *= 0.3F;
        }
    }

    private static boolean isKeyClicked(KeyMapping keyMapping) {
        return InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), keyMapping.getKey().getValue());
    }

    private static float calculateImpulse(boolean wayA, boolean wayB) {
        if (wayA == wayB) {
            return 0.0F;
        } else {
            return wayA ? 1.0F : -1.0F;
        }
    }
}
