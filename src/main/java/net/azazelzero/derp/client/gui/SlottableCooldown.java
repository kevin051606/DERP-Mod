package net.azazelzero.derp.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.azazelzero.derp.client.event.ClientForgeEvents;
import net.azazelzero.derp.core.derp.DAT;
import net.azazelzero.derp.core.event.ModForgeEvents;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.gui.ForgeIngameGui;
import net.minecraftforge.client.gui.GuiUtils;
import net.minecraftforge.client.gui.IIngameOverlay;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

import static net.minecraft.client.gui.GuiComponent.blit;


public class SlottableCooldown implements IIngameOverlay {
    private static final ResourceLocation WINDOW_TEXTURE = new ResourceLocation("derp","ui/skillsmenu.png");
    @Override
    public void render(ForgeIngameGui gui, PoseStack poseStack, float partialTick, int width, int height) {
        RenderSystem.setShaderColor(1f,1f,1f,1f);
            RenderSystem.setShaderColor(1f,1f,1f,1f);
            GuiUtils.drawContinuousTexturedBox(
                    poseStack,
                    WINDOW_TEXTURE,
                    width-120,
                    height-29,
                    0,
                    171, 8,
                    12, 256,
                    256, 28,
                    0, 116,
                    0, 0
            );
            for (int i = 0; i < ClientForgeEvents.SlottedSkills.length; i++) {
                if (i>4) continue;
                DAT slot=ClientForgeEvents.SlottedSkills[i];
                if (slot==null)continue;
                renderSkillDrag(poseStack,(width-115)+(i*22),height-24,slot);
            }
    }
    public void renderSkillDrag(PoseStack matrices, int x, int y, DAT dat) {
        if(dat==null) return;

        Float[] clr;
        if (dat.Info!=null){
            clr=ModForgeEvents.derpsLoaded.get(dat.Info.LayerID).get(dat.Info.DerpID).Color;
        } else clr= new Float[]{1f, 1f, 1f, 1f};


        float clrAdd=0;
        for (Float v : clr) {
            clrAdd=clrAdd+v;
        }
        clrAdd=clrAdd/4;
        clr[3]=1f;

        AtomicInteger pixels= new AtomicInteger();
        ClientForgeEvents.Slots.forEach((s,i)->{
            float steps= 18f /(float) s.CoolDown;
            if (Objects.equals(s.SkillPosition.SkillID, dat.Id)) pixels.set(18-(int) (steps*i.get()));
        });

        SkillTree.drawContinuousTexturedBox(
                matrices,
                WINDOW_TEXTURE,
                x,
                y+6,
                161,
                18, 8,
                12, 256,
                256, 0,
                pixels.get(), 18,
                0, 0f,clr
        );
//        System.out.println(pixels.get());
        RenderSystem.setShaderColor(clrAdd,clrAdd, clrAdd,1f);
        RenderSystem.setShaderTexture(0,new ResourceLocation(dat.Icon));
        blit(matrices,x+1,y+1, 0,0f,0f,pixels.get(),16,16,16);
    }
}
