package net.azazelzero.derp.client.gui;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.azazelzero.derp.client.event.ClientForgeEvents;
import net.azazelzero.derp.client.gui.popups.PanelHandler;
import net.azazelzero.derp.client.gui.utils.*;
import net.azazelzero.derp.client.gui.utils.widgets.DynamicScreen;
import net.azazelzero.derp.client.gui.popups.AddDerp;
import net.azazelzero.derp.client.net.ClientMessage;
import net.azazelzero.derp.core.derp.*;
import net.azazelzero.derp.core.derp.requirements.DatRequirement;
import net.azazelzero.derp.core.event.ModForgeEvents;
import net.azazelzero.derp.core.net.MapMessageUpload;
import net.azazelzero.derp.core.net.Packet;
import net.azazelzero.derp.core.net.PacketHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Widget;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraftforge.client.gui.GuiUtils;
import org.jetbrains.annotations.NotNull;

import java.util.*;


public class SkillTreeEditor extends Screen implements DynamicScreen {

    private static final ResourceLocation TABS_TEXTURE = new ResourceLocation("textures/gui/advancements/tabs.png");
    private static final ResourceLocation WINDOW_TEXTURE = new ResourceLocation("derp","ui/skillsmenu.png");
    public static final ResourceLocation EDITOR_TEXTURE = new ResourceLocation("derp","ui/editor.png");
    private static final ResourceLocation BLUEPAIN = new ResourceLocation("textures/block/crying_obsidian.png");
//    private static final ResourceLocation BLUEPAIN = new ResourceLocation("textures/block/netherrack.png");
//    private static final ResourceLocation BLUEPAIN = new ResourceLocation("textures/block/stone_bricks.png");
    private static final ResourceLocation WIDGETS_TEXTURE = new ResourceLocation("textures/gui/advancements/widgets.png");
    private int evolution=0;

    private List<GuiEventListener> children = Lists.newArrayList();
    public List<Integer> variableCarryover;
    private PopUpMenuManager popUpMenuManager;
    public PoseStack Matricies;
    private int derpIndex;
    private static final Component TITLE = new TranslatableComponent("gui.skilltree");

    public SkillTreeEditor(List<Integer> sa, PoseStack mat, PopUpMenuManager popUpMenuManager){

        super(new TranslatableComponent("gui.skilltree"));
        variableCarryover=sa;
        this.popUpMenuManager=popUpMenuManager;
        derpIndex=variableCarryover.get(0);
        evolution=variableCarryover.get(1);
        Matricies=mat;
        ModForgeEvents.derpsLoadedList.clear();
        ModForgeEvents.derpsLoaded.forEach((k,v)->{
            v.forEach((key,value)->{
                ModForgeEvents.derpsLoadedList.add(value);
            });
        });
    }
    public DERP derp(){
        return ModForgeEvents.derpsLoadedList.get(variableCarryover.get(0));
    }

    @Override
    public void init(){
        java.util.function.Consumer<GuiEventListener> add = (b) -> {
            if (b instanceof Widget w)
                this.renderables.add(w);
            children.add(b);
        };

        for (DAT[] dats : derp().DATs[variableCarryover.get(2)]) {
            for (DAT dat : dats) {
                if (dat==null)continue;;
                for (DatRequirement requirement : dat.Requirements) {
                    if (requirement==null)continue;;
                    requirement.init();
                }
            }
        }
        int screenX = (this.width - 132) / 2;
        int screenY = (this.height - 185) / 2;
        int counter=0;
        popUpMenuManager.add(new PanelHandler(0,screenX+132,screenY,21,160,popUpMenuManager,this,this));
        this.addWidget(new Button((screenX-31),(screenY-12), 18, 18, new TextComponent("ssddfd"), (b) -> {
            popUpMenuManager.add(new AddDerp(0,1,1,133,171,this,this,popUpMenuManager));
// add a derp
            variableCarryover.set(10,1);
//            variableCarryoverFields.clear();
//            variableCarryoverFields.add(new Field("Add Derp",""));
//            variableCarryoverFields.add(new Field("Derp Name",""));
//            List<String> options = new ArrayList<>();
//            SkillActionRegistry.SKILL_ACTIONS.getEntries().forEach((a)-> options.add(a.get().getRegistryName().toString()));
//            variableCarryoverFields.add(new Field("Add Requirement",options));
//            variableCarryoverFields.add(new Field("Test Field",""));
        }));
    }


