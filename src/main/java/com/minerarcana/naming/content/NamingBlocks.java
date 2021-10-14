package com.minerarcana.naming.content;

import com.minerarcana.naming.Naming;
import com.minerarcana.naming.block.ListeningStone;
import com.minerarcana.naming.block.PosterBoardBlock;
import com.minerarcana.naming.blockentity.PosterBoardBlockEntity;
import com.minerarcana.naming.item.PosterBoardBlockItem;
import com.minerarcana.naming.recipe.NamingRecipeBuilder;
import com.minerarcana.naming.renderer.PosterBoardBlockRenderer;
import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.data.ShapelessRecipeBuilder;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.Tags;

@SuppressWarnings("unused")
public class NamingBlocks {

    public static final BlockEntry<PosterBoardBlock> POSTER_BOARD = Naming.getRegistrate()
            .object("poster_board")
            .block(PosterBoardBlock::new)
            .tileEntity(PosterBoardBlockEntity::new)
            .renderer(() -> PosterBoardBlockRenderer::new)
            .build()
            .item(PosterBoardBlockItem::new)
            .group(() -> ItemGroup.TAB_MISC)
            .recipe((context, provider) -> ShapelessRecipeBuilder.shapeless(context.get())
                    .requires(Items.PAPER)
                    .requires(Tags.Items.STONE)
                    .unlockedBy("item", RegistrateRecipeProvider.hasItem(Items.PAPER))
                    .save(provider)
            )
            .build()
            .register();

    public static final BlockEntry<ListeningStone> LISTENING_STONE = Naming.getRegistrate()
            .object("listening_stone")
            .block(ListeningStone::new)
            .blockstate((context, provider) -> {
                ModelFile on = provider.models().cube(
                        "listening_stone_on",
                        Naming.rl("block/poster_board"),
                        Naming.rl("block/poster_board"),
                        Naming.rl("block/listening_stone_on"),
                        Naming.rl("block/poster_board"),
                        Naming.rl("block/poster_board"),
                        Naming.rl("block/poster_board")
                );
                ModelFile off = provider.models().cube(
                        "listening_stone_off",
                        Naming.rl("block/poster_board"),
                        Naming.rl("block/poster_board"),
                        Naming.rl("block/listening_stone_off"),
                        Naming.rl("block/poster_board"),
                        Naming.rl("block/poster_board"),
                        Naming.rl("block/poster_board")
                );
                provider.horizontalBlock(context.get(), blockState -> blockState.getValue(ListeningStone.LIT) ? on : off);
            })
            .item()
            .recipe((context, provider) -> NamingRecipeBuilder.of(context.get())
                    .withIngredient(Ingredient.of(POSTER_BOARD.get()))
                    .withPattern("listen.*")
                    .withAbility("listening_stone")
                    .build(provider)
            )
            .group(() -> ItemGroup.TAB_MISC)
            .model((context, provider) -> provider.blockItem(context, "_off"))
            .build()
            .register();

    public static void setup() {

    }
}
