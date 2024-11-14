package net.azazelzero.derp.client.gui.utils.widgets;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.TextComponent;

import java.util.List;

public class ScrollableWidget extends AbstractWidget{
    public int scroll;
    public final Font font;
    public final List<String> list;
    public final OnOptionCLicked optionCLicked;

    @Override
    public void updateNarration(NarrationElementOutput p_169152_) {

    }

    public interface OnOptionCLicked {
        void onOptionClicked(String OptionSelected);
    }

    public ScrollableWidget(int x, int y, int width, int height, Font font, List<String> list, OnOptionCLicked optionCLicked) {
        super(x,y,width,height,new TextComponent(""));
        this.font = font;
        this.list = list;
        this.optionCLicked = optionCLicked;
    }

    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float p_93660_) {
        for (int i = 0; i < height/9; i++) {
            int sin = i +scroll;
            if (sin<0||list.size()-1<sin||list.get(sin)==null) continue;
            String entry = list.get(sin);
            if (entry.length()>30)entry=entry.substring(0,width/4);
            font.draw(poseStack,entry,x,y+(i*9),4210752);
        }
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        super.onClick(mouseX, mouseY);
        int index=(int)Math.floor((mouseY-y)/9)+scroll;
        if (index>list.size()-1||index<0) return;
        optionCLicked.onOptionClicked(list.get(index));
    }
}
