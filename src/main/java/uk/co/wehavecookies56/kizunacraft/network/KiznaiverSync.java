package uk.co.wehavecookies56.kizunacraft.network;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.relauncher.Side;
import uk.co.wehavecookies56.kizunacraft.Kiznaivers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Toby on 17/05/2016.
 */
public class KiznaiverSync extends AbstractMessage.AbstractClientMessage<KiznaiverSync> {

    public KiznaiverSync() {}

    private List<String> kiznaivers;
    private boolean implant;

    public KiznaiverSync(Kiznaivers.IKiznaivers kiznaivers) {
        this.kiznaivers = kiznaivers.getKiznaivers();
        this.implant = kiznaivers.hasImplant();
    }

    @Override
    protected void read(PacketBuffer buffer) throws IOException {
        implant = buffer.readBoolean();
        kiznaivers = new ArrayList<String>();
        while (buffer.isReadable()) {
            kiznaivers.add(buffer.readStringFromBuffer(100));
        }
    }

    @Override
    protected void write(PacketBuffer buffer) throws IOException {
        buffer.writeBoolean(implant);
        for (int i = 0; i < kiznaivers.size(); i++) {
            buffer.writeString(kiznaivers.get(i));
        }
    }

    @Override
    public void process(EntityPlayer player, Side side) {
        final Kiznaivers.IKiznaivers kiznaiver = player.getCapability(Kiznaivers.KIZNAIVERS, null);
        kiznaiver.setHasImplant(implant);
        for (int i = 0; i < kiznaivers.size(); i++) {
            kiznaiver.bindKiznaiver(kiznaivers.get(i));
        }
        System.out.println(side);
        System.out.println(implant);
    }
}