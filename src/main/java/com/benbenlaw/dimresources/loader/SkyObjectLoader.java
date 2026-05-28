package com.benbenlaw.dimresources.loader;

import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.HashMap;
import java.util.Map;

public class SkyObjectLoader extends SimpleJsonResourceReloadListener<SkyObjectData> {

    public static final Map<Identifier, SkyObjectData> SKY_OBJECTS = new HashMap<>();

    public SkyObjectLoader() {
        super(SkyObjectData.CODEC, FileToIdConverter.json("sky_objects"));
    }


    @Override
    protected void apply(Map<Identifier, SkyObjectData> identifierSkyObjectDataMap, ResourceManager resourceManager, ProfilerFiller profilerFiller) {
        SKY_OBJECTS.clear();
        SKY_OBJECTS.putAll(identifierSkyObjectDataMap);
        System.out.println("Loaded " + SKY_OBJECTS.size() + " sky objects from JSON.");
    }
}
