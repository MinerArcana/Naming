package com.minerarcana.naming.screen;

import com.google.common.collect.Lists;
import com.minerarcana.naming.Naming;
import com.minerarcana.naming.blockentity.IButtoned;
import com.minerarcana.naming.container.MessageContainer;
import com.minerarcana.naming.network.property.Property;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Objects;

public class MessageScreen<T extends MessageContainer<U>, U extends Enum<U> & IButtoned<U>> extends ContainerScreen<T> {
    private final ResourceLocation location;
    private TextFieldWidget nameField;
    private List<TextFieldWidget> listenerFields;

    public MessageScreen(T pMenu, PlayerInventory pPlayerInventory, ITextComponent pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
        this.imageHeight = 97;
        this.imageWidth = 176;
        this.location = Naming.rl("textures/screen/listening_stone.png");
    }

    protected MessageScreen(T pMenu, PlayerInventory pPlayerInventory, ITextComponent pTitle, int imageWidth,
                            ResourceLocation location) {
        super(pMenu, pPlayerInventory, pTitle);
        this.imageHeight = 97;
        this.imageWidth = imageWidth;
        this.location = location;
    }

    @Override
    public void tick() {
        super.tick();
        this.nameField.tick();
        String name = this.menu.getName().get();
        if (name != null && !nameField.getValue().equalsIgnoreCase(name)) {
            nameField.setValue(name);
        }
        for (int i = 0; i < this.listenerFields.size(); i++) {
            String listenerValue = this.menu.getListeners().get(i).getValue().get();
            TextFieldWidget listenerField = this.listenerFields.get(i);
            if (listenerValue != null && !listenerValue.equalsIgnoreCase(listenerField.getValue())) {
                listenerField.setValue(listenerValue);
            }
            listenerField.tick();
        }
    }

    @Override
    public void render(@Nonnull MatrixStack matrixStack, int pMouseX, int pMouseY, float pPartialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, pMouseX, pMouseY, pPartialTicks);
        RenderSystem.disableBlend();
        this.renderFg(matrixStack, pMouseX, pMouseY, pPartialTicks);
    }

    @Override
    protected void renderLabels(@Nonnull MatrixStack pMatrixStack, int pX, int pY) {

    }

    @Override
    @SuppressWarnings("deprecation")
    protected void renderBg(@Nonnull MatrixStack pMatrixStack, float pPartialTicks, int pX, int pY) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.getMinecraft().getTextureManager().bind(location);
        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;
        this.blit(pMatrixStack, i, j, 0, 0, this.imageWidth, this.imageHeight);

        this.blit(pMatrixStack, i + 4, j + 4, 0, this.imageHeight, 110, 16);
        for (int x = 0; x < this.listenerFields.size(); x++) {
            this.blit(pMatrixStack, i + this.imageWidth - 115, x * 18 + j + 22, 0, this.imageHeight, 110, 16);
        }
    }

    public void renderFg(MatrixStack pPoseStack, int pMouseX, int pMouseY, float pPartialTicks) {
        this.nameField.render(pPoseStack, pMouseX, pMouseY, pPartialTicks);
        for (TextFieldWidget textFieldWidget : this.listenerFields) {
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
        this.listenerFields = Lists.newArrayList();
        for (int x = 0; x < this.menu.getListeners().size(); x++) {
            Pair<Property<Integer>, Property<String>> properties = this.menu.getListeners().get(x);
            this.addButton(new MessageTypeButton<>(
                    i + 4,
                    x * 18 + j + 22,
                    menu.getEnum(),
                    properties.getLeft(),
                    getMenu().getPropertyManager(),
                    location
            ));
            this.listenerFields.add(createTextField(i + this.imageWidth - 111, x * 18 + j + 26, properties.getRight()));
        }
    }

    protected TextFieldWidget createTextField(int x, int y, Property<String> property) {
        TextFieldWidget textFieldWidget = new TextFieldWidget(this.font, x, y, 103, 12, StringTextComponent.EMPTY);
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
        this.children.add(textFieldWidget);
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
