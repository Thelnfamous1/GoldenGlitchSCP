package me.Thelnfamous1.goldenglitch_scp.core;

import me.Thelnfamous1.goldenglitch_scp.SCPMod;
import me.Thelnfamous1.goldenglitch_scp.items.SCP500;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class SCPItems {
    // Create a Deferred Register to hold Items which will all be registered under the "examplemod" namespace
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(SCPMod.MODID);
    // Creates a new BlockItem with the id "examplemod:example_block", combining the namespace and path
    //public static final DeferredItem<BlockItem> EXAMPLE_BLOCK_ITEM = ITEMS.registerSimpleBlockItem("example_block", SCPBlocks.EXAMPLE_BLOCK);

    public static final DeferredItem<Item> SCP_500 = ITEMS.registerItem("scp_500", SCP500::new);
}
