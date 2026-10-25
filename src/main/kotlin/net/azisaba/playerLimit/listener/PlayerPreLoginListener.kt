package net.azisaba.playerLimit.listener

import net.azisaba.playerLimit.PlayerLimit
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerPreLoginEvent

class PlayerPreLoginListener : Listener {
    @EventHandler(priority = EventPriority.LOW)
    fun onAsyncPlayerPreLogin(e: AsyncPlayerPreLoginEvent) {
        val plugin = PlayerLimit.instance
        val op = Bukkit.getOfflinePlayer(e.uniqueId)
        if (op.isOp || plugin.hasPermission(op, "playerlimit.bypass")) return
        val max = plugin.maxPlayers
        if (max != -1 && Bukkit.getOnlinePlayers().count { !it.hasPermission("playerlimit.bypass") } >= max) {
            e.disallow(
                AsyncPlayerPreLoginEvent.Result.KICK_OTHER,
                LegacyComponentSerializer.legacyAmpersand().deserialize(plugin.message),
            )
        }
    }
}
