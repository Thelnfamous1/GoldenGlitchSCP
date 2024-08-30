package me.Thelnfamous1.goldenglitch_scp.client.renderers.scp_173;

import me.Thelnfamous1.goldenglitch_scp.core.SCPEntities;
import me.Thelnfamous1.goldenglitch_scp.entities.SCP173;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class SCP173Renderer<T extends SCP173> extends GeoEntityRenderer<T> {
    public SCP173Renderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new SCP173Model<>());
        this.withScale(SCPEntities.SCP_173_SCALE);
    }
}
