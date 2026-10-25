package net.azisaba.playerLimit

import net.azisaba.playerLimit.listener.PlayerPreLoginListener
import net.milkbowl.vault.permission.Permission
import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer
import org.bukkit.plugin.java.JavaPlugin

class PlayerLimit : JavaPlugin() {
    // read from the async pre-login thread and written from region/global threads
    @Volatile
    var maxPlayers = -1

    @Volatile
    var message = DEFAULT_MESSAGE

    private var permissionsProvider: Permission? = null

    override fun onEnable() {
        saveDefaultConfig()
        try {
            permissionsProvider = server.servicesManager.getRegistration(Permission::class.java)?.provider
        } catch (_: RuntimeException) {
        } catch (_: NoClassDefFoundError) {
        }
        server.pluginManager.registerEvents(PlayerPreLoginListener(), this)
        requireNotNull(getCommand("playerlimit")).setExecutor(PlayerLimitCommand())
        reload()
    }

    fun hasPermission(player: OfflinePlayer, perm: String): Boolean {
        val provider = permissionsProvider ?: return false
        return provider.playerHas(Bukkit.getWorlds()[0].name, player, perm)
    }

    fun saveConfigAsync() {
        server.asyncScheduler.runNow(this) { saveConfig() }
    }

    @Synchronized
    override fun saveConfig() {
        config.set("maxPlayers", maxPlayers)
        config.set("message", message)
        super.saveConfig()
    }

    override fun onDisable() {
        saveConfig()
    }

    @Synchronized
    fun reload() {
        reloadConfig()
        maxPlayers = config.getInt("maxPlayers", -1)
        message = config.getString("message", DEFAULT_MESSAGE) ?: DEFAULT_MESSAGE
    }

    companion object {
        private const val DEFAULT_MESSAGE = "&cサーバーは満員です!"

        val instance: PlayerLimit
            get() = getPlugin(PlayerLimit::class.java)
    }
}
