package com.minerarcana.naming.screen;

import com.minerarcana.naming.Naming;
import com.minerarcana.naming.content.NamingText;
import com.minerarcana.naming.target.INamingTarget;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.Objects;

public class NamerScreen extends Screen {
    private static final ResourceLocation LOCATION = Naming.rl("textures/screen/namer.png");


    private final int imageWidth = 122;
    private final int imageHeight = 28;

    private final INamingTarget namingTarget;
    private EditBox name;

    public NamerScreen(INamingTarget namingTarget) {
        super(NamingText.SCREEN_TITLE);
        this.namingTarget = namingTarget;
    }

    @Override
    public void tick() {
        super.tick();
        this.name.tick();
    }

    @Override
    public void render(@Nonnull PoseStack matrixStack, int pMouseX, int pMouseY, float pPartialTicks) {
        this.renderBackground(matrixStack);
        this.renderBg(matrixStack);
        super.render(matrixStack, pMouseX, pMouseY, pPartialTicks);
        RenderSystem.disableBlend();
        this.renderFg(matrixStack, pMouseX, pMouseY, pPartialTicks);
    }

    protected void renderBg(PoseStack pMatrixStack) {
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, LOCATION);
        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;
        this.blit(pMatrixStack, i, j, 0, 0, this.imageWidth, this.imageHeight);
    }

    @Override
    protected void init() {
        super.init();
        this.getMinecraft().keyboardHandler.setSendRepeatsToGui(true);
        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;
        this.name = new EditBox(this.font, i + 10, j + 10, 103, 12, NamingText.SCREEN_TITLE);
        this.name.setCanLoseFocus(false);
        this.name.setTextColor(-1);
        this.name.setTextColorUneditable(-1);
        this.name.setBordered(false);
        this.name.setMaxLength(35);
        String nameValue = this.namingTarget.getName();
        if (nameValue != null) {
            this.name.setValue(nameValue);
        }
        this.renderables.add(this.name);
        this.setInitialFocus(this.name);
    }

    @Override
    public void resize(@Nonnull Minecraft pMinecraft, int pWidth, int pHeight) {
        String s = this.name.getValue();
        this.init(pMinecraft, pWidth, pHeight);
        this.name.setValue(s);
    }

    @Override
    public void removed() {
        super.removed();
        this.getMinecraft().keyboardHandler.setSendRepeatsToGui(false);
    }

    @Override
    public boolean keyPressed(int pKeyCode, int pScanCode, int pModifiers) {
        if (pKeyCode == 256 || pKeyCode == 257) {
            this.onClose();
            Objects.requireNonNull(this.getMinecraft().player).closeContainer();
        }

        return this.name.keyPressed(pKeyCode, pScanCode, pModifiers) ||
                this.name.canConsumeInput() ||
                super.keyPressed(pKeyCode, pScanCode, pModifiers);
    }

    @Override
    public void onClose() {
        if (this.name != null) {
            if (namingTarget.matchesOriginal(this.name.getValue())) {
                Naming.network.name(null, namingTarget);
            } else {
                Naming.network.name(this.name.getValue(), namingTarget);
            }
        }
        super.onClose();
    }

    public void renderFg(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTicks) {
        this.name.render(pPoseStack, pMouseX, pMouseY, pPartialTicks);
    }
}
