package com.minerarcana.naming.screen;

import com.google.common.collect.Lists;
import com.minerarcana.naming.Naming;
import com.minerarcana.naming.blockentity.IButtoned;
import com.minerarcana.naming.container.MessageContainer;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.apache.commons.lang3.tuple.Pair;
import xyz.brassgoggledcoders.shadyskies.containersyncing.property.Property;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Objects;

public class MessageScreen<T extends MessageContainer<U>, U extends Enum<U> & IButtoned<U>> extends AbstractContainerScreen<T> {
    private final ResourceLocation location;
    private EditBox nameField;
    private List<EditBox> listenerFields;

    public MessageScreen(T pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
        this.imageHeight = 97;
        this.imageWidth = 176;
        this.location = Naming.rl("textures/screen/listening_stone.png");
    }

    protected MessageScreen(T pMenu, Inventory pPlayerInventory, Component pTitle, int imageWidth,
                            ResourceLocation location) {
        super(pMenu, pPlayerInventory, pTitle);
        this.imageHeight = 97;
        this.imageWidth = imageWidth;
        this.location = location;
    }

    @Override
    public void containerTick() {
        super.containerTick();
        this.nameField.tick();
        String name = this.menu.getName().get();
        if (name != null && !nameField.getValue().equalsIgnoreCase(name)) {
            nameField.setValue(name);
        }
        for (int i = 0; i < this.listenerFields.size(); i++) {
            String listenerValue = this.menu.getListeners().get(i).getValue().get();
            EditBox listenerField = this.listenerFields.get(i);
            if (listenerValue != null && !listenerValue.equalsIgnoreCase(listenerField.getValue())) {
                listenerField.setValue(listenerValue);
            }
            listenerField.tick();
        }
    }

    @Override
    public void render(@Nonnull PoseStack matrixStack, int pMouseX, int pMouseY, float pPartialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, pMouseX, pMouseY, pPartialTicks);
        RenderSystem.disableBlend();
        this.renderFg(matrixStack, pMouseX, pMouseY, pPartialTicks);
    }

    @Override
    protected void renderLabels(@Nonnull PoseStack pMatrixStack, int pX, int pY) {

    }

    @Override
    protected void renderBg(@Nonnull PoseStack pMatrixStack, float pPartialTicks, int pX, int pY) {
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, location);
        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;
        this.blit(pMatrixStack, i, j, 0, 0, this.imageWidth, this.imageHeight);

        this.blit(pMatrixStack, i + 4, j + 4, 0, this.imageHeight, 110, 16);
        for (int x = 0; x < this.listenerFields.size(); x++) {
            this.blit(pMatrixStack, i + this.imageWidth - 115, x * 18 + j + 22, 0, this.imageHeight, 110, 16);
        }
    }

    public void renderFg(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTicks) {
        this.nameField.render(pPoseStack, pMouseX, pMouseY, pPartialTicks);
        for (EditBox textFieldWidget : this.listenerFields) {
            textFieldWidget.render(pPoseStack, pMouseX, pMouseY, pPartialTicks);
        }
    }

    @Override
    protected void init() {
        super.init();
        this.getMinecraft().keyboardHandler.setSendRepeatsToGui(true);
        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;
        this.nameField = createTextField(i + 7, j + 8, this.menu.getName());
        this.nameField.setFocus(true);
        this.setInitialFocus(this.nameField);
        this.addWidget(this.nameField);
        this.listenerFields = Lists.newArrayList();
        for (int x = 0; x < this.menu.getListeners().size(); x++) {
            Pair<Property<Integer>, Property<String>> properties = this.menu.getListeners().get(x);
            MessageTypeButton<U> button = new MessageTypeButton<>(
                    i + 4,
                    x * 18 + j + 22,
                    menu.getEnum(),
                    properties.getLeft(),
                    getMenu().getPropertyManager(),
                    location
            );
            this.addWidget(button);
            this.renderables.add(button);
            EditBox editBox = createTextField(i + this.imageWidth - 111, x * 18 + j + 26, properties.getRight());
            this.listenerFields.add(editBox);
            this.addWidget(editBox);
        }
    }

    protected EditBox createTextField(int x, int y, Property<String> property) {
        EditBox textFieldWidget = new EditBox(this.font, x, y, 103, 12, Component.empty());
        textFieldWidget.setCanLoseFocus(true);
        textFieldWidget.setEditable(true);
        textFieldWidget.setTextColor(-1);
        textFieldWidget.setTextColorUneditable(-1);
        textFieldWidget.setBordered(false);
        textFieldWidget.setMaxLength(35);
        String priorValue = property.get();
        if (priorValue != null) {
            textFieldWidget.setValue(priorValue);
        }
        textFieldWidget.setResponder(value -> this.menu.getPropertyManager()
                .updateServer(property, value)
        );
        this.renderables.add(textFieldWidget);
        return textFieldWidget;
    }

    @Override
    public void resize(@Nonnull Minecraft pMinecraft, int pWidth, int pHeight) {
        this.init(pMinecraft, pWidth, pHeight);
        this.nameField.setValue(this.menu.getName().getOrElse(""));
        List<Pair<Property<Integer>, Property<String>>> listenerProperties = this.menu.getListeners();
        for (int i = 0; i < listenerProperties.size(); i++) {
            this.listenerFields.get(i).setValue(listenerProperties.get(i).getRight().getOrElse(""));
        }
    }

    @Override
    public boolean keyPressed(int pKeyCode, int pScanCode, int pModifiers) {
        if (pKeyCode == 256) {
            Objects.requireNonNull(this.getMinecraft().player).closeContainer();
        }

        return this.nameField.keyPressed(pKeyCode, pScanCode, pModifiers) ||
                this.nameField.canConsumeInput() ||
                this.listenerFields.stream().anyMatch(message ->
                        message.keyPressed(pKeyCode, pScanCode, pModifiers)
                                || message.canConsumeInput()
                ) ||
                super.keyPressed(pKeyCode, pScanCode, pModifiers);
    }

    @Override
    public void removed() {
        super.removed();
        this.getMinecraft().keyboardHandler.setSendRepeatsToGui(false);
    }
}
