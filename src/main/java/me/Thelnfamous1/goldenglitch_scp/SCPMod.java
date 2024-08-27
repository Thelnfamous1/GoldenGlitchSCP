package me.Thelnfamous1.goldenglitch_scp;

import me.Thelnfamous1.goldenglitch_scp.core.SCPBlocks;
import me.Thelnfamous1.goldenglitch_scp.core.SCPCreativeModeTabs;
import me.Thelnfamous1.goldenglitch_scp.core.SCPItems;
import me.Thelnfamous1.goldenglitch_scp.items.SCP500;
import net.neoforged.neoforge.common.EffectCure;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(SCPMod.MODID)
public class SCPMod
{
    // Define mod id in a common place for everything to reference
    public static final String MODID = "goldenglitch_scp";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();
    private static EffectCure SCP_500_CURE;

    // The constructor for the mod class is the first code that is run when your mod is loaded.
    // FML will recognize some parameter types like IEventBus or ModContainer and pass them in automatically.
    public SCPMod(IEventBus modEventBus, ModContainer modContainer)
    {
        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        // Register the Deferred Register to the mod event bus so blocks get registered
        SCPBlocks.BLOCKS.register(modEventBus);
        // Register the Deferred Register to the mod event bus so items get registered
        SCPItems.ITEMS.register(modEventBus);
        // Register the Deferred Register to the mod event bus so tabs get registered
        SCPCreativeModeTabs.CREATIVE_MODE_TABS.register(modEventBus);

        // Register ourselves for server and other game events we are interested in.
        // Note that this is necessary if and only if we want *this* class (ExampleMod) to respond directly to events.
        // Do not add this line if there are no @SubscribeEvent-annotated functions in this class, like onServerStarting() below.
        NeoForge.EVENT_BUS.register(this);

        // Register the item to a creative tab
        modEventBus.addListener(SCPCreativeModeTabs::addCreative);

        // Register our mod's ModConfigSpec so that FML can create and load the config file for us
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    public static EffectCure getScp500Cure() {
        if(SCP_500_CURE == null){
            throw new IllegalStateException("SCP-500 Cure was accessed too early!");
        }
        return SCP_500_CURE;
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
        SCP_500_CURE = EffectCure.get("scp_500");
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
    }

    @SubscribeEvent
    public void onEffectAdded(MobEffectEvent.Added event)
    {
        if(event.getEffectInstance() != null && SCP500.isEffectCuredBySCP500(event.getEffectInstance())){
            event.getEffectInstance().getCures().add(getScp500Cure());
        }
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @EventBusSubscriber(modid = MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {

        }
    }
}
