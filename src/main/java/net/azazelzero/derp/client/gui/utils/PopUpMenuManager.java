package net.azazelzero.derp.client.gui.utils;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.azazelzero.derp.client.gui.utils.widgets.DynamicScreen;
import net.azazelzero.derp.client.gui.utils.widgets.PopUpWidget;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.screens.Screen;

import java.util.ArrayList;
import java.util.List;

public class PopUpMenuManager {
    // this is implemented to make the editor more clean, and effective
    // this code has functions to add menu by taking in pop menu object and rendering
    // custom widgets that take layer into account and  will also be moveable with the menu
    private boolean buttonToggle =false;
    private PopMenu drag = null;
    private int[] dragOrigin = new int[2];
    public List<PopMenu> PopUpMenus;
    public PopUpMenuManager(){
        PopUpMenus=new ArrayList<>();
    }

    public void render(DynamicScreen screen, int x, int y, float delta, int button){
        PoseStack matrices = new PoseStack();
        for (PopMenu popUpMenu : PopUpMenus) {
            RenderSystem.setShaderColor(1f,1f,1f,1f);
            popUpMenu.render(screen,matrices,x,y,delta,button);
        }
        if (button==0) {
            buttonToggle = true;
            drag=null;
        }
        if (drag!=null) drag(x,y);
        if (button>0&&buttonToggle){
            PopUpWidget popUpWidget=null;
            for (PopMenu popUpMenu : PopUpMenus) {
                if (!(x>popUpMenu.x&&popUpMenu.x+popUpMenu.width>x&&y>popUpMenu.y&&popUpMenu.y+popUpMenu.height>y)) continue;
                drag=popUpMenu;
                for (PopUpWidget currentWidget : popUpMenu.currentWidgets()) {
                    AbstractWidget widget= currentWidget.returnAsWidget();
                    widget.x=currentWidget.relativePosition()[0]+popUpMenu.x;
                    widget.y=currentWidget.relativePosition()[1]+popUpMenu.y;
                    if (x>widget.x&&widget.x+widget.getWidth()>x&&y>widget.y&&widget.y+widget.getHeight()>y) popUpWidget=currentWidget;
                }

            }
            if (popUpWidget!=null) {
                drag = null;
            } else if(drag!=null) {
                dragOrigin[0] = x-drag.x;
                dragOrigin[1] = y-drag.y;
            }
            buttonToggle=false;
        }
    }

    private void drag(int x, int y) {
        drag.move(
                x-dragOrigin[0],
                y-dragOrigin[1]
        );
    }

    public void add(PopMenu popMenu){
        popMenu.zLevel=PopUpMenus.size();
        PopUpMenus.add(popMenu);
    }
    public void remove(PopMenu menu){
        PopUpMenus.remove(menu);
        List<PopMenu> sorted = new ArrayList<>();
        int counter=0;
        for (PopMenu popUpMenu : PopUpMenus) {
            popUpMenu.zLevel=counter;
            sorted.add(counter,popUpMenu);
            counter++;
        }
        PopUpMenus.clear();
        PopUpMenus=sorted;
    }

}
