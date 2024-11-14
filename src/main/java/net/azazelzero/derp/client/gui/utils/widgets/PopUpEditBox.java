package net.azazelzero.derp.client.gui.utils.widgets;

import net.azazelzero.derp.client.gui.utils.PopMenu;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.TextComponent;

public class PopUpEditBox extends EditBox implements PopUpWidget {
    public final PopMenu popMenu;
    public final int[] relativePosition;
    public PopUpEditBox(Font p_94114_, int p_94115_, int p_94116_, int p_94117_, int p_94118_, PopMenu popMenu, int[] relativePosition) {
        super(p_94114_, p_94115_, p_94116_, p_94117_, p_94118_, new TextComponent("a"));
        this.popMenu = popMenu;
        this.relativePosition = relativePosition;
    }

    public boolean mouseClicked(double x, double y, int b){
        if (otherWidgetClickedOnHigherZ(popMenu.manager,x,y)) return false;
        return super.mouseClicked(x,y,b);
    }

    @Override
    public PopMenu PopUpMenu() {
        return popMenu;
    }

    @Override
    public EditBox returnAsWidget() {
        return this;
    }


    @Override
    public int[] relativePosition() {
        if (relativePosition.length<2)  return new int[]{0,0};
        return relativePosition;
    }
}