    public boolean mouseScrolled(double a, double b, double c) {
        if(variableCarryover.get(3)==1)variableCarryover.set(4,variableCarryover.get(4)+((int)c*6));
        if(variableCarryover.get(3)==0&&!(variableCarryover.get(9)+(int)c>ModForgeEvents.derpsLoadedList.size()-7||variableCarryover.get(9)+(int)c<0))variableCarryover.set(9,variableCarryover.get(9)+(int)c);
        variableCarryover.set(11,variableCarryover.get(11)+(int) c);
//        if (variableCarryover.get(9)+(int)c>ModForgeEvents.derpsLoadedList.size()||variableCarryover.get(9)+(int)c<0) variableCarryover.set(9,variableCarryover.get(9)+(int)(c*-1));

        return false;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int p_94697_) {
        variableCarryover.set(11,1);
        System.out.println(p_94697_);
        if (variableCarryover.get(10)!=0) return super.mouseClicked(mouseX, mouseY, p_94697_);
        int i = (this.width - 132) / 2;
        int j = (this.height - 185) / 2;
        DAT[][] panel =derp().DATs[variableCarryover.get(2)];
        for (int x = 0; x < panel.length; x++) {
            for (int y = 0; y < panel[x].length; y++) {
                if (panel[x][y]==null)continue;
                SkillClicked(i+11+(x*23),j+23+(y*23),panel[x][y],mouseX,mouseY);
            }
        }
        if (mouseX<i-3) scrollBarClicked(mouseX,mouseY);

        int SlottableSlotX= (int) Math.floor((mouseX-(i+9))/22);
        if (SlottableSlotX<0||SlottableSlotX>4)return super.mouseClicked(mouseX, mouseY, p_94697_);
        if (mouseY>j+175) {
            DAT dat = new DAT();
            dat.Icon="nulled";
            dat.Slottable=true;
            if (ClientForgeEvents.SlottedSkills[SlottableSlotX]==null)return super.mouseClicked(mouseX, mouseY, p_94697_);
            dat.Id=ClientForgeEvents.SlottedSkills[SlottableSlotX].Id;
            dat.SlottableEvent=ClientForgeEvents.SlottedSkills[SlottableSlotX].SlottableEvent;
            System.out.println("wow"+dat.Slottable);
            ClientForgeEvents.SlottedSkills[SlottableSlotX] = null;
            PacketHandler.INSTANCE.sendToServer(new ClientMessage(new Packet(dat, derp().Layer, derp().name, Minecraft.getInstance().player.getName().getString(),  SlottableSlotX)));
        }
        return super.mouseClicked(mouseX, mouseY, p_94697_);
    }

    @Override
    public boolean mouseReleased(double p_94722_, double p_94723_, int p_94724_) {
        variableCarryover.set(11,0);
        return super.mouseReleased(p_94722_, p_94723_, p_94724_);
    }

