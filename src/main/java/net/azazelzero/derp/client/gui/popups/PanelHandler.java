package net.azazelzero.derp.client.gui.popups;

import com.mojang.blaze3d.vertex.PoseStack;
import net.azazelzero.derp.client.gui.SkillTreeEditor;
import net.azazelzero.derp.client.gui.utils.PopMenu;
import net.azazelzero.derp.client.gui.utils.PopUpMenuManager;
import net.azazelzero.derp.client.gui.utils.widgets.DynamicScreen;
import net.azazelzero.derp.client.gui.utils.widgets.PopUpButton;
import net.azazelzero.derp.client.gui.utils.widgets.PopUpWidget;
import net.minecraft.client.gui.screens.Screen;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class PanelHandler extends PopMenu {
    private List<PopUpWidget> widgets;
    public PanelHandler(int zLevel, int x, int y, int width, int height, PopUpMenuManager manager, DynamicScreen screen, SkillTreeEditor screenOG) {
        super(zLevel, x, y, width, height, manager, screen, false);
        int screenX = (screenOG.width - 132) / 2;
        int screenY = (screenOG.height - 185) / 2;
        widgets = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            AtomicInteger atomicI = new AtomicInteger(i);
            PopUpButton button = new PopUpButton(screenX+132,screenY+((i)*20),20,22,this, new int[]{0,0},(s)->{
                System.out.println(screenOG.derp().Panels.length);
                if (screenOG.derp().Panels.length<=atomicI.get()) return;

                screenOG.variableCarryover.set(2,atomicI.get());
            });

            screen.AddWidget(button);
        }

    }

    @Override
    public void render(DynamicScreen screen, PoseStack a, int mouseX, int mouseY, float delta, int button) {

    }

    @Override
    public List<PopUpWidget> currentWidgets() {
        return widgets;
    }
}
