package com.minerarcana.naming.mixin;

import com.minerarcana.naming.blockentity.IMessageCarrier;
import net.minecraft.tileentity.SignTileEntity;
import net.minecraft.util.text.ITextComponent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Mixin(value = SignTileEntity.class, remap = false)
public class SignTileEntityMixin implements IMessageCarrier {
    @Shadow
    @Final
    private ITextComponent[] messages;

    @Override
    public Collection<String> getMessages() {
        return Arrays.stream(messages)
                .map(ITextComponent::getString)
                .filter(string -> !string.isEmpty())
                .collect(Collectors.toList());
    }
}
