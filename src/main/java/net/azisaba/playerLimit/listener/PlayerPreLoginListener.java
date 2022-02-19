package net.azisaba.playerLimit.listener;

import net.azisaba.playerLimit.PlayerLimit;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

public class PlayerPreLoginListener implements Listener {
    @EventHandler(priority = EventPriority.LOW)
    public void onAsyncPlayerPreLogin(AsyncPlayerPreLoginEvent e) {
        OfflinePlayer op = Bukkit.getOfflinePlayer(e.getUniqueId());
        if (op.isOp() || PlayerLimit.getInstance().hasPermission(op, "playerlimit.bypass")) return;
        int max = PlayerLimit.getInstance().maxPlayers;
        if (max != -1 && Bukkit.getOnlinePlayers().stream().filter(p -> !p.hasPermission("playerlimit.bypass")).count() >= max) {
            e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, ChatColor.translateAlternateColorCodes('&', PlayerLimit.getInstance().message));
        }
    }
}
