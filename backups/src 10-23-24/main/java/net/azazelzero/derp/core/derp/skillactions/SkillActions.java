package net.azazelzero.derp.core.derp.skillactions;

import com.google.gson.JsonObject;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.azazelzero.derp.Main;
import net.minecraft.commands.Commands;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.server.ServerLifecycleHooks;

public class SkillActions {
    public static final RegistryObject<net.azazelzero.derp.core.derp.skillactions.SkillActionEntry>  command = SkillActionRegistry.SKILL_ACTIONS.register("command",command::new);
    public static class command extends SkillActionEntry {

        @Override
        public void action(String playerName) {
            Commands command=new Commands(Commands.CommandSelection.ALL);
            try {
                String finalCommand = Parameters.get("command").getAsString();
                finalCommand = finalCommand.replaceAll("\\{playername}",playerName);
                System.out.println(finalCommand);
                command.getDispatcher().execute(finalCommand,ServerLifecycleHooks.getCurrentServer().createCommandSourceStack());
            } catch (CommandSyntaxException e) {
                Main.LOGGER.error(e.getMessage());
            }

        }
    }
}
