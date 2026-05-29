package net.kyle.jukeboxplus.renderer;

import net.kyle.jukeboxplus.block.entity.JukeboxPlusBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.RotationAxis;

public class JukeboxPlusBlockEntityRenderer implements BlockEntityRenderer<JukeboxPlusBlockEntity> {

    public JukeboxPlusBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {}

    @Override
    public void render(JukeboxPlusBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        Direction facing = entity.getCachedState().get(Properties.HORIZONTAL_FACING);

        for (int i = 0; i < 9; i++) {
            ItemStack stack = entity.getStack(i);
            if (stack.isEmpty()) continue;

            matrices.push();
            matrices.translate(0.5, 19.0 / 16.0, 0.5);
            float yaw = facing.getAxis() == Direction.Axis.X
                    ? facing.asRotation()
                    : facing.asRotation() - 180;
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(yaw));
            matrices.translate(0, 0, 0.08);
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(270));
            matrices.translate(0, 0, (i + 3.0f) / 14.0f - 0.5f);
            matrices.scale(0.5f, 0.5f, 0.5f);

            MinecraftClient.getInstance().getItemRenderer().renderItem(
                    stack, ModelTransformationMode.FIXED,
                    light, overlay, matrices, vertexConsumers, entity.getWorld(), 0
            );

            matrices.pop();
        }
    }
}