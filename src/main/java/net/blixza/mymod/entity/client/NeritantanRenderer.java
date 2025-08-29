package net.blixza.mymod.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.blixza.mymod.MyMod;
import net.blixza.mymod.entity.custom.NeritantanEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;


public class NeritantanRenderer extends MobRenderer<NeritantanEntity, NeritantanModel<NeritantanEntity>> {
    public NeritantanRenderer(EntityRendererProvider.Context context) {
        super(context, new NeritantanModel<>(context.bakeLayer(NeritantanModel.LAYER_LOCATION)), 0.25f);
    }

    @Override
    public ResourceLocation getTextureLocation(NeritantanEntity entity) {
        return ResourceLocation.fromNamespaceAndPath(MyMod.MOD_ID, "textures/entity/neritantan/neritantan.png");
    }

    @Override
    public void render(NeritantanEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        if(entity.isBaby()) {
            poseStack.scale(0.45f, 0.45f, 0.45f);
        } else {
            poseStack.scale(1f, 1f, 1f);
        }

        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }
}