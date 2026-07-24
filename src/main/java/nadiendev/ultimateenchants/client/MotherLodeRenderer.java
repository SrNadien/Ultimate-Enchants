package nadiendev.ultimateenchants.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import org.lwjgl.opengl.GL11;

import java.util.Set;


public final class MotherLodeRenderer {
    private MotherLodeRenderer() {}

    // Dorado, para diferenciarse del rojo que suele usarse para lava/peligro
    private static final float RED = 1.0f;
    private static final float GREEN = 0.84f;
    private static final float BLUE = 0.0f;
    private static final float ALPHA = 1.0f;

    private static VertexBuffer vertexBuffer;
    private static Set<BlockPos> lastBuilt = null;

    public static void onRenderLevel(RenderLevelStageEvent event) {
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_TRANSLUCENT_BLOCKS) {
            return;
        }

        Set<BlockPos> current = MotherLodeHighlight.highlighted;
        if (current.isEmpty()) {
            return;
        }

        if (current != lastBuilt) {
            rebuild(current);
            lastBuilt = current;
        }

        if (vertexBuffer == null) {
            return;
        }

        Vec3 camPos = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();

        RenderSystem.depthMask(false);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        PoseStack poseStack = event.getPoseStack();
        poseStack.pushPose();

        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        RenderSystem.applyModelViewMatrix();
        RenderSystem.depthFunc(GL11.GL_ALWAYS);

        poseStack.mulPose(event.getModelViewMatrix());
        poseStack.translate(-camPos.x(), -camPos.y(), -camPos.z());

        vertexBuffer.bind();
        vertexBuffer.drawWithShader(poseStack.last().pose(), event.getProjectionMatrix(), RenderSystem.getShader());
        VertexBuffer.unbind();
        RenderSystem.depthFunc(GL11.GL_LEQUAL);

        poseStack.popPose();
        RenderSystem.applyModelViewMatrix();
    }

    private static void rebuild(Set<BlockPos> positions) {
        if (vertexBuffer == null) {
            vertexBuffer = new VertexBuffer(VertexBuffer.Usage.STATIC);
        }

        Tesselator tessellator = Tesselator.getInstance();
        BufferBuilder buffer = tessellator.begin(VertexFormat.Mode.DEBUG_LINES, DefaultVertexFormat.POSITION_COLOR);

        final float size = 1.0f;

        for (BlockPos pos : positions) {
            final int x = pos.getX(), y = pos.getY(), z = pos.getZ();

            // TOP
            buffer.addVertex(x, y + size, z).setColor(RED, GREEN, BLUE, ALPHA);
            buffer.addVertex(x + size, y + size, z).setColor(RED, GREEN, BLUE, ALPHA);
            buffer.addVertex(x + size, y + size, z).setColor(RED, GREEN, BLUE, ALPHA);
            buffer.addVertex(x + size, y + size, z + size).setColor(RED, GREEN, BLUE, ALPHA);
            buffer.addVertex(x + size, y + size, z + size).setColor(RED, GREEN, BLUE, ALPHA);
            buffer.addVertex(x, y + size, z + size).setColor(RED, GREEN, BLUE, ALPHA);
            buffer.addVertex(x, y + size, z + size).setColor(RED, GREEN, BLUE, ALPHA);
            buffer.addVertex(x, y + size, z).setColor(RED, GREEN, BLUE, ALPHA);

            // BOTTOM
            buffer.addVertex(x + size, y, z).setColor(RED, GREEN, BLUE, ALPHA);
            buffer.addVertex(x + size, y, z + size).setColor(RED, GREEN, BLUE, ALPHA);
            buffer.addVertex(x + size, y, z + size).setColor(RED, GREEN, BLUE, ALPHA);
            buffer.addVertex(x, y, z + size).setColor(RED, GREEN, BLUE, ALPHA);
            buffer.addVertex(x, y, z + size).setColor(RED, GREEN, BLUE, ALPHA);
            buffer.addVertex(x, y, z).setColor(RED, GREEN, BLUE, ALPHA);
            buffer.addVertex(x, y, z).setColor(RED, GREEN, BLUE, ALPHA);
            buffer.addVertex(x + size, y, z).setColor(RED, GREEN, BLUE, ALPHA);

            // Aristas verticales
            buffer.addVertex(x + size, y, z + size).setColor(RED, GREEN, BLUE, ALPHA);
            buffer.addVertex(x + size, y + size, z + size).setColor(RED, GREEN, BLUE, ALPHA);

            buffer.addVertex(x + size, y, z).setColor(RED, GREEN, BLUE, ALPHA);
            buffer.addVertex(x + size, y + size, z).setColor(RED, GREEN, BLUE, ALPHA);

            buffer.addVertex(x, y, z + size).setColor(RED, GREEN, BLUE, ALPHA);
            buffer.addVertex(x, y + size, z + size).setColor(RED, GREEN, BLUE, ALPHA);

            buffer.addVertex(x, y, z).setColor(RED, GREEN, BLUE, ALPHA);
            buffer.addVertex(x, y + size, z).setColor(RED, GREEN, BLUE, ALPHA);
        }

        MeshData build = buffer.build();
        if (build == null) {
            return;
        }
        vertexBuffer.bind();
        vertexBuffer.upload(build);
        VertexBuffer.unbind();
    }
}