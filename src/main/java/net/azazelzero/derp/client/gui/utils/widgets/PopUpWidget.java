package net.azazelzero.derp.client.gui.utils.widgets;

import net.azazelzero.derp.client.gui.utils.PopMenu;
import net.azazelzero.derp.client.gui.utils.PopUpMenuManager;
import net.minecraft.client.gui.components.AbstractWidget;

public interface  PopUpWidget {
    public PopMenu PopUpMenu();
    public AbstractWidget returnAsWidget();
    public default boolean otherWidgetClickedOnHigherZ(PopUpMenuManager manager, double x, double y){
        int zLevel = PopUpMenu().zLevel;
        for (PopMenu popUpMenu : manager.PopUpMenus) {
            if (popUpMenu.zLevel<zLevel) continue;
            if (popUpMenu==PopUpMenu()) continue;
            System.out.println("z: "+zLevel);
            System.out.println("a "+popUpMenu);
            System.out.println("b "+PopUpMenu());
            for (PopUpWidget currentWidget : popUpMenu.currentWidgets()) {
                AbstractWidget widget = currentWidget.returnAsWidget();
                widget.x = currentWidget.relativePosition()[0] + popUpMenu.x;
                widget.y = currentWidget.relativePosition()[1] + popUpMenu.y;
                if (x > widget.x && widget.x + widget.getWidth() > x && y > widget.y && widget.y + widget.getHeight() > y)
                    return true;
            }
        }

        return false;
    }
    public default int[] relativePosition(){

        return new int[]{0,0};
    };
}
