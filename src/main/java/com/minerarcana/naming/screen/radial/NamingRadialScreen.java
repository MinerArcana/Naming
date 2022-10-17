package com.minerarcana.naming.screen.radial;

import com.minerarcana.naming.content.NamingText;
import com.minerarcana.naming.screen.IAllowMovement;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.Screen;

import javax.annotation.Nonnull;

public class NamingRadialScreen extends Screen implements IAllowMovement {
    public NamingRadialScreen() {
        super(NamingText.SCREEN_TITLE);
        this.passEvents = true;
    }

    @Override
    public void render(@Nonnull PoseStack matrixStack, int pMouseX, int pMouseY, float pPartialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, pMouseX, pMouseY, pPartialTicks);
        RenderSystem.disableBlend();
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
