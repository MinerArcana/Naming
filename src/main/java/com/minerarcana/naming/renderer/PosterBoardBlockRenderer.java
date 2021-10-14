package com.minerarcana.naming.renderer;

import com.minerarcana.naming.blockentity.PosterBoardBlockEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.texture.NativeImage;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.Direction;
import net.minecraft.util.IReorderingProcessor;
import net.minecraft.util.math.vector.Vector3f;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

public class PosterBoardBlockRenderer extends TileEntityRenderer<PosterBoardBlockEntity> {
    public PosterBoardBlockRenderer(TileEntityRendererDispatcher dispatcher) {
        super(dispatcher);
    }

    @Override
    @ParametersAreNonnullByDefault
    public void render(PosterBoardBlockEntity pBlockEntity, float pPartialTicks, MatrixStack pMatrixStack,
                       IRenderTypeBuffer pBuffer, int pCombinedLight, int pCombinedOverlay) {
        pMatrixStack.pushPose();
        FontRenderer fontrenderer = this.renderer.getFont();
        pMatrixStack.scale(0.017816667F, -0.017816667F, 0.017816667F);
        int i = pBlockEntity.getColor().getTextColor();
        int j = (int) ((double) NativeImage.getR(i) * 0.4D);
        int k = (int) ((double) NativeImage.getG(i) * 0.4D);
        int l = (int) ((double) NativeImage.getB(i) * 0.4D);
        int i1 = NativeImage.combine(0, l, k, j);
        pMatrixStack.translate(28, 10, 28);
        for (Direction direction : Direction.values()) {
            if (pBlockEntity.renderSide(direction)) {
                pMatrixStack.pushPose();

                pMatrixStack.mulPose(Vector3f.YP.rotationDegrees(direction.toYRot()));
                pMatrixStack.translate(0, -36.5, 28.25);
                for (int k1 = 0; k1 < 4; ++k1) {
                    IReorderingProcessor reorderingProcessor = pBlockEntity.getRenderMessage(k1, (textComponent) -> {
                        List<IReorderingProcessor> list = fontrenderer.split(textComponent, 90);
                        return list.isEmpty() ? IReorderingProcessor.EMPTY : list.get(0);
                    });
                    if (reorderingProcessor != null) {
                        float f3 = (float) (-fontrenderer.width(reorderingProcessor) / 2);
                        fontrenderer.drawInBatch(reorderingProcessor, f3, (float) (k1 * 10 - 20), i1, false, pMatrixStack.last().pose(), pBuffer, false, 0, pCombinedLight);
                    }
                }
                pMatrixStack.popPose();
            }
        }

        pMatrixStack.popPose();
    }
}
