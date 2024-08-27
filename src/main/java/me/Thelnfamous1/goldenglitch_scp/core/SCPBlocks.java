package me.Thelnfamous1.goldenglitch_scp.core;

import me.Thelnfamous1.goldenglitch_scp.SCPMod;
import net.neoforged.neoforge.registries.DeferredRegister;

public class SCPBlocks {
    // Create a Deferred Register to hold Blocks which will all be registered under the "examplemod" namespace
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(SCPMod.MODID);
    // Creates a new Block with the id "examplemod:example_block", combining the namespace and path
    //public static final DeferredBlock<Block> EXAMPLE_BLOCK = BLOCKS.registerSimpleBlock("example_block", BlockBehaviour.Properties.of().mapColor(MapColor.STONE));
}
