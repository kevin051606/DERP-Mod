package net.azazelzero.derp.core.net;

import net.minecraft.network.FriendlyByteBuf;

import java.io.*;

public class SerializeUtil {

    public static byte[] serialize(Serializable value) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try (ObjectOutputStream outputStream = new ObjectOutputStream(out)) {
            outputStream.writeObject(value);
        }

        return out.toByteArray();
    }

    public static <T extends Serializable> T deserialize(byte[] data) throws IOException, ClassNotFoundException {
        try (ByteArrayInputStream bis = new ByteArrayInputStream(data)) {
            //noinspection unchecked
            return (T) new ObjectInputStream(bis).readObject();
        }
    }
    public static void serializeAdd(FriendlyByteBuf pleaseKillMe, Serializable value) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(out);

            try {
                outputStream.writeObject(value);
            } catch (Throwable var7) {
                try {
                    outputStream.close();
                } catch (Throwable var6) {
                    var7.addSuppressed(var6);
                }

                throw var7;
            }

            outputStream.close();
        } catch (IOException var8) {
            System.out.println("gotcha" + var8.toString());
            throw new RuntimeException(var8);
        }

        pleaseKillMe.writeByteArray(out.toByteArray());
    }
    public static <T extends Serializable> T deserializeAdd(FriendlyByteBuf data) {
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(data.readByteArray());

            Serializable var2;
            try {
                var2 = (Serializable)(new ObjectInputStream(bis)).readObject();
            } catch (Throwable var5) {
                try {
                    bis.close();
                } catch (Throwable var4) {
                    var5.addSuppressed(var4);
                }

                throw var5;
            }

            bis.close();
            return (T) var2;
        } catch (ClassNotFoundException | IOException var6) {
            System.out.println("gotcha" + var6.toString());
            throw new RuntimeException(var6);
        }
    }
}
