package net.azazelzero.derp.core.data;

import net.azazelzero.derp.DerpEventHandler;
import net.azazelzero.derp.core.derp.DERPData;
import net.azazelzero.derp.core.derp.Slottable;
import net.azazelzero.derp.core.net.SerializeUtil;
import net.minecraft.nbt.CompoundTag;

import java.io.IOException;
import java.util.ArrayList;

public class DerpPlayerData{
  private int val;
  public ArrayList<DERPData[]> derps=null;
  public Slottable[] slots=null;

  public void copyFrom(DerpPlayerData source) {
    this.val = source.val;
  }
  public void saveNBTData(CompoundTag nbt){
      nbt.putInt("val",val);
      try {
          nbt.putByteArray("derps", net.azazelzero.derp.core.net.SerializeUtil.serialize(derps));
          nbt.putByteArray("Slotted", net.azazelzero.derp.core.net.SerializeUtil.serialize(slots));

      } catch (IOException e) {
          nbt.putByteArray("derps",new byte[1]);
      }

  }
  public void loadNBTData(CompoundTag nbt){
      this.val = nbt.getInt("val");
      try {
          derps= SerializeUtil.deserialize(nbt.getByteArray("derps"));
          slots= SerializeUtil.deserialize(nbt.getByteArray("Slotted"));

      } catch (IOException | ClassNotFoundException e) {
          derps= null;
          slots=new Slottable[5];
      }
  }
}

