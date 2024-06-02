package net.azazelzero.erc.client.keybinds;

import com.mojang.blaze3d.platform.InputConstants;
import dev.latvian.mods.rhino.Context;
import dev.latvian.mods.rhino.ScriptableObject;
import net.azazelzero.erc.Main;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.ClientRegistry;
import org.lwjgl.glfw.GLFW;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class KeyInit {
    public static KeyMapping ercKeyOpenUI; //= new KeyMapping("key.erc.open", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_J,"key.categories.misc");
    private static final String KEYBIND_ERC_CATEGORY = "keybind.erc.category";

    public static void init(){
        ercKeyOpenUI = registerKey("open", KEYBIND_ERC_CATEGORY, InputConstants.KEY_J);
    }
    private static KeyMapping registerKey(String name, String category, int keycode){
        final var key = new KeyMapping("key." + Main.MODID + "." + name, keycode, category);
        ClientRegistry.registerKeyBinding(key);
        return key;
    }
}
