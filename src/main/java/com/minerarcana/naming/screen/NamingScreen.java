package com.minerarcana.naming.screen;

import com.minerarcana.naming.content.NamingText;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nonnull;
import java.util.Objects;

public class NamingScreen extends Screen {
    private static final ResourceLocation LOCATION = new ResourceLocation("textures/gui/container/anvil.png");

    protected int imageWidth = 176;
    protected int imageHeight = 166;

    private TextFieldWidget name;

    public NamingScreen() {
        super(NamingText.SCREEN_TITLE);
    }

    @Override
    public void tick() {
        super.tick();
        this.name.tick();
    }

    @Override
    public void render(@Nonnull MatrixStack matrixStack, int pMouseX, int pMouseY, float pPartialTicks) {
        this.renderBackground(matrixStack);
        this.renderBg(matrixStack);
        super.render(matrixStack, pMouseX, pMouseY, pPartialTicks);
        RenderSystem.disableBlend();
        this.renderFg(matrixStack, pMouseX, pMouseY, pPartialTicks);
    }

    @SuppressWarnings("deprecation")
    protected void renderBg(MatrixStack pMatrixStack) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.getMinecraft().getTextureManager().bind(LOCATION);
        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;
        this.blit(pMatrixStack, i, j, 0, 0, this.imageWidth, this.imageHeight);
        this.blit(pMatrixStack, i + 59, j + 20, 0, this.imageHeight, 110, 16);
        this.blit(pMatrixStack, i + 99, j + 45, this.imageWidth, 0, 28, 21);
    }

    @Override
    protected void init() {
        super.init();
        this.getMinecraft().keyboardHandler.setSendRepeatsToGui(true);
        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;
        this.name = new TextFieldWidget(this.font, i + 62, j + 24, 103, 12, new TranslationTextComponent("container.repair"));
        this.name.setCanLoseFocus(false);
        this.name.setTextColor(-1);
        this.name.setTextColorUneditable(-1);
        this.name.setBordered(false);
        this.name.setMaxLength(35);
        this.name.setResponder(this::onNameChanged);
        this.children.add(this.name);
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
        if (pKeyCode == 256) {
            Objects.requireNonNull(this.getMinecraft().player).closeContainer();
        }

        return this.name.keyPressed(pKeyCode, pScanCode, pModifiers) ||
                this.name.canConsumeInput() ||
                super.keyPressed(pKeyCode, pScanCode, pModifiers);
    }

    private void onNameChanged(String text) {
        if (!text.isEmpty()) {
            System.out.println(text);
        }
    }

    public void renderFg(MatrixStack pPoseStack, int pMouseX, int pMouseY, float pPartialTicks) {
        this.name.render(pPoseStack, pMouseX, pMouseY, pPartialTicks);
    }
}
