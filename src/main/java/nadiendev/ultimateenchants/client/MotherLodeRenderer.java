package nadiendev.ultimateenchants.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;

import java.util.Set;

public final class MotherLodeRenderer {
    private MotherLodeRenderer() {}

    // Dorado, para diferenciarse del rojo que suele usarse para lava/peligro
    private static final float RED = 1.0f;
    private static final float GREEN = 0.84f;
    private static final float BLUE = 0.0f;
    private static final float ALPHA = 1.0f;


    public static void onRenderLevel(RenderLevelStageEvent.AfterTranslucentBlocks event) {
        Set<BlockPos> current = MotherLodeHighlight.highlighted;
        if (current.isEmpty()) {
            return;
        }

    
        Vec3 camPos = Minecraft.getInstance().gameRenderer.getMainCamera().position();

        PoseStack poseStack = event.getPoseStack();
        poseStack.pushPose();
        poseStack.mulPose(event.getModelViewMatrix());
        poseStack.translate(-camPos.x(), -camPos.y(), -camPos.z());

        MultiBufferSource.BufferSource bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();
        VertexConsumer buffer = bufferSource.getBuffer(RenderTypes.lines());

        final float size = 1.0f;
        for (BlockPos pos : current) {
            drawBoxLines(buffer, poseStack, pos.getX(), pos.getY(), pos.getZ(), size);
        }

        bufferSource.endBatch(RenderTypes.lines());
        poseStack.popPose();
    }

    private static void drawBoxLines(VertexConsumer buffer, PoseStack poseStack, int x, int y, int z, float size) {
        var pose = poseStack.last();

        // TOP
        line(buffer, pose, x, y + size, z, x + size, y + size, z);
        line(buffer, pose, x + size, y + size, z, x + size, y + size, z + size);
        line(buffer, pose, x + size, y + size, z + size, x, y + size, z + size);
        line(buffer, pose, x, y + size, z + size, x, y + size, z);

        // BOTTOM
        line(buffer, pose, x + size, y, z, x + size, y, z + size);
        line(buffer, pose, x + size, y, z + size, x, y, z + size);
        line(buffer, pose, x, y, z + size, x, y, z);
        line(buffer, pose, x, y, z, x + size, y, z);

        // Aristas verticales
        line(buffer, pose, x + size, y, z + size, x + size, y + size, z + size);
        line(buffer, pose, x + size, y, z, x + size, y + size, z);
        line(buffer, pose, x, y, z + size, x, y + size, z + size);
        line(buffer, pose, x, y, z, x, y + size, z);
    }

    private static void line(VertexConsumer buffer, PoseStack.Pose pose, float x1, float y1, float z1, float x2, float y2, float z2) {
        float nx = x2 - x1, ny = y2 - y1, nz = z2 - z1;
        buffer.addVertex(pose, x1, y1, z1).setColor(RED, GREEN, BLUE, ALPHA).setNormal(pose, nx, ny, nz);
        buffer.addVertex(pose, x2, y2, z2).setColor(RED, GREEN, BLUE, ALPHA).setNormal(pose, nx, ny, nz);
    }
}