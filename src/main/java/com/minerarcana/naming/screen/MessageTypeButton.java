package com.minerarcana.naming.screen;

import com.minerarcana.naming.blockentity.IButtoned;
import com.minerarcana.naming.network.property.Property;
import com.minerarcana.naming.network.property.PropertyManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

import javax.annotation.Nonnull;

public class MessageTypeButton<T extends Enum<T> & IButtoned<T>> extends Button {
    private final Property<Integer> property;
    private final PropertyManager propertyManager;
    private final Class<T> tClass;
    private final ResourceLocation location;

    public MessageTypeButton(int pX, int pY, Class<T> aClass, Property<Integer> property, PropertyManager propertyManager,
                             ResourceLocation location) {
        super(pX, pY, 59, 16, TextComponent.EMPTY, button -> {
        });
        this.property = property;
        this.propertyManager = propertyManager;
        this.tClass = aClass;
        this.location = location;
    }

    @Override
    @Nonnull
    public Component getMessage() {
        return tClass.getEnumConstants()[property.getOrElse(0)].getMessage();
    }

    @Override
    public void onPress() {
        propertyManager.updateServer(property, tClass.getEnumConstants()[property.getOrElse(0)].cycle().ordinal());
    }

    @Override
    public void renderButton(@Nonnull PoseStack pMatrixStack, int pMouseX, int pMouseY, float pPartialTicks) {
        Minecraft minecraft = Minecraft.getInstance();
        Font font = minecraft.font;
        RenderSystem.setShaderTexture(0, location);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, this.alpha);
        int i = this.isHovered ? 1 : 0;
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableDepthTest();
        this.blit(pMatrixStack, this.x, this.y, 0, 129 + i * 16, this.width, this.height);
        this.renderBg(pMatrixStack, minecraft, pMouseX, pMouseY);
        int j = getFGColor();
        drawCenteredString(pMatrixStack, font, this.getMessage(), this.x + this.width / 2 - 1, this.y + (this.height - 8) / 2, j | Mth.ceil(this.alpha * 255.0F) << 24);

    }
}
