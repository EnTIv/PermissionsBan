package com.entiv.permissionsban;

import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class Main extends JavaPlugin {

    private static Main plugin;

    private static String getMessage() {
        return plugin.getConfig().getString("提示消息");
    }

    private static final BukkitRunnable runnable = new BukkitRunnable() {
        @Override
        public void run() {
            for (Player player : Bukkit.getOnlinePlayers()) {

                if (player.isOp()) continue;

                boolean hasIllegalPermissions = false;

                for (PermissionAttachmentInfo effectivePermission : player.getEffectivePermissions()) {
                    String permission = effectivePermission.getPermission();
                    if (permission.contains("customnpcs")) {
                        hasIllegalPermissions = true;
                        break;
                    }
                }

                if (hasIllegalPermissions) {
                    BanList banList = Bukkit.getBanList(BanList.Type.NAME);
                    player.kickPlayer(getMessage());
                    banList.addBan(player.getName(), getMessage(), null, "PermissionsBan");
                }
            }
        }
    };

    @Override
    public void onEnable() {
        plugin = this;

        String[] message = {
                "§anpcmod修复插件§e v" + getDescription().getVersion() + " §a已启用",
                "§a插件制作作者:§e EnTIv §aQQ群:§e 600731934"
        };

        saveDefaultConfig();
        getServer().getConsoleSender().sendMessage(message);
        runnable.runTaskTimer(this, 0, 20);
    }

    @Override
    public void onDisable() {
        String[] message = {
                "§anpcmod修复插件§e v" + getDescription().getVersion() + " §a已x卸载",
                "§a插件制作作者:§e EnTIv §aQQ群:§e 600731934"
        };
        getServer().getConsoleSender().sendMessage(message);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.isOp()) {
            reloadConfig();
            sender.sendMessage("§9§lPermissionsBan§6§l >>§a npcmod修复插件已重载成功!");
        }
        return true;
    }
}