    public void renderSkillDrag(PoseStack matrices, int x, int y, DAT dat) {
        if(dat==null) return;

        Float[] clr=derp().SkillUnlockedColor;
        if(!dat.unlocked) clr=derp().SkillLockedColor;

        float clrAdd=0;
        for (Float v : clr) {
            clrAdd=clrAdd+v;
        }
        clrAdd=clrAdd/4;
        clr[3]=1f;
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
                0, 0,clr
        );
        RenderSystem.setShaderColor(clrAdd,clrAdd, clrAdd,1f);
        RenderSystem.setShaderTexture(0,new ResourceLocation(dat.Icon));
        blit(matrices,x+1,y+1, 0,0f,0f,16,16,16,16);
    }

    private void scrollBarClicked(double mouseX, double mouseY){
        int j = (this.height - 185) / 2;
        int yClicked= (int) Math.floor((mouseY-(j+13))/18);
        System.out.println(yClicked);
        if (yClicked>7 || yClicked<0) return;
        if (yClicked+variableCarryover.get(9)>ModForgeEvents.derpsLoadedList.size()-1) return;
        variableCarryover.set(0,yClicked+variableCarryover.get(9));
        variableCarryover.set(1,0);
        variableCarryover.set(2,0);

    }


    private void SkillClicked(int x, int y, DAT dat, double mouseX, double mouseY) {
        boolean clicked = isMouseInBetween(x - 1, x + 20, y - 1, y + 18,(int) mouseX,(int) mouseY);
        if (dat.unlocked) return;
        if (clicked){
            Packet skill=new Packet(
                    Minecraft.getInstance().player.getName().getString(),
                    new DERPData.DATPostion(dat.Panel, dat.X, dat.Y),
                    derp().name,
                    derp().Layer,
                    variableCarryover.get(1)
            );

            PacketHandler.INSTANCE.sendToServer(new ClientMessage(skill));
        }
    }

    private boolean isMouseInBetween(int minX, int maxX, int minY, int maxY, int mouseX, int mouseY){
        return mouseX > minX && mouseX < maxX && mouseY > minY && mouseY < maxY;
    }
    public void render(PoseStack matrices, int mouseX, int mouseY, float delta) {
        Matricies=matrices;
        int i = (this.width - 132) / 2;
        int j = (this.height - 185) / 2;
        this.renderBackground(matrices);
        boolean canScrollDerpsUI = isMouseInBetween(i - 28, i - 2, j + 9, j + 151 + 9, mouseX, mouseY);
        renderInside(matrices,i,j);
        RenderSystem.setShaderColor(derp().Color[0],derp().Color[1],derp().Color[2],derp().Color[3]);
        renderWindow(matrices,i,j);
        RenderSystem.setShaderColor(derp().Color[0],derp().Color[1],derp().Color[2],derp().Color[3]);

        drawTabs(matrices,i,j);
        renderDerpScrollBar(matrices,i,j);
        renderSkills(matrices,i,j);
        if (variableCarryover.get(10)>=1){
            variableCarryover.set(7,0);
            popUpMenuManager.render(this,mouseX,mouseY,delta,variableCarryover.get(11));
            return;
        }
        if(variableCarryover.get(7)==1) renderSkillsHover(matrices,i,j,mouseX,mouseY);
        if(variableCarryover.get(7)==0) renderSkillDrag(matrices,mouseX-9,mouseY-9,derp().DATs[variableCarryover.get(2)][variableCarryover.get(5)][variableCarryover.get(6)]);
        variableCarryover.set(8,variableCarryover.get(8)+1);
        if (variableCarryover.get(8)>40)variableCarryover.set(8,0);
    }







    public void renderWindow(PoseStack matrices, int i, int j) {
//        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.enableBlend();
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, WINDOW_TEXTURE);
        this.blit(matrices,i,j, 0, 0, 133, 171);

        this.font.draw(matrices, TITLE, (float)(i+8), (float)(j+7), 4210752);

    }

    public void renderDerpScrollBar(PoseStack matrices, int i, int j){
        drawContinuousTexturedBox(
                matrices,
                WINDOW_TEXTURE,
                i-31,
                j-12,
                161,
                18, 8,
                12, 256,
                256, 18,
                0, 18,
                0, 0
        );

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

        for (int index = 0; index < 7; index++) {
            int val=variableCarryover.get(9)+index;
            if (val>ModForgeEvents.derpsLoadedList.size()-1||val<0) continue;
            DERP derp=ModForgeEvents.derpsLoadedList.get(val);
            if (derp==null) derp= new DERP();
            int x=i-26;
            int y=j+13+(18*index);
            Float[] clr=derp.Color;

            float clrAdd=0;
            for (Float v : clr) {
                clrAdd=clrAdd+v;
            }
            clrAdd=clrAdd/4;
            clr[3]=1f;
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
                    0, 0,clr
            );
            RenderSystem.setShaderColor(clrAdd,clrAdd, clrAdd,1f);
            RenderSystem.setShaderTexture(0,new ResourceLocation(derp.Icon));
            blit(matrices,x+1,y+1, 0,0f,0f,16,16,16,16);
        }
    }
    public void renderSkillsHover(PoseStack matrices, int width, int height,int mouseX,int mouseY){
        DAT[][] panel =derp().DATs[variableCarryover.get(2)];
        boolean scrollLock = false;
        //testing for hovering over menu
        int x=variableCarryover.get(5);
        int y=variableCarryover.get(6);
        if(x!=-1&&x<5&&y>-1&&y<6) scrollLock=renderSkillHover(matrices,width+11+(x*23),height+23+(y*23),panel[x][y],mouseX,mouseY);

        x=(mouseX-(width+11))/23;
        y=(mouseY-(height+23))/23;
        if(!scrollLock&&(x>-1&&x<5&&y>-1&&y<6)) scrollLock=renderSkillHover(matrices,width+11+(x*23),height+23+(y*23),panel[x][y],mouseX,mouseY);




        if(scrollLock)variableCarryover.set(3,1);
        if(!scrollLock) {
            variableCarryover.set(3,0);
            variableCarryover.set(4,10);
            variableCarryover.set(5,-1);
    }
    }

    private boolean renderSkillHover(PoseStack matrices, int x, int y, DAT dat, int mouseX, int mouseY) {
        if(dat==null)return false;
        boolean hover=false;
        boolean hoveringMenu=false;
        int X=dat.X;
        int Y=dat.Y;
        RenderSystem.setShaderColor(1f,1f,1f,1f);
        hover=isMouseInBetween(x-1,x+20,y-1,y+18,mouseX,mouseY);
        if(!hover&&variableCarryover.get(5)==X) {
            hover = isMouseInBetween(x + 18, x + 18 + 82, y, y + 71, mouseX, mouseY);
            hoveringMenu=true;
        }
        if(hover){

            if (!hoveringMenu)variableCarryover.set(5,X);
            if (!hoveringMenu)variableCarryover.set(6,Y);
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
            List<FormattedCharSequence> Lines= font.split(FormattedText.of(Desc),150);
            List<FormattedCharSequence> requirements= new ArrayList<>();

            requirements.add(quick(""));
            requirements.add(quick("Requirements:"));
            for (DatRequirement requirement : dat.Requirements) {
                String[] linesToWrite=requirement.DescriptionString(Minecraft.getInstance().player.getName().getString());
                for (String s : linesToWrite) {
                    if (s==null) continue;
                    if(s.length()>26) s=s.substring(0,26);
                    requirements.add(quick(s));
                }
            }
//            System.out.println(dat);
            matrices.scale(0.5f,0.5f,0.5f);

            int yCounter=variableCarryover.get(4);
            int LineSize= (Lines.size()+requirements.size())*8;
            if(LineSize+yCounter<112&&LineSize>120)yCounter=((LineSize-112)*-1);
            if(yCounter>10)yCounter=10;
            if (LineSize<120)yCounter=10;
            variableCarryover.set(4,yCounter);

            int color=SelectDerp.colorToInt(derp().FontColor[0].intValue(), derp().FontColor[1].intValue(), derp().FontColor[2].intValue());
            for (FormattedCharSequence line : Lines) {
                if(Objects.equals(line.toString(), "null")) continue;;
                if (yCounter>=0&&yCounter<=120){
                    this.font.draw(matrices,line,(float)(x+18+6)*2,(float)(y*2)+yCounter+10,color);

                }
                yCounter+=8;
            }

            for (FormattedCharSequence line : requirements) {
                if(Objects.equals(line.toString(), "null")) continue;;
                if (yCounter>=0&&yCounter<=120){
                    this.font.draw(matrices,line,(float)(x+18+6)*2,(float)(y*2)+yCounter+10,color);

                }
                yCounter+=8;
            }

            matrices.scale(2f,2f,2f);
            RenderSystem.setShaderTexture(0,WINDOW_TEXTURE);
            RenderSystem.setShaderColor(1f,1f,1f,1f);
            RenderSystem.enableBlend();
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            matrices.scale(1f,1f,8f);

//            blit(matrices,x+18,y,9,0.6835f,0.7226f,84,12,256,256);
            drawContinuousTexturedBox(
                    matrices,
                    WINDOW_TEXTURE,
                    x+18,
                    y,
                    174,
                    185, 8,
                    12, 256,
                    256, 10,
                    0, 82,
                    0, 100
            );
            drawContinuousTexturedBox(
                    matrices,
                    WINDOW_TEXTURE,
                    x+18,
                    y+62,
                    174,
                    247, 8,
                    12, 256,
                    256, 9,
                    0, 82,
                    0, 100
            );
            matrices.scale(1f,1f,0.125f);

        }
        return hover;
    }
    private FormattedCharSequence quick(String txt){
        return Language.getInstance().getVisualOrder(FormattedText.of(txt));
    }

    public void renderSkills(PoseStack matrices, int width, int height){
        DAT[][] panel =derp().DATs[variableCarryover.get(2)];
        for (DAT[] dats : panel) {
            for (DAT dat : dats) {
                renderSkill(matrices,width+11,height+23,dat);
            }
        }
    }

    private void renderSkill(PoseStack matrices, int x, int y, DAT dat) {
        if(dat==null) return;

        x=x+(dat.X*23);
        y=y+(dat.Y*23);
        Float[] clr=derp().SkillUnlockedColor;
        if(!dat.unlocked) clr=derp().SkillLockedColor;
        if (dat.beingDragged) clr[3]=0.5f;
        if (!dat.beingDragged) clr[3]=1f;
        RenderSystem.setShaderColor(clr[0],clr[1],clr[2],clr[3]);

        float clrAdd=0;
        for (Float v : clr) {
            clrAdd=clrAdd+v;
        }
        clrAdd=clrAdd/4;

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
        RenderSystem.setShaderColor(clrAdd,clrAdd, clrAdd,1f);
        RenderSystem.setShaderTexture(0,new ResourceLocation(dat.Icon));
        blit(matrices,x+1,y+1, 0,0f,0f,16,16,16,16);

        if (!dat.Slottable)return;
        clr=derp().SkillUnlockedColor;

        if(dat.unlocked) clr=derp().SkillLockedColor;
        if (dat.beingDragged) clr[3]=0.5f;
        if (!dat.beingDragged) clr[3]=1f;
        RenderSystem.setShaderColor(clr[0],clr[1],clr[2],clr[3]);

        if (variableCarryover.get(8)>20) phase0(matrices,x,y);
        if (variableCarryover.get(8)>20) return;
        drawContinuousTexturedBox(
                matrices,
                WINDOW_TEXTURE,
                x,
                y,
                179,
                18, 8,
                12, 256,
                256, 18,
                0, 18,
                0, 0
        );

    }
    public void phase0(PoseStack matrices,int x,int y){
        drawContinuousTexturedBox(
                matrices,
                WINDOW_TEXTURE,
                x,
                y,
                179,
                0, 8,
                12, 256,
                256, 18,
                0, 18,
                0, 0
        );
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
        int counter=0;

        Integer[] visible= new Integer[8];
        //Side Tabs
        for (int i = 0; i < derp().Panels.length; i++) {
            drawContinuousTexturedBox(
                    matrices,
                    WINDOW_TEXTURE,
                    screenX+132,
                    screenY+((i-counter)*20),
                    28,
                    221, 8,
                    12, 256,
                    256, 22,
                    0, 20,
                    0, 0

            );

        }
        int xOffset=28;
        if (variableCarryover.get(2)==0) xOffset=0;
        drawContinuousTexturedBox(
                matrices,
                WINDOW_TEXTURE,
                screenX+129,
                screenY+((variableCarryover.get(2)-counter)*20),
                xOffset,
                199, 8,
                12, 256,
                256, 22,
                0, 28,
                0, 0

        );
    }
    public static void drawContinuousTexturedBox(PoseStack poseStack, ResourceLocation resource, int x, int y, int u, int v, int width, int height, int textureWidth, int textureHeight,
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
    public static void drawContinuousTexturedBox(PoseStack poseStack,ResourceLocation resource, int x, int y, int u, int v, int width, int height, int textureWidth, int textureHeight,
                                           int topBorder, int bottomBorder, int leftBorder, int rightBorder, float zLevel, Float[] clr)
    {
//        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderColor(clr[0],clr[1],clr[2],clr[3]);
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

    @Override
    public void AddWidget(AbstractWidget widget) {
        addWidget(widget);
    }
    @Override
    public <T extends GuiEventListener & NarratableEntry> T addWidget(T p_96625_){
        System.out.println("adding child"+children.isEmpty());
        if (!children.isEmpty())children.add(0,p_96625_);
        else children.add(p_96625_);
        return p_96625_;
    }
    @Override
    protected void removeWidget(GuiEventListener p_169412_) {
        if (p_169412_ instanceof Widget) {
            this.renderables.remove((Widget)p_169412_);
        }

        this.children.remove(p_169412_);
    }
    @Override
    protected void clearWidgets() {
        this.children.clear();
    }

    @Override
    public @NotNull List<? extends GuiEventListener> children() {
        return this.children;
    }

    @Override
    public void RemoveWidget(AbstractWidget widget) {
        this.removeWidget(widget);
    }

    @Override
    public Screen screen() {
        return this;
    }
    @Override
    public Font font(){
        return this.font;
    }

    public void onClose() {
        PacketHandler.INSTANCE.sendToServer(new MapMessageUpload(new Packet(ModForgeEvents.derpsLoaded, Minecraft.getInstance().player.getName().getString())));
        super.onClose();
    }

}
