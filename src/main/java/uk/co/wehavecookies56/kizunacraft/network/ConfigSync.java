package uk.co.wehavecookies56.kizunacraft.network;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.relauncher.Side;
import uk.co.wehavecookies56.kizunacraft.ConfigHandler;

import java.io.IOException;

/**
 * Created by Toby on 21/05/2016.
 */
public class ConfigSync extends AbstractMessage.AbstractClientMessage<ConfigSync> {

    public ConfigSync() {}

    private int maxKiznaivers;

    public ConfigSync(int maxKiznaivers) {
        this.maxKiznaivers = maxKiznaivers;
    }

    @Override
    protected void read(PacketBuffer buffer) throws IOException {
        this.maxKiznaivers = buffer.readInt();
    }

    @Override
    protected void write(PacketBuffer buffer) throws IOException {
        buffer.writeInt(this.maxKiznaivers);
    }

    @Override
    public void process(EntityPlayer player, Side side) {
        ConfigHandler.maxKiznaivers = this.maxKiznaivers;
    }
}
