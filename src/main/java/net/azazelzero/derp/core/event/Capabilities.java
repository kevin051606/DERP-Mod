package net.azazelzero.derp.core.event;

import net.azazelzero.derp.Main;
import net.azazelzero.derp.core.data.DerpPlayerData;
import net.azazelzero.derp.core.data.DerpPlayerDataProvider;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class Capabilities {

    @SubscribeEvent
    public void onAttachCapabilitiesPlayer(AttachCapabilitiesEvent<Entity> event){
        if(event.getObject() instanceof Player)
        {
            if(!event.getObject().getCapability(DerpPlayerDataProvider.DERP_DATA).isPresent()) {
                event.addCapability(new ResourceLocation(Main.MODID, "properties"), new DerpPlayerDataProvider());
            }

        }
    }

    @SubscribeEvent
    public void clonedEvent(PlayerEvent.Clone event) {
        System.out.println("ran a");
        if (event.isWasDeath()) {
            System.out.println("ran b");
            Player player = event.getPlayer();
            player.reviveCaps();
//            event.
//            event.getPlayer().revive();
            player.getCapability(DerpPlayerDataProvider.DERP_DATA).ifPresent(newStore -> {
                System.out.println("ran c");
//                event.getOriginal().reviveCaps();
                event.getOriginal().revive();
                event.getOriginal().getCapability(DerpPlayerDataProvider.DERP_DATA).ifPresent(oldStore -> {

                    System.out.println("ran d");
                    newStore.copyFrom(oldStore);
                    System.out.println(newStore.derps.size());
                });
            });

        }
    }



}
