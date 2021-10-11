package com.minerarcana.naming.content;

import com.minerarcana.naming.Naming;
import com.minerarcana.naming.block.PosterBoardBlock;
import com.minerarcana.naming.blockentity.PosterBoardBlockEntity;
import com.minerarcana.naming.item.PosterBoardBlockItem;
import com.minerarcana.naming.renderer.PosterBoardBlockRenderer;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.item.ItemGroup;

public class NamingBlocks {

    public static final BlockEntry<PosterBoardBlock> POSTER_BOARD = Naming.getRegistrate()
            .object("poster_board")
            .block(PosterBoardBlock::new)
            .tileEntity(PosterBoardBlockEntity::new)
            .renderer(() -> PosterBoardBlockRenderer::new)
            .build()
            .item(PosterBoardBlockItem::new)
            .group(() -> ItemGroup.TAB_MISC)
            .build()
            .register();

    public static void setup() {

    }
}
