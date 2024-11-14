package net.azazelzero.derp.client.gui.utils;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;

public class randpmScreenForTextComponents extends Screen {
    protected randpmScreenForTextComponents(Component p_96550_) {
        super(p_96550_);
    }
    public static TextComponent stringToTextComponent(String string){
        return new TextComponent(string);
    }
}
