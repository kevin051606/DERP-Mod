package net.azazelzero.derp.client.gui.popups;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.azazelzero.derp.client.gui.utils.PopMenu;
import net.azazelzero.derp.client.gui.utils.PopUpMenuManager;
import net.azazelzero.derp.client.gui.utils.widgets.DynamicScreen;
import net.azazelzero.derp.client.gui.utils.widgets.PopUpButton;
import net.azazelzero.derp.client.gui.utils.widgets.PopUpEditBox;
import net.azazelzero.derp.client.gui.utils.widgets.PopUpWidget;
import net.azazelzero.derp.core.derp.DERP;
import net.azazelzero.derp.core.event.ModForgeEvents;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;

import java.util.ArrayList;
import java.util.List;

import static net.azazelzero.derp.client.gui.SkillTreeEditor.EDITOR_TEXTURE;

public class AddDerp extends PopMenu {
    public List<PopUpWidget> widgets;
    public List<EditBox> boxes= new ArrayList<>();
    public AddDerp(int zLevel, int x, int y, int width, int height, Screen screen, DynamicScreen screens, PopUpMenuManager manager) {
        super(zLevel, x,  y,  width, height,manager,screens,true) ;
        widgets=new ArrayList<>();
        PopUpEditBox editBox = new PopUpEditBox(screen.getMinecraft().font,1,1, 103,9, this, new int[]{5,12});
        editBox.setMaxLength(200);
        widgets.add(editBox);
        screens.AddWidget(editBox);
        PopUpEditBox editBox2 = new PopUpEditBox(screen.getMinecraft().font,1,11, 103,9, this,new int[]{5,32});
        editBox2.setMaxLength(200);
        widgets.add(editBox2);
        PopUpButton close = new PopUpButton(0,0,16,16,this, new int[]{5,90}, b->{
            this.closeAndPop();
        });
        widgets.add(close);
        boxes.add(editBox);
        boxes.add(editBox2);
        screens.AddWidget(close);
        screens.AddWidget(editBox);
        screens.AddWidget(editBox2);
    }
    @Override
    public void render(DynamicScreen screen, PoseStack a, int mouseX, int mouseY, float delta, int button) {
        RenderSystem.setShaderTexture(0, EDITOR_TEXTURE);
        screen.screen().blit(a,x,y, 0, 0, 133, 171);
        screen.font().draw(a,"ID",5+x,2+y, 0);
        screen.font().draw(a,"Description",5+x,22+y, 0);
        for (PopUpWidget widget : widgets) {
            widget.returnAsWidget().x=widget.relativePosition()[0]+x;
            widget.returnAsWidget().y=widget.relativePosition()[1]+y;
            widget.returnAsWidget().render(a,mouseX,mouseY,delta);
        }

    }

    @Override
    public List<PopUpWidget> currentWidgets() {
        return widgets;
    }

    @Override
    public void closeAndPop() {
        DERP filler= new DERP();
        filler.name=boxes.get(0).getValue();
        filler.Description=boxes.get(1).getValue();
        ModForgeEvents.derpsLoadedList.add(filler);
        super.closeAndPop();
    }
}
