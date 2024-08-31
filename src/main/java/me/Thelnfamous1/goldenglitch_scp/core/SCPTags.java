package me.Thelnfamous1.goldenglitch_scp.core;

import me.Thelnfamous1.goldenglitch_scp.SCPMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class SCPTags {
    public static class Blocks{
        public static final TagKey<Block> SEE_THROUGH_BLOCKS = createBlockTag("see_through_blocks");

        private static TagKey<Block> createBlockTag(String name) {
            return TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(SCPMod.MODID, name));
        }
    }
}
