package net.azazelzero.erc.core.scripting;

import dev.latvian.mods.rhino.ScriptableObject;
import dev.latvian.mods.rhino.annotations.JSConstructor;
import dev.latvian.mods.rhino.annotations.JSFunction;
import dev.latvian.mods.rhino.annotations.JSGetter;

public class Storage extends ScriptableObject {

    public Storage() {}

    @JSConstructor
    public Storage(int a){

    }

    @JSFunction
    public void printToConsole(){
        System.out.println("Hello World from java");
    }


    @Override
    public String getClassName() {
        return "Storage";
    }
}
