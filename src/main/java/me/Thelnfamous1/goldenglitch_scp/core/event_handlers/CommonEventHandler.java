package me.Thelnfamous1.goldenglitch_scp.core.event_handlers;

import me.Thelnfamous1.goldenglitch_scp.SCPMod;
import me.Thelnfamous1.goldenglitch_scp.core.SCPEntities;
import me.Thelnfamous1.goldenglitch_scp.entities.SCP173;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;

public class CommonEventHandler {

    @EventBusSubscriber(modid = SCPMod.MODID, bus = EventBusSubscriber.Bus.MOD)
    public static class Mod {
        @SubscribeEvent
        public static void registerEntityAttributes(EntityAttributeCreationEvent event){
            event.put(SCPEntities.SCP_173.get(), SCP173.createAttributes().build());
        }
    }
    /*
    @EventBusSubscriber(modid = SCPMod.MODID, bus = EventBusSubscriber.Bus.GAME)
    public static class Game {
    }
     */
}
