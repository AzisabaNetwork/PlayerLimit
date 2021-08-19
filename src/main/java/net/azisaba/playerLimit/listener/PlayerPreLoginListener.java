package net.azisaba.playerLimit.listener;

import net.azisaba.playerLimit.PlayerLimit;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

public class PlayerPreLoginListener implements Listener {
    @EventHandler(priority = EventPriority.LOW)
    public void onAsyncPlayerPreLogin(AsyncPlayerPreLoginEvent e) {
        if (Bukkit.getOfflinePlayer(e.getUniqueId()).isOp()) return;
        int max = PlayerLimit.getInstance().maxPlayers;
        if (max != -1 && Bukkit.getOnlinePlayers().size() >= max) {
            e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, PlayerLimit.getInstance().message);
        }
    }
}
