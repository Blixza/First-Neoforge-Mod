package net.blixza.mymod.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.blixza.mymod.MyMod;
import net.blixza.mymod.entity.custom.NeritantanEntity;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class NeritantanModel<T extends NeritantanEntity> extends HierarchicalModel<T> {
	public static final ModelLayerLocation LAYER_LOCATION =
			new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(MyMod.MOD_ID, "neritantan"), "main");

	private final ModelPart Body;
	private final ModelPart Head;

	public NeritantanModel(ModelPart root) {
		this.Body = root.getChild("Body");
		this.Head = this.Body.getChild("Head");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition Body = partdefinition.addOrReplaceChild("Body", CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, -3.0F, -3.0F, 6.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition Head = Body.addOrReplaceChild("Head", CubeListBuilder.create().texOffs(0, 13).addBox(-2.0F, -3.0F, -4.0F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(0, 17).addBox(1.0F, -4.0F, -4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(4, 17).addBox(-2.0F, -4.0F, -4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition FrontLegL = Body.addOrReplaceChild("FrontLegL", CubeListBuilder.create().texOffs(12, 13).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(1.5F, -1.0F, -2.5F));

		PartDefinition FrontLegR = Body.addOrReplaceChild("FrontLegR", CubeListBuilder.create().texOffs(12, 15).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.5F, -1.0F, -2.5F));

		PartDefinition BackLegL = Body.addOrReplaceChild("BackLegL", CubeListBuilder.create().texOffs(16, 13).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(1.5F, -1.0F, 2.5F));

		PartDefinition BackLegR = Body.addOrReplaceChild("BackLegR", CubeListBuilder.create().texOffs(16, 15).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.5F, -1.0F, 2.5F));

		PartDefinition Tail = Body.addOrReplaceChild("Tail", CubeListBuilder.create().texOffs(0, 8).addBox(-2.0F, 0.0F, 0.0F, 4.0F, 0.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -2.0F, 2.5F));

		return LayerDefinition.create(meshdefinition, 32, 32);
	}

	@Override
	public void setupAnim(NeritantanEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.root().getAllParts().forEach(ModelPart::resetPose);
		this.applyHeadRotation(netHeadYaw, headPitch);

		this.animateWalk(NeritantanAnimations.WALKING, limbSwing, limbSwingAmount, 2f, 2.5f);
		this.animate(entity.idleAnimationState, NeritantanAnimations.IDLE, ageInTicks, 1f);
	}

	private void applyHeadRotation(float headYaw, float headPitch) {
		headYaw = Mth.clamp(headYaw, -30f, 30f);
		headPitch = Mth.clamp(headPitch, -25f, 45);

		this.Head.yRot = headYaw * ((float)Math.PI / 180f);
		this.Head.xRot = headPitch *  ((float)Math.PI / 180f);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
		Body.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
	}

	@Override
	public ModelPart root() {
		return Body;
	}
}