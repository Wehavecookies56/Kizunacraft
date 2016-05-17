package uk.co.wehavecookies56.kizunacraft;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Toby on 17/05/2016.
 */
public class Kiznaivers {

    public List<String> kiznaivers = new ArrayList<String>();

    @CapabilityInject(IKiznaivers.class)
    public static final Capability<IKiznaivers> KIZNAIVERS = null;

    public static void register() {
        CapabilityManager.INSTANCE.register(IKiznaivers.class, new Kiznaivers.Storage(), Kiznaivers.Default.class);
    }

    public interface IKiznaivers {
        List<String> getKiznaivers();
        boolean hasImplant();

        void bindKiznaiver(String name);
        void setHasImplant(boolean implant);
    }

    @SubscribeEvent
    public void onEntityConstructing(AttachCapabilitiesEvent.Entity event) {
        event.addCapability(new ResourceLocation("kizunacraft", "IKiznaivers"), new ICapabilitySerializable<NBTTagCompound>() {
            IKiznaivers inst = KIZNAIVERS.getDefaultInstance();

            @Override
            public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
                return capability == KIZNAIVERS;
            }

            @Override
            public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
                return capability == KIZNAIVERS ? (T) inst : null;
            }

            @Override
            public NBTTagCompound serializeNBT() {
                return (NBTTagCompound) KIZNAIVERS.getStorage().writeNBT(KIZNAIVERS, inst, null);
            }

            @Override
            public void deserializeNBT(NBTTagCompound nbt) {
                KIZNAIVERS.getStorage().readNBT(KIZNAIVERS, inst, null, nbt);
            }
        });
    }

    public static class Storage implements Capability.IStorage<IKiznaivers> {

        @Override
        public NBTBase writeNBT(Capability<IKiznaivers> capability, IKiznaivers instance, EnumFacing side) {
            NBTTagCompound properties = new NBTTagCompound();

            properties.setBoolean("Implant", instance.hasImplant());

            NBTTagList tagList = new NBTTagList();
            for (int i = 0; i < instance.getKiznaivers().size(); i++) {
                String s = instance.getKiznaivers().get(i);
                if (s != null) {
                    NBTTagCompound tagCompound = new NBTTagCompound();
                    tagCompound.setString("Kiznaivers" + i, s);
                    tagList.appendTag(tagCompound);
                }

            }
            properties.setTag("KiznaiverList", tagList);
            return properties;
        }

        @Override
        public void readNBT(Capability<IKiznaivers> capability, IKiznaivers instance, EnumFacing side, NBTBase nbt) {
            NBTTagCompound properties = (NBTTagCompound) nbt;
            instance.setHasImplant(properties.getBoolean("Implant"));
            NBTTagList tagList = properties.getTagList("KiznaiverList", Constants.NBT.TAG_COMPOUND);
            for (int i = 0; i < tagList.tagCount(); i++) {
                NBTTagCompound recipes = tagList.getCompoundTagAt(i);
                    instance.getKiznaivers().add(i, recipes.getString("Kiznaivers" + i));
            }
        }
    }


    public static class Default implements IKiznaivers {
        private List<String> kiznaivers = new ArrayList<String>();

        private boolean hasImplant = false;

        @Override
        public boolean hasImplant() {
            return this.hasImplant;
        }

        @Override
        public void setHasImplant(boolean implant) {
            this.hasImplant = implant;
        }

        @Override
        public List<String> getKiznaivers() {
            return kiznaivers;
        }

        @Override
        public void bindKiznaiver(String name) {
            if (!kiznaivers.contains(name))
                kiznaivers.add(name);
        }
    }


}
