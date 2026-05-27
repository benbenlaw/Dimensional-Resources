package com.benbenlaw.dimresources.integration.jei;

import com.benbenlaw.dimresources.item.DRDataComponent;
import mezz.jei.api.ingredients.subtypes.ISubtypeInterpreter;
import mezz.jei.api.ingredients.subtypes.UidContext;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;

public class ItemSubtypeInterpreter implements ISubtypeInterpreter<ItemStack> {
    @Override
    public @Nullable Object getSubtypeData(ItemStack stack, UidContext uidContext) {
        return stack.getOrDefault(DRDataComponent.PLANET, "");
    }

}