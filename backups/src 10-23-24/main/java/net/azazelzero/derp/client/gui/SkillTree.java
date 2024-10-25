package net.azazelzero.derp.client.gui;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.azazelzero.derp.client.event.ClientForgeEvents;
import net.azazelzero.derp.core.derp.DAT;
import net.azazelzero.derp.core.derp.DERP;
import net.azazelzero.derp.client.keybinds.KeyInit;
import net.azazelzero.derp.core.event.ModForgeEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraftforge.client.gui.GuiUtils;

import java.util.List;


public class SkillTree extends Screen {

    private static final ResourceLocation TABS_TEXTURE = new ResourceLocation("textures/gui/advancements/tabs.png");
    private static final ResourceLocation WINDOW_TEXTURE = new ResourceLocation("derp","ui/skillsmenu.png");
    private static final ResourceLocation BLUEPAIN = new ResourceLocation("textures/block/crying_obsidian.png");
//    private static final ResourceLocation BLUEPAIN = new ResourceLocation("textures/block/netherrack.png");
//    private static final ResourceLocation BLUEPAIN = new ResourceLocation("textures/block/stone_bricks.png");
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

    private boolean canScrollDerpsUI =true;
    private int ScrollDerpsUI = 0;
    private boolean editingMode;
    private String derpSelected;
    private int evolution=0;
    private DERP derp;
    private String Panel;

    private List<Integer> variableCarryover;
    private int derpIndex;
    private static final Component TITLE = new TranslatableComponent("gui.skilltree");

    public SkillTree(List<Integer> sa){

        super(new TranslatableComponent("gui.skilltree"));
        variableCarryover=sa;
        derpIndex=variableCarryover.get(0);
        evolution=variableCarryover.get(1);
    }
    public DERP derp(){
        return ClientForgeEvents.ClientPlayerData.get(derpIndex)[evolution];
    }

    public void init(Minecraft objectQuestion){
        objectQuestion.getItemRenderer();

    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (KeyInit.ercKeyOpenUI.matches(keyCode, scanCode)) {
            onClose();
            return true;

        }

        return super.keyPressed(keyCode, scanCode, modifiers);
    }
    public boolean mouseScrolled(double a, double b, double c) {
//        System.out.println("a:"+a);
//        System.out.println("b:"+b);
//        System.out.println("c:"+c);
        System.out.println("scroll:"+ScrollDerpsUI+"  condition:"+canScrollDerpsUI);

        if (canScrollDerpsUI){ ScrollDerpsUI=ScrollDerpsUI + (int) c;}
        variableCarryover.set(4,variableCarryover.get(4)+(int)c);


        return false;
    }
    private boolean isMouseInBetween(int minX,  int maxX, int minY, int maxY, int mouseX, int mouseY){
        return mouseX > minX && mouseX < maxX && mouseY > minY && mouseY < maxY;
    }
    public void render(PoseStack matrices, int mouseX, int mouseY, float delta) {
        int i = (this.width - 132) / 2;
        int j = (this.height - 185) / 2;
        this.renderBackground(matrices);
        canScrollDerpsUI = isMouseInBetween(i-28, i-2, j+9, j+151+9,mouseX,mouseY);

        renderInside(matrices,i,j);
        RenderSystem.setShaderColor(derp().Color[0],derp().Color[1],derp().Color[2],derp().Color[3]);
        renderWindow(matrices,i,j);
        RenderSystem.setShaderColor(derp().Color[0],derp().Color[1],derp().Color[2],derp().Color[3]);

        drawTabs(matrices,i,j);
        renderDerpScrollBar(matrices,i,j);
        renderSkills(matrices,i,j);
        renderSkillsHover(matrices,i,j,mouseX,mouseY);
    }
    public void renderWindow(PoseStack matrices, int i, int j) {
//        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.enableBlend();
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, WINDOW_TEXTURE);
        this.blit(matrices,i,j, 0, 0, 132, 170);

