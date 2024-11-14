package net.azazelzero.derp.client.gui.popups;

import com.mojang.blaze3d.vertex.PoseStack;
import net.azazelzero.derp.client.gui.utils.PopMenu;
import net.azazelzero.derp.client.gui.utils.PopUpMenuManager;
import net.azazelzero.derp.client.gui.utils.widgets.DynamicScreen;
import net.azazelzero.derp.client.gui.utils.widgets.PopUpWidget;
import net.minecraft.client.gui.screens.Screen;

import java.util.List;

public class PanelRequirements extends PopMenu {
    public PanelRequirements(int zLevel, int x, int y, int width, int height, PopUpMenuManager manager, DynamicScreen screen, boolean draggable) {
        super(zLevel, x, y, width, height, manager, screen, draggable);
    }

    @Override
    public void render(DynamicScreen screen, PoseStack a, int mouseX, int mouseY, float delta, int button) {

    }

    @Override
    public List<PopUpWidget> currentWidgets() {
        return List.of();
    }
}
