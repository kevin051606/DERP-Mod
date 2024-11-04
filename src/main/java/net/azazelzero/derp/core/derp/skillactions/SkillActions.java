package net.azazelzero.derp.core.derp.skillactions;

import com.google.gson.JsonObject;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.azazelzero.derp.Main;
import net.minecraft.commands.Commands;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.server.ServerLifecycleHooks;
import virtuoel.pehkui.Pehkui;
import virtuoel.pehkui.api.ScaleData;
import virtuoel.pehkui.api.ScaleOperations;
import virtuoel.pehkui.api.ScaleRegistries;
import virtuoel.pehkui.api.ScaleType;
import virtuoel.pehkui.command.argument.ScaleOperationArgumentType;
import virtuoel.pehkui.command.argument.ScaleTypeArgumentType;

public class SkillActions {
    public static final RegistryObject<SkillActionEntry>  command = SkillActionRegistry.SKILL_ACTIONS.register("command",() -> new SkillActionEntry(new SkillActions.command()));
    public static final RegistryObject<SkillActionEntry>  scale = SkillActionRegistry.SKILL_ACTIONS.register("scale",() -> new SkillActionEntry(new SkillActions.scale()));
    public static class command extends SkillAction {

        @Override
        public void action(String playerName) {
            JsonObject Parameters = getParameters();
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
        @Override
        public void onRemove(String playerName) {}
    }
    public static class scale extends SkillAction{

        @Override
        public void action(String playerName) {

            JsonObject Parameters = getParameters();
            final float scale = Parameters.get("value").getAsFloat();
            final ScaleType type = ScaleRegistries.SCALE_TYPES.get(new ResourceLocation(Pehkui.MOD_ID,Parameters.get("scale_type").getAsString()));
                final ScaleData data = type.getScaleData(ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayerByName(playerName));
                double value=ScaleRegistries.SCALE_OPERATIONS.get(new ResourceLocation("pehkui",Parameters.get("operation").getAsString())).applyAsDouble(data.getTargetScale(),scale);
                data.setTargetScale((float) value);
                data.setPersistence(true);
        }

        @Override
        public void onRemove(String playerName) {
            JsonObject Parameters = getParameters();
            final ScaleType type = ScaleRegistries.SCALE_TYPES.get(new ResourceLocation(Pehkui.MOD_ID,Parameters.get("scale_type").getAsString()));
            final ScaleData data = type.getScaleData(ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayerByName(playerName));
            data.resetScale();

        }
    }

    public static class Null extends SkillAction{
        @Override
        public void action(String playerName) {

        }

        @Override
        public void onRemove(String playerName) {

        }
    }
}
