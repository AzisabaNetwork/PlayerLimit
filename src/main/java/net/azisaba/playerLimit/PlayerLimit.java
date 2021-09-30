package net.azisaba.playerLimit;

import net.azisaba.playerLimit.listener.PlayerPreLoginListener;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public class PlayerLimit extends JavaPlugin {
    public int maxPlayers = -1;
    public String message = "&cサーバーは満員です!";
    public Permission permissionsProvider = null;

    public static PlayerLimit getInstance() {
        return PlayerLimit.getPlugin(PlayerLimit.class);
    }

    @Override
    public void onEnable() {
        saveDefaultConfig();
        try {
            RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
            if (rsp != null) {
                permissionsProvider = rsp.getProvider();
            }
        } catch (RuntimeException | NoClassDefFoundError ignore) {}
        getServer().getPluginManager().registerEvents(new PlayerPreLoginListener(), this);
        Objects.requireNonNull(getServer().getPluginCommand("playerlimit")).setExecutor(new PlayerLimitCommand());
        reload();
    }

    public boolean hasPermission(OfflinePlayer player, String perm) {
        if (permissionsProvider == null) return false;
        return permissionsProvider.playerHas(Bukkit.getWorlds().get(0).getName(), player, perm);
    }

    public void saveConfigAsync() {
        new Thread(this::saveConfig).start();
    }

    @Override
    public void saveConfig() {
        getConfig().set("maxPlayers", maxPlayers);
        getConfig().set("message", message);
        super.saveConfig();
    }

    @Override
    public void onDisable() {
        saveConfig();
    }

    public void reload() {
        reloadConfig();
        maxPlayers = getConfig().getInt("maxPlayers", -1);
        message = getConfig().getString("message", "&cサーバーは満員です!");
    }
}
