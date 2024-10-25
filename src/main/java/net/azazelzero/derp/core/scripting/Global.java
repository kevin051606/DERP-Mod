package net.azazelzero.derp.core.scripting;

import net.azazelzero.derp.Main;
import net.azazelzero.derp.core.net.Message;
import net.azazelzero.derp.core.net.Packet;
import net.azazelzero.derp.core.net.PacketHandler;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.server.ServerLifecycleHooks;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.luaj.vm2.LuaBoolean;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.TwoArgFunction;


public class Global{


    public Global(){}

    public static LuaValue getTable(){
        LuaTable table = new LuaTable();
        table.set("log", new log());
        table.set("tellPlayer", new tellPlayer());
        return table;
    }


    public void printToChat(){

    }

    static class tellPlayer extends TwoArgFunction{
        @Override
        public LuaValue call(LuaValue player,LuaValue txt){
//            if(!txt.isstring()) return LuaBoolean.FALSE;
//                PacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayerByName(player.toString())),new Message(new Packet(txt.toString())));
                    PacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayerByName(player.toString())),new Message(new Packet(txt.toString())));

            return LuaBoolean.TRUE;
        }
    }

    static class log extends OneArgFunction{
        @Override
        public LuaValue call(LuaValue txt){
//            if(!txt.isstring()) return LuaBoolean.FALSE;
            Logger logger = LogManager.getLogger(Main.MODID);
            logger.info(txt.tostring());
            return LuaBoolean.TRUE;
        }
    }

    public boolean foo(){
        return true;
    }

}
