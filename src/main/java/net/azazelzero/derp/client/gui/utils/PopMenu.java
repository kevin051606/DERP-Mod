package net.azazelzero.derp.client.gui.utils;

import com.mojang.blaze3d.vertex.PoseStack;
import net.azazelzero.derp.client.gui.utils.widgets.DynamicScreen;
import net.azazelzero.derp.client.gui.utils.widgets.PopUpWidget;
import net.minecraft.client.gui.screens.Screen;

import java.util.List;

public abstract class PopMenu {
    public int zLevel;
    public int x;
    public int y;
    public final int width;
    public final int height;
    public final PopUpMenuManager manager;
    public final DynamicScreen screen;
    public final boolean draggable;

    public PopMenu(int zLevel, int x, int y, int width, int height, PopUpMenuManager manager, DynamicScreen screen, boolean draggable) {
        this.zLevel = zLevel;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.manager = manager;
        this.screen = screen;
        this.draggable = draggable;
    }

    public abstract void render(DynamicScreen screen, PoseStack a, int mouseX, int mouseY, float delta, int button);
    public abstract List<PopUpWidget> currentWidgets();
    public void move(int x, int y){
        if (!draggable) return;
        this.x=x;
        this.y=y;
    }

    public void closeAndPop(){
        for (PopUpWidget currentWidget : currentWidgets()) {
            screen.RemoveWidget(currentWidget.returnAsWidget());
        }
        manager.remove(this);

    }

}
