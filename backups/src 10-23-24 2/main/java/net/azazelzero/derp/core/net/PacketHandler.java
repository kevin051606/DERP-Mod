package net.azazelzero.derp.core.net;

import net.azazelzero.derp.Main;
import net.azazelzero.derp.client.net.ClientMessage;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public final class PacketHandler {
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(Main.MODID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    private PacketHandler(){}
    public static void init(){
        int index =0;
        System.out.println("aac");
        INSTANCE.messageBuilder(net.azazelzero.derp.core.net.Message.class, index++, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(net.azazelzero.derp.core.net.Message::encode)
                .decoder(net.azazelzero.derp.core.net.Message::new)
                .consumer(Message::handle)
                .add();
        System.out.println("adsdda");


        INSTANCE.messageBuilder(ClientMessage.class, index++, NetworkDirection.PLAY_TO_SERVER)
                .encoder(ClientMessage::encode)
                .decoder(ClientMessage::new)
                .consumer(ClientMessage::handle)
                .add();



        INSTANCE.messageBuilder(MapMessage.class, index++, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(MapMessage::encode)
                .decoder(MapMessage::new)
                .consumer(MapMessage::handle)
                .add();

    }
}
