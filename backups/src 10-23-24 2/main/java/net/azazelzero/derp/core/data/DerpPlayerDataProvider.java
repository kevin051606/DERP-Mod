package net.azazelzero.derp.core.data;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DerpPlayerDataProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {
    public static Capability<DerpPlayerData> DERP_DATA = CapabilityManager.get(new CapabilityToken<DerpPlayerData>() { });

    private DerpPlayerData derpData;
    private final LazyOptional<DerpPlayerData> optional = LazyOptional.of(this::createPlayerDerpData);

    private DerpPlayerData createPlayerDerpData() {
        if(this.derpData==null){
            this.derpData = new DerpPlayerData();
        }
        return this.derpData;
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if(cap==DERP_DATA){
            return optional.cast();
        }

        return LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt= new CompoundTag();
        createPlayerDerpData().saveNBTData(nbt);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        createPlayerDerpData().loadNBTData(nbt);
    }
}
