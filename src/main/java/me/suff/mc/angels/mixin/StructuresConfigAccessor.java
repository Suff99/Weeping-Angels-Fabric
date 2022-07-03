package me.suff.mc.angels.mixin;

import net.minecraft.world.gen.chunk.StructureConfig;
import net.minecraft.world.gen.chunk.StructuresConfig;
import net.minecraft.world.gen.feature.StructureFeature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(StructuresConfig.class)
public interface StructuresConfigAccessor {

    @Accessor("structures")
    void setStructures(Map< StructureFeature< ? >, StructureConfig > structuresSpacingMap);
}