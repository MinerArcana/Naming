package com.minerarcana.naming.screen;

import com.minerarcana.naming.blockentity.IButtoned;
import com.minerarcana.naming.network.property.Property;
import com.minerarcana.naming.network.property.PropertyManager;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

import javax.annotation.Nonnull;

public class MessageTypeButton<T extends Enum<T> & IButtoned<T>> extends Button {
    private final Property<Integer> property;
    private final PropertyManager propertyManager;
    private final Class<T> tClass;
    private final ResourceLocation location;

    public MessageTypeButton(int pX, int pY, Class<T> aClass, Property<Integer> property, PropertyManager propertyManager,
                             ResourceLocation location) {
        super(pX, pY, 59, 16, StringTextComponent.EMPTY, button -> {
        });
        this.property = property;
        this.propertyManager = propertyManager;
        this.tClass = aClass;
        this.location = location;
    }

    @Override
    @Nonnull
    public ITextComponent getMessage() {
        return tClass.getEnumConstants()[property.getOrElse(0)].getMessage();
    }

    @Override
    public void onPress() {
        propertyManager.updateServer(property, tClass.getEnumConstants()[property.getOrElse(0)].cycle().ordinal());
    }

    @Override
    @SuppressWarnings("deprecation")
    public void renderButton(@Nonnull MatrixStack pMatrixStack, int pMouseX, int pMouseY, float pPartialTicks) {
        Minecraft minecraft = Minecraft.getInstance();
        FontRenderer fontrenderer = minecraft.font;
        minecraft.getTextureManager().bind(location);
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, this.alpha);
        int i = this.isHovered() ? 1 : 0;
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableDepthTest();
        this.blit(pMatrixStack, this.x, this.y, 0, 129 + i * 16, this.width, this.height);
        this.renderBg(pMatrixStack, minecraft, pMouseX, pMouseY);
        int j = getFGColor();
        drawCenteredString(pMatrixStack, fontrenderer, this.getMessage(), this.x + this.width / 2 - 1, this.y + (this.height - 8) / 2, j | MathHelper.ceil(this.alpha * 255.0F) << 24);

    }
}
