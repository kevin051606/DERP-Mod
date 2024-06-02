package net.azazelzero.erc.client.gui;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.azazelzero.erc.client.keybinds.KeyInit;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.advancements.AdvancementTab;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.WorldStem;
import net.minecraft.server.packs.resources.Resource;
import net.minecraftforge.client.gui.GuiUtils;

import java.util.Iterator;

public class SkillTree extends Screen {

    private static final ResourceLocation TABS_TEXTURE = new ResourceLocation("textures/gui/advancements/tabs.png");
    private static final ResourceLocation WINDOW_TEXTURE = new ResourceLocation("textures/gui/advancements/window.png");
    private static final ResourceLocation WIDGETS_TEXTURE = new ResourceLocation("textures/gui/advancements/widgets.png");
    private static final int TEXTURE_WIDTH = 256;
    private static final int TEXTURE_HEIGHT = 256;
    private static final int FRAME_WIDTH = 252;
    private static final int FRAME_HEIGHT = 140;
    private static final int FRAME_PADDING = 8;
    private static final int FRAME_CUT = 16;
    private static final int FRAME_EXPAND = 24;
    private static final int CONTENT_GROW = 32;
    private static final int TABS_HEIGHT = 28;

    private static final Component TITLE = new TranslatableComponent("gui.skilltree");

    public SkillTree(){

        super(new TranslatableComponent("gui.skilltree"));
    }

    public void init(){

    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (KeyInit.ercKeyOpenUI.matches(keyCode, scanCode)) {
            this.minecraft.setScreen((Screen)null);
            this.minecraft.mouseHandler.grabMouse();
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }
    public void render(PoseStack matrices, int mouseX, int mouseY, float delta) {
        int i = (this.width - 252) / 2;
        int j = (this.height - 140) / 2;
        this.renderBackground(matrices);

        renderWindow(matrices,mouseX,mouseY);
        drawTabs(matrices,i,j);
    }
    public void renderWindow(PoseStack matrices, int mouseX, int mouseY) {
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.enableBlend();
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, WINDOW_TEXTURE);
        this.blit(matrices,mouseX,mouseY, 0, 0, 252, 140);


        this.font.draw(matrices, TITLE, (float)(mouseX + 8), (float)(mouseY + 6), 4210752);

    }

//    private void renderInside(PoseStack p_97374_, int p_97375_, int p_97376_, int p_97377_, int p_97378_) {
//            PoseStack posestack = RenderSystem.getModelViewStack();
//            posestack.pushPose();
//            posestack.translate((double)(p_97377_ + 9), (double)(p_97378_ + 18), 0.0D);
//            RenderSystem.applyModelViewMatrix();
//            advancementtab.drawContents(p_97374_);
//            posestack.popPose();
//            RenderSystem.applyModelViewMatrix();
//            RenderSystem.depthFunc(515);
//    }

    private void drawTabs(PoseStack matrices, double mouseX, double mouseY) {
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.colorMask(true, true, true, true);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        RenderSystem.disableDepthTest();
        RenderSystem.setShaderTexture(0, TABS_TEXTURE);

                GuiUtils.drawContinuousTexturedBox(
                        matrices,
                        TABS_TEXTURE,
                        4,
                        4,
                        0,
                        33, 8,
                        12, 256,
                        256, 31,
                        0, 27,
                        0, 0

                );

//        var mouse = getMousePos(mouseX, mouseY);
//
//        for (var i = 0; i < categories.size(); i++) {
//            var category = categories.get(i);
//
//            drawIcon(
//                    matrices,
//                    category.getIcon(),
//                    1f,
//                    FRAME_PADDING + 32 * i + 6 + 8,
//                    FRAME_PADDING + 9 + 8
//            );
//
//            if (isInsideTab(mouse, i)) {
//                tooltip = textRenderer.wrapLines(category.getTitle(), LINE_WIDTH);
//            }
//        }
    }

}