        this.font.draw(matrices, TITLE, (float)(i+8), (float)(j+7), 4210752);

    }

    public void renderDerpScrollBar(PoseStack matrices, int i, int j){
        drawContinuousTexturedBox(
                        matrices,
                        WINDOW_TEXTURE,
                        i-31,
                        j+8,
                        133,
                        0, 8,
                        12, 256,
                        256, 154,
                        0, 28,
                        0, 0
        );
    }
    public void renderSkillsHover(PoseStack matrices, int width, int height,int mouseX,int mouseY){
        DAT[][] panel =derp().DATs[variableCarryover.get(2)];
        boolean scrollLock = false;
        for (int x = 0; x < panel.length; x++) {
            for (int y = 0; y < panel[x].length; y++) {
                if(!scrollLock) scrollLock=renderSkillHover(matrices,width+14+(x*23),height+23+(y*23),panel[x][y],mouseX,mouseY);
            }
        }
        if(scrollLock)variableCarryover.set(3,1);
        if(!scrollLock) {
            variableCarryover.set(3,0);
            variableCarryover.set(4,0);
            variableCarryover.set(5,-1);
        }
    }

    private boolean renderSkillHover(PoseStack matrices, int x, int y, DAT dat, int mouseX, int mouseY) {
        if(dat==null)return false;
        boolean hover=false;
        hover=isMouseInBetween(x,x+18,y,y+18,mouseX,mouseY);
        if(!hover&&variableCarryover.get(5)==x) hover=isMouseInBetween(x+18,x+18+82,y+18,y+71,mouseX,mouseY);
        if(hover){
            System.out.println("ssssdsd");
            variableCarryover.set(5,x);
            drawContinuousTexturedBox(
                    matrices,
                    WINDOW_TEXTURE,
                    x+18,
                    y,
                    174,
                    185, 8,
                    12, 256,
                    256, 71,
                    0, 82,
                    0, 0
            );
            String Desc=dat.Description;
            List<FormattedCharSequence> Lines= font.split(FormattedText.of(Desc),70);
            matrices.scale(0.5f,0.5f,0.5f);
            int yCounter=variableCarryover.get(4);
            int color=SelectDerp.colorToInt(derp().FontColor[0].intValue(), derp().FontColor[1].intValue(), derp().FontColor[2].intValue());
            for (FormattedCharSequence line : Lines) {
                if (yCounter>=8&&yCounter<=120){
                    drawString(matrices,this.font,line,(x+18+6)*2,(y*2)+yCounter+10,color);

                }
                yCounter+=8;
            }
            matrices.scale(2f,2f,2f);
            drawContinuousTexturedBox(
                    matrices,
                    WINDOW_TEXTURE,
                    x+18,
                    y,
                    174,
                    185, 8,
                    12, 256,
                    256, 11,
                    0, 82,
                    0, 0
            );
        }
        return hover;
    }

    public void renderSkills(PoseStack matrices, int width, int height){
        DAT[][] panel =derp().DATs[variableCarryover.get(2)];
        for (int x = 0; x < panel.length; x++) {
            for (int y = 0; y < panel[x].length; y++) {
                renderSkill(matrices,width+14+(x*23),height+23+(y*23),panel[x][y]);
            }
        }
    }

    private void renderSkill(PoseStack matrices, int x, int y, DAT dat) {
        if(dat==null)return;;
        if(!dat.unlocked) RenderSystem.setShaderColor(0.5f,0.5f,0.5f,1f);
        drawContinuousTexturedBox(
                matrices,
                WINDOW_TEXTURE,
                x,
                y,
                161,
                0, 8,
                12, 256,
                256, 18,
                0, 18,
                0, 0
        );
        RenderSystem.setShaderTexture(0,new ResourceLocation(dat.Icon));
        blit(matrices,x+1,y+1, 0,0f,0f,16,16,16,16);
    }

    public void renderInside(PoseStack matrices, int i, int j) {
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.enableBlend();
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, new ResourceLocation(derp().Texture));
        int j1;
        for(int i1 = 1; i1 <= 6; ++i1) {
            for(j1 = 0; j1 <= 8; ++j1) {
                blit(matrices, i + 3 + 16 * i1, j + 17 + 16 * j1, 0.0F, 0.0F, 16, 16, 16, 16);
            }
        }

        int i1 = 0;

        for(j1 = 0; j1 <= 8; ++j1) {
            blit(matrices, i + 9, j + 17 + 16 * j1, 0, 6.0F, 0.0F, 10, 16, 16, 16);
        }
        i1 = 7;

        for(j1 = 0; j1 <= 8; ++j1) {
            blit(matrices, i + 3 + 16 * i1, j + 17 + 16 * j1, 0, 0.0F, 0.0F, 9, 16, 16, 16);
        }
    }

    private void drawTabs(PoseStack matrices, int screenX, int screenY) {
//        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.colorMask(true, true, true, true);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        RenderSystem.disableDepthTest();
        RenderSystem.setShaderTexture(0, TABS_TEXTURE);
        drawContinuousTexturedBox(
                matrices,
                TABS_TEXTURE,
                screenX,
                screenY-22,
                0,
                4, 8,
                12, 256,
                256, 22,
                0, 28,
                0, 0

        );
        for (int i = 1; i < ModForgeEvents.derpsLoaded.size(); i++) {
            drawContinuousTexturedBox(
                    matrices,
                    TABS_TEXTURE,
                    screenX + (28 * i),
                    screenY - 21,
                    0,
                    4, 8,
                    12, 256,
                    256, 21,
                    0, 28,
                    0, 0

            );
        }

        //Side Tabs
        for (int i = 0; i < ModForgeEvents.derpsLoaded.size(); i++) {
            drawContinuousTexturedBox(
                    matrices,
                    WINDOW_TEXTURE,
                    screenX+132,
                    screenY+(i*20),
                    28,
                    221, 8,
                    12, 256,
                    256, 22,
                    0, 20,
                    0, 0

            );

        }

    }
    private void drawContinuousTexturedBox(PoseStack poseStack,ResourceLocation resource, int x, int y, int u, int v, int width, int height, int textureWidth, int textureHeight,
                                           int topBorder, int bottomBorder, int leftBorder, int rightBorder, float zLevel)
    {
//        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0,resource);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        int fillerWidth = textureWidth - leftBorder - rightBorder;
        int fillerHeight = textureHeight - topBorder - bottomBorder;
        int canvasWidth = width - leftBorder - rightBorder;
        int canvasHeight = height - topBorder - bottomBorder;
        int xPasses = canvasWidth / fillerWidth;
        int remainderWidth = canvasWidth % fillerWidth;
        int yPasses = canvasHeight / fillerHeight;
        int remainderHeight = canvasHeight % fillerHeight;

        // Draw Border
        // Top Left
        GuiUtils.drawTexturedModalRect(poseStack, x, y, u, v, leftBorder, topBorder, zLevel);
        // Top Right
        GuiUtils.drawTexturedModalRect(poseStack, x + leftBorder + canvasWidth, y, u + leftBorder + fillerWidth, v, rightBorder, topBorder, zLevel);
        // Bottom Left
        GuiUtils.drawTexturedModalRect(poseStack, x, y + topBorder + canvasHeight, u, v + topBorder + fillerHeight, leftBorder, bottomBorder, zLevel);
        // Bottom Right
        GuiUtils.drawTexturedModalRect(poseStack, x + leftBorder + canvasWidth, y + topBorder + canvasHeight, u + leftBorder + fillerWidth, v + topBorder + fillerHeight, rightBorder, bottomBorder, zLevel);

        for (int i = 0; i < xPasses + (remainderWidth > 0 ? 1 : 0); i++) {
            // Top Border
            GuiUtils.drawTexturedModalRect(poseStack, x + leftBorder + (i * fillerWidth), y, u + leftBorder, v, (i == xPasses ? remainderWidth : fillerWidth), topBorder, zLevel);
            // Bottom Border
            GuiUtils.drawTexturedModalRect(poseStack, x + leftBorder + (i * fillerWidth), y + topBorder + canvasHeight, u + leftBorder, v + topBorder + fillerHeight, (i == xPasses ? remainderWidth : fillerWidth), bottomBorder, zLevel);

            // Throw in some filler for good measure
            for (int j = 0; j < yPasses + (remainderHeight > 0 ? 1 : 0); j++)
                GuiUtils.drawTexturedModalRect(poseStack, x + leftBorder + (i * fillerWidth), y + topBorder + (j * fillerHeight), u + leftBorder, v + topBorder, (i == xPasses ? remainderWidth : fillerWidth), (j == yPasses ? remainderHeight : fillerHeight), zLevel);
        }
    }

}
