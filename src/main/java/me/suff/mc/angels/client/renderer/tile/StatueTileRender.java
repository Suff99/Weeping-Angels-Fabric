package me.suff.mc.angels.client.renderer.tile;

import me.suff.mc.angels.client.models.WeepingAngelModel;
import me.suff.mc.angels.client.renderer.entiity.WeepingAngelRenderer;
import me.suff.mc.angels.common.block.StatueBlock;
import me.suff.mc.angels.common.blockentity.StatueTile;
import me.suff.mc.angels.enums.WeepingAngelPose;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.util.Identifier;

/* Created by Craig on 18/02/2021 */
public class StatueTileRender extends BlockEntityRenderer< StatueTile > {

    WeepingAngelModel weepingAngelModel = new WeepingAngelModel();

    public StatueTileRender(BlockEntityRenderDispatcher dispatcher) {
        super(dispatcher);
    }

    @Override
    public void render(StatueTile statueTile, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        matrices.push();
        matrices.translate(0.5F, 1.5F, 0.5F);
        matrices.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion(180F));
        BlockState blockstate = statueTile.getCachedState();
        float rotation = 22.5F * (float) blockstate.get(StatueBlock.ROTATION);
        matrices.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(rotation));
        WeepingAngelModel angel = weepingAngelModel;
        Identifier texture;

        WeepingAngelPose pose = statueTile.getAngelPose();
        if (angel != null) {
            angel.setWeepingAngelPose(pose);
            texture = WeepingAngelRenderer.generateTex(pose, statueTile.getAngelVariant());
            angel.setAngles(null, 0, 0, 0, 0, 0);
            angel.render(matrices, vertexConsumers.getBuffer(RenderLayer.getEntityCutout(texture)), light, OverlayTexture.DEFAULT_UV, 1, 1, 1, 1);
        }
        matrices.pop();
    }
}
