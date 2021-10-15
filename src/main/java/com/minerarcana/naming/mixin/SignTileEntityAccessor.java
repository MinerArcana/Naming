package com.minerarcana.naming.mixin;

import net.minecraft.tileentity.SignTileEntity;
import net.minecraft.util.text.ITextComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = SignTileEntity.class, remap = false)
public interface SignTileEntityAccessor {
    @Accessor
    ITextComponent[] getMessages();
}
