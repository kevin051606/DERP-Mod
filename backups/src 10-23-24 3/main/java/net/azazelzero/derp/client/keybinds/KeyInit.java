package net.azazelzero.derp.client.keybinds;

import com.mojang.blaze3d.platform.InputConstants;

import net.azazelzero.derp.Main;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.ClientRegistry;

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
