package net.azazelzero.derp.client.gui.utils.widgets;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.screens.Screen;

public interface DynamicScreen {
    public void AddWidget(AbstractWidget widget);
    public void RemoveWidget(AbstractWidget widget);
    public Font font();
    public Screen screen();
}
