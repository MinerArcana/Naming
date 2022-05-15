package com.minerarcana.naming.screen;

import com.google.common.collect.Lists;
import com.minerarcana.naming.Naming;
import com.minerarcana.naming.blockentity.SpeakingTarget;
import com.minerarcana.naming.container.SpeakingContainer;
import com.minerarcana.naming.network.property.Property;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.player.Inventory;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Objects;

public class SpeakingStoneScreen extends MessageScreen<SpeakingContainer, SpeakingTarget> {
    private List<EditBox> targetNameFields;

    public SpeakingStoneScreen(SpeakingContainer pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle, 256, Naming.rl("textures/screen/speaking_stone.png"));
    }

    @Override
    public void containerTick() {
        super.containerTick();
        for (int i = 0; i < this.targetNameFields.size(); i++) {
            String listenerValue = this.menu.getTargetNameProperties().get(i).get();
            EditBox listenerField = this.targetNameFields.get(i);
            SpeakingTarget speakingTarget = this.menu.getEnumFor(i);
            if (speakingTarget.isNeedsTargetName()) {
                listenerField.setEditable(true);
                if (listenerValue != null && !listenerValue.equalsIgnoreCase(listenerField.getValue())) {
                    listenerField.setValue(listenerValue);
                }
            } else {
                listenerField.setEditable(false);
                listenerField.setFocus(false);
                listenerField.setValue("");
            }

            listenerField.tick();
        }
    }

    @Override
    protected void renderBg(@Nonnull PoseStack pMatrixStack, float pPartialTicks, int pX, int pY) {
        super.renderBg(pMatrixStack, pPartialTicks, pX, pY);

        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;

        for (int x = 0; x < this.targetNameFields.size(); x++) {
            SpeakingTarget speakingTarget = SpeakingTarget.values()[this.menu.getListeners().get(x).getLeft().getOrElse(0)];
            this.blit(pMatrixStack, i + 61, x * 18 + j + 22, 110, this.imageHeight + (speakingTarget.isNeedsTargetName() ? 0 : 16), 110, 16);
        }
    }

    @Override
    public void renderFg(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTicks) {
        super.renderFg(pPoseStack, pMouseX, pMouseY, pPartialTicks);
        for (EditBox textFieldWidget : this.targetNameFields) {
            textFieldWidget.render(pPoseStack, pMouseX, pMouseY, pPartialTicks);
        }
    }

    @Override
    public void init() {
        super.init();
        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;

        this.targetNameFields = Lists.newArrayList();
        for (int x = 0; x < this.menu.getTargetNameProperties().size(); x++) {
            Property<String> stringProperty = this.menu.getTargetNameProperties().get(x);
            EditBox targetNameWidget = new EditBox(this.font, i + 64, x * 18 + j + 26, 78, 12, TextComponent.EMPTY);
            this.renderables.add(targetNameWidget);
            targetNameWidget.setEditable(this.menu.getEnumFor(x).isNeedsTargetName());
            if (stringProperty.get() != null && this.menu.getEnumFor(x).isNeedsTargetName()) {
                targetNameWidget.setValue(stringProperty.getOrElse(""));
            }
            targetNameWidget.setTextColor(-1);
            targetNameWidget.setTextColorUneditable(-1);
            targetNameWidget.setBordered(false);
            targetNameWidget.setMaxLength(35);
            int finalX = x;
            targetNameWidget.setResponder(value -> {
                if (this.menu.getEnumFor(finalX).isNeedsTargetName()) {
                    this.menu.getPropertyManager().updateServer(stringProperty, value);
                }
            });
            this.targetNameFields.add(targetNameWidget);
        }
    }

    @Override
    public void resize(@Nonnull Minecraft pMinecraft, int pWidth, int pHeight) {
        this.init(pMinecraft, pWidth, pHeight);
        List<Pair<Property<Integer>, Property<String>>> listenerProperties = this.menu.getListeners();
        for (int i = 0; i < listenerProperties.size(); i++) {
            this.targetNameFields.get(i).setValue(listenerProperties.get(i).getRight().getOrElse(""));
        }
    }

    @Override
    public boolean keyPressed(int pKeyCode, int pScanCode, int pModifiers) {
        if (pKeyCode == 256) {
            Objects.requireNonNull(this.getMinecraft().player).closeContainer();
        }

        return this.targetNameFields.stream().anyMatch(message ->
                message.keyPressed(pKeyCode, pScanCode, pModifiers)
                        || message.canConsumeInput()
        ) || super.keyPressed(pKeyCode, pScanCode, pModifiers);
    }
}
