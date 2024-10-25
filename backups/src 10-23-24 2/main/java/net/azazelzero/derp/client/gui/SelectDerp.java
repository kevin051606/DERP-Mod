package net.azazelzero.derp.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.azazelzero.derp.client.event.ClientForgeEvents;
import net.azazelzero.derp.core.derp.DERP;
import net.azazelzero.derp.client.keybinds.KeyInit;
import net.azazelzero.derp.core.event.ModForgeEvents;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Widget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.gui.GuiUtils;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;


public class SelectDerp extends Screen {

    private static final ResourceLocation TABS_TEXTURE = new ResourceLocation("textures/gui/advancements/tabs.png");
    private static final ResourceLocation WINDOW_TEXTURE = new ResourceLocation("derp","ui/skillsmenu.png");
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
    private final List<Integer> variableCarryOver;

    private boolean canScrollDerpsUI =true;
    private int ScrollDerpsUI = 0;
    private boolean editingMode;
    private String derpSelected;
    private net.azazelzero.derp.core.derp.DERP[] derp;
    private int derpIndex=0;
    private String layer;
    private int currentLayer;
    Float[] Colors;
    private String Panel;

    private static final Component TITLE = new TranslatableComponent("gui.derpselect");

    public SelectDerp(List<Integer> sa){

        super(new TranslatableComponent("gui.derpselect"));
//        currentLayer=0;
//        layer=ModForgeEvents.layers.get(currentLayer);
//        derp = new DERP[ModForgeEvents.derpsLoaded.get(layer).size()];
//        AtomicInteger counter= new AtomicInteger(0);
//        ModForgeEvents.derpsLoaded.get(layer).entrySet().forEach((v)->{
//            derp[counter.get()]=v.getValue();
//            counter.getAndIncrement();
//        });
//        System.out.println("here 2");
//        if(derp[derpIndex].Layer=="null") onClose();
//        System.out.println("here 3");

//        Colors=derp[derpIndex].Color;
        this.variableCarryOver = sa;
        this.derpIndex = (Integer)this.variableCarryOver.get(0);
        this.currentLayer = 0;
        this.layer = (String)ModForgeEvents.layers.get(this.currentLayer);
        System.out.println("SSSS");
        ModForgeEvents.layers.forEach((a) -> {
            System.out.println();
        });
        this.derp = new net.azazelzero.derp.core.derp.DERP[(ModForgeEvents.derpsLoaded.get(this.layer)).size()];
        AtomicInteger counter = new AtomicInteger(0);
        (ModForgeEvents.derpsLoaded.get(this.layer)).entrySet().forEach((v) -> {
            this.derp[counter.get()] = (DERP)v.getValue();
            counter.getAndIncrement();
        });
        System.out.println("here 2");
        if (this.derp[this.derpIndex].Layer == "null") {
            this.onClose();
        }

        System.out.println("here 3");
        this.Colors = this.derp[this.derpIndex].Color;

    }

