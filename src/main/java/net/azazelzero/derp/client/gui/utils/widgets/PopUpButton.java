package net.azazelzero.derp.client.gui.utils.widgets;

import net.azazelzero.derp.client.gui.utils.PopMenu;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.TextComponent;

public class PopUpButton extends Button implements PopUpWidget {
    public final PopMenu popMenu;
    public final int[] relativePosition;
    public PopUpButton(int p_93721_, int p_93722_, int p_93723_, int p_93724_, PopMenu popMenu, int[] relativePosition, OnPress p_93726_) {
        super(p_93721_, p_93722_, p_93723_, p_93724_, new TextComponent(""), p_93726_);
        this.popMenu = popMenu;
        this.relativePosition = relativePosition;
    }

    @Override
    public PopMenu PopUpMenu() {
        System.out.println("button "+popMenu.zLevel);
        return popMenu;
    }
    public void onClick(double x, double y){
        if (otherWidgetClickedOnHigherZ(popMenu.manager,x,y)) return;
        super.onClick(x,y);
    }

    @Override
    public AbstractWidget returnAsWidget() {
        return this;
    }
    @Override
    public int[] relativePosition() {
        if (relativePosition.length<2)  return new int[]{0,0};
        return relativePosition;
    }
}
