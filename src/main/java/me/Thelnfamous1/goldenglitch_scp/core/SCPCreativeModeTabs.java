package me.Thelnfamous1.goldenglitch_scp.core;

import me.Thelnfamous1.goldenglitch_scp.SCPMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class SCPCreativeModeTabs {
    // Create a Deferred Register to hold CreativeModeTabs which will all be registered under the "examplemod" namespace
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, SCPMod.MODID);
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> SCP = CREATIVE_MODE_TABS.register("scp", () -> CreativeModeTab.builder()
            .title(Component.translatable(String.format("itemGroup.%s", SCPMod.MODID))) //The language key for the title of your CreativeModeTab
            .withTabsBefore(CreativeModeTabs.TOOLS_AND_UTILITIES)
            .icon(() -> SCPItems.SCP_500.get().getDefaultInstance())
            .displayItems((parameters, output) -> {
                // Add the items to the tab. For your own tabs, this method is preferred over the event
                output.accept(SCPItems.SCP_500.get());
                output.accept(SCPItems.SCP_173_SPAWN_EGG.get());
            }).build());

    // Add the example block item to the building blocks tab
    public static void addCreative(BuildCreativeModeTabContentsEvent event)
    {
        /*
        if (event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS)
            event.accept(SCPItems.EXAMPLE_BLOCK_ITEM);
         */
    }
}
