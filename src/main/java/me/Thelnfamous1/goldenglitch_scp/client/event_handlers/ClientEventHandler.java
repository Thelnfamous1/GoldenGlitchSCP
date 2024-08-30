package me.Thelnfamous1.goldenglitch_scp.client.event_handlers;

import me.Thelnfamous1.goldenglitch_scp.SCPMod;
import me.Thelnfamous1.goldenglitch_scp.client.renderers.scp_173.SCP173Renderer;
import me.Thelnfamous1.goldenglitch_scp.core.SCPEntities;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

public class ClientEventHandler {
    @EventBusSubscriber(modid = SCPMod.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class Mod {
        @SubscribeEvent
        public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event){
            event.registerEntityRenderer(SCPEntities.SCP_173.get(), SCP173Renderer::new);
        }
    }
    /*
    @EventBusSubscriber(modid = SCPMod.MODID, bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
    public static class Game {
    }
     */

}
