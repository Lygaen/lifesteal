package fr.lygaen.lifesteal;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public final class LifeSteal extends JavaPlugin {
    public static LifeSteal instance;

    @Override
    public void onEnable() {
        // Plugin startup logic
        getServer().getPluginManager().registerEvents(new Listener(), this);
        Objects.requireNonNull(getCommand("lifereset")).setExecutor(this);

        instance = this;
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        instance = null;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player target;
        if(args.length == 1) {
            target = Bukkit.getPlayerExact(args[0]);
            if(target == null) return false;
        } else if (args.length == 0 && sender instanceof Player) {
            target = (Player) sender;
        } else return false;

        target.getPersistentDataContainer().remove(new NamespacedKey(LifeSteal.instance, "spectator"));
        if(target.getGameMode().equals(GameMode.SPECTATOR))
            target.setGameMode(GameMode.SURVIVAL);

        AttributeInstance targetHealth = target.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        assert targetHealth != null;

        targetHealth.setBaseValue(targetHealth.getDefaultValue());
        target.sendMessage(ChatColor.RED + "[LifeSteal]"+ ChatColor.RESET + "Your life was reset !");
        sender.sendMessage(ChatColor.RED + "[LifeSteal]"+ ChatColor.RESET + "Correctly reset "+target.displayName()+"'s life !");

        return true;
    }
}
