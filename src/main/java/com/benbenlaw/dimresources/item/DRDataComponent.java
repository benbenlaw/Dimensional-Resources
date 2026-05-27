package com.benbenlaw.dimresources.item;

import com.benbenlaw.dimresources.DimResources;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class DRDataComponent {

    public static final DeferredRegister<DataComponentType<?>> COMPONENTS = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE,  DimResources.MOD_ID);

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Identifier>> PLANET = COMPONENTS.register("planet", () ->
            DataComponentType.<Identifier>builder()
                    .persistent(Identifier.CODEC)
                    .networkSynchronized(Identifier.STREAM_CODEC)
                    .cacheEncoding()
                    .build());

}
