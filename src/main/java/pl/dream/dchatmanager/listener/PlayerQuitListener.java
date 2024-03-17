package pl.dream.dchatmanager.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import pl.dream.dchatmanager.DChatManager;

public class PlayerQuitListener implements Listener {

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e){
        DChatManager.getPlugin().sendMessageListener.playerCooldownList.remove(e.getPlayer().getUniqueId());
    }
}