    @Override
    public void init(){
        super.init();
        int i = (this.width - 252) / 2;
        int j = (this.height - 140) / 2;
        System.out.println("kill me");
        this.addWidget(new Button(125 ,103 ,13, 19, new TextComponent("ssddfd"), (b) -> {
            this.derpIndex = (Integer)this.variableCarryOver.get(0);
            this.variableCarryOver.set(0, (Integer)this.variableCarryOver.get(0) + 1);
            --this.derpIndex;
            if (this.derpIndex < 0) {
                this.derpIndex = (ModForgeEvents.derpsLoaded.get(ModForgeEvents.layers.get(this.currentLayer))).size() - 1;
            }

            this.derpIndex = this.derpIndex;
            this.variableCarryOver.set(0, this.derpIndex);
        }));
        this.addWidget(new Button(287 ,103, 13, 19, new TextComponent("ssddfd"), (b) -> {
            this.derpIndex = (Integer)this.variableCarryOver.get(0);
            this.variableCarryOver.set(0, (Integer)this.variableCarryOver.get(0) + 1);
            ++this.derpIndex;
            if (this.derpIndex >(ModForgeEvents.derpsLoaded.get(ModForgeEvents.layers.get(this.currentLayer))).size()-1) {
                this.derpIndex = 0;
            }

            this.variableCarryOver.set(0, this.derpIndex);
        }));
        this.addWidget(new Button(168 ,207, 257-168, 13, new TextComponent("ssddfd"), (b) -> {
            this.currentLayer=variableCarryOver.get(1);
            derpIndex=variableCarryOver.get(0);
            DERP[] newDerpSet=new DERP[3];
            newDerpSet[0]=derp[derpIndex];
            ClientForgeEvents.ClientPlayerData.add(newDerpSet);
            currentLayer++;
            if(ModForgeEvents.layers.size()-1<currentLayer) onClose();

            variableCarryOver.set(0,0);
            variableCarryOver.set(1,currentLayer);
        }));
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

        return false;
    }
    private boolean isMouseInBetween(int minX,  int maxX, int minY, int maxY, int mouseX, int mouseY){
        return mouseX > minX && mouseX < maxX && mouseY > minY && mouseY < maxY;
    }
    public boolean mouseClicked(double mouseX, double mouseY, int p_97345_) {
        if (p_97345_ == 0) {
            int i = (this.width - 252) / 2;
            int j = (this.height - 140) / 2;
            if(isMouseInBetween(i-23,i-23+14,(j+75),(j+75+21),(int) mouseX, (int) mouseY)){
                derpIndex=derpIndex-1;
                if(derpIndex<0)derpIndex=ModForgeEvents.derpsLoaded.size()-1;
            }
            if(isMouseInBetween((i+140),(i+140+14),(j+75),(j+75+21),(int)mouseX,(int)mouseY)){
                derpIndex=derpIndex+1;
                if(derpIndex>ModForgeEvents.derpsLoaded.size()-1)derpIndex=0;
            }
        }
        System.out.println("x:"+mouseX+" y:"+mouseY);
        return super.mouseClicked(mouseX, mouseY, p_97345_);
    }
    public void render(PoseStack matrices, int mouseX, int mouseY, float delta) {
        int i = (this.width - 132) / 2;
        int j = (this.height - 185) / 2;
        this.renderBackground(matrices);
        canScrollDerpsUI = isMouseInBetween(i-28, i-2, j+9, j+151+9,mouseX,mouseY);


        renderInside(matrices,i,j);
        renderText(matrices,i,j);
        RenderSystem.setShaderColor(Colors[0], Colors[1], Colors[2], Colors[3]);
        renderWindow(matrices,i,j);
        renderHover(matrices,i,j,mouseX,mouseY);
        for(Widget widget : this.renderables) {
            widget.render(matrices, mouseX, mouseY, delta);
        }
    }
    public static int colorToInt(int r, int g, int b) {
        int result = 0;
        result += r;
        result <<= 8;
        result += g;
        result <<= 8;
        result += b;
        return result;
    }
    public void renderText(PoseStack matrices, int i, int j){
        this.font.draw(matrices,derp[derpIndex].Description,i+16,j+30,4210752);
        drawString(matrices, this.font, this.derp[this.derpIndex].displayName, i + 68, j + 25, colorToInt(derp[derpIndex].FontColor[0].intValue(), derp[derpIndex].FontColor[1].intValue(), derp[derpIndex].FontColor[2].intValue()));
        FormattedText s = FormattedText.of(this.derp[this.derpIndex].Description);
        this.font.drawWordWrap(FormattedText.of(this.derp[this.derpIndex].Description), i + 19, j + 40, 100, colorToInt(derp[derpIndex].FontColor[0].intValue(), derp[derpIndex].FontColor[1].intValue(), derp[derpIndex].FontColor[2].intValue()));

    }
    public void renderWindow(PoseStack matrices, int i, int j) {
        RenderSystem.enableBlend();
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, WINDOW_TEXTURE);
        this.blit(matrices,i,j, 0, 0, 132, 170);

        this.font.draw(matrices, TITLE, (float)(i+8), (float)(j+7), 4210752);
        RenderSystem.enableBlend();
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(Colors[0], Colors[1], Colors[2], Colors[3]);
        matrices.scale(0.5f,0.5f,0.5f);
        drawContinuousTexturedBox(matrices,WINDOW_TEXTURE,(i+140)*2,(j+75)*2,163,58,8,12,256,256,42,0,27,0,0f);
        drawContinuousTexturedBox(matrices,WINDOW_TEXTURE,(i-23)*2,(j+75)*2,162,101,8,12,256,256,42,0,27,0,0f);
        matrices.scale(2f,2f,2f);
        drawContinuousTexturedBox(matrices,WINDOW_TEXTURE,i+22,j+180,0 ,243,8,12,256,256,13,0,87,0,0f);

    }
    public void renderHover(PoseStack matrices,int i, int j,int mouseX, int mouseY){
//        mouseX=mouseX/2;
//        mouseY=mouseY/2;
        matrices.scale(0.5f,0.5f,0.5f);
        if (isMouseInBetween(i-23,i-23+14,(j+75),(j+75+21),mouseX,mouseY)){
            drawContinuousTexturedBox(matrices,WINDOW_TEXTURE,(i-23)*2,(j+75)*2,162,143,8,12,256,256,42,0,27,0,0f);

        }
        if(isMouseInBetween((i+140),(i+140+14),(j+75),(j+75+21),mouseX,mouseY)){
            drawContinuousTexturedBox(matrices,WINDOW_TEXTURE,(i+140)*2,(j+75)*2,162,143,8,12,256,256,42,0,27,0,0f);

        }
        matrices.scale(2f,2f,2f);
        if(isMouseInBetween(i+22,i+87+22,j+180,j+193,mouseX,mouseY)){
            RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
            drawContinuousTexturedBox(matrices,WINDOW_TEXTURE,i+ 22,j+180,87 ,243,8,12,256,256,13,0,87,0,0f);
        }

    }


    public void renderInside(PoseStack matrices, int i, int j) {
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.enableBlend();
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, new ResourceLocation(derp[derpIndex].Texture));
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
