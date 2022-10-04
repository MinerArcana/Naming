package com.minerarcana.naming.renderer;

import com.minerarcana.naming.blockentity.ISideText;
import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.level.block.entity.BlockEntity;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

public class SideTextBlockRenderer<T extends BlockEntity & ISideText> implements BlockEntityRenderer<T> {
    private final Font font;

    public SideTextBlockRenderer(BlockEntityRendererProvider.Context context) {
        this.font = context.getFont();
    }

    @Override
    @ParametersAreNonnullByDefault
    public void render(T pBlockEntity, float pPartialTicks, PoseStack pMatrixStack,
                       MultiBufferSource pBuffer, int pCombinedLight, int pCombinedOverlay) {
        pMatrixStack.pushPose();
        pMatrixStack.scale(0.010816667F, -0.010816667F, 0.010816667F);
        int i = pBlockEntity.getTextColor().getTextColor();
        int j = (int) ((double) NativeImage.getR(i) * 0.4D);
        int k = (int) ((double) NativeImage.getG(i) * 0.4D);
        int l = (int) ((double) NativeImage.getB(i) * 0.4D);
        int i1 = NativeImage.combine(0, l, k, j);
        pMatrixStack.translate(46, -10, 46);
        for (Direction direction : Direction.values()) {
            if (pBlockEntity.renderSide(direction)) {
                pMatrixStack.pushPose();

                pMatrixStack.mulPose(Vector3f.YP.rotationDegrees(direction.toYRot()));
                if (direction.getAxis() == Direction.Axis.X) {
                    pMatrixStack.mulPose(Vector3f.YP.rotationDegrees(180));
                }
                pMatrixStack.translate(0, -36.5, 48.25);
                for (int k1 = 0; k1 < 4; ++k1) {
                    FormattedCharSequence reorderingProcessor = pBlockEntity.getRenderedMessage(k1, (textComponent) -> {
                        List<FormattedCharSequence> list = font.split(textComponent, 90);
                        return list.isEmpty() ? FormattedCharSequence.EMPTY : list.get(0);
                    });
                    if (reorderingProcessor != null) {
                        float f3 = (float) (-font.width(reorderingProcessor) / 2);
                        font.drawInBatch(reorderingProcessor, f3, (float) (k1 * 10 - 20), i1, false,
                                pMatrixStack.last().pose(), pBuffer, false, 0, pCombinedLight);
                    }
                }
                pMatrixStack.popPose();
            }
        }

        pMatrixStack.popPose();
    }
}
