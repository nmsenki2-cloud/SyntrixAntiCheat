package hu.guardac.listener;

import com.github.retrooper.packetevents.event.PacketListenerAbstract;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import hu.guardac.GuardAC;
import hu.guardac.data.PlayerData;
import org.bukkit.entity.Player;

public class PacketListener extends PacketListenerAbstract {

    private final GuardAC plugin;

    public PacketListener(GuardAC plugin) { this.plugin = plugin; }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        if (!(event.getPlayer() instanceof Player player)) return;

        PlayerData data = plugin.getCheckManager().getData(player);
        if (data == null || data.isBypassing()) return;

        if (event.getPacketType() == PacketType.Play.Client.PLAYER_POSITION ||
            event.getPacketType() == PacketType.Play.Client.PLAYER_POSITION_AND_ROTATION ||
            event.getPacketType() == PacketType.Play.Client.PLAYER_ROTATION) {

            data.setPacketCount(data.getPacketCount() + 1);
        }
    }
}
