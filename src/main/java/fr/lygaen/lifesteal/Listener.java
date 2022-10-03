package fr.lygaen.lifesteal;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.persistence.PersistentDataType;

public class Listener implements org.bukkit.event.Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player p = event.getPlayer();

        if(p.getPersistentDataContainer().has(new NamespacedKey(LifeSteal.instance, "spectator"))) {
            p.sendMessage(ChatColor.RED + "[LifeSteal]"+ ChatColor.RESET +" you are dead !");
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player victim = event.getPlayer();
        if(victim.getKiller() == null)
            return;
        Player killer = victim.getKiller();

        AttributeInstance victimHealth = victim.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        assert victimHealth != null;

        if(victimHealth.getBaseValue() <= 2) {
            victim.setGameMode(GameMode.SPECTATOR);
            //the real important part here is the key, not the data on itself
            victim.getPersistentDataContainer().set(new NamespacedKey(LifeSteal.instance, "spectator"), PersistentDataType.BYTE, (byte)1);

            victim.sendMessage(ChatColor.RED + "[LifeSteal]"+ ChatColor.RESET + "You ran out of hearts !");
            victim.sendMessage(ChatColor.RED + "[LifeSteal]"+ ChatColor.RESET +" You can now spectate the others.");
        } else {
            victimHealth.setBaseValue(victimHealth.getBaseValue() - 2);
            killer.sendMessage(ChatColor.RED + "[LifeSteal]"+ ChatColor.RESET + killer.displayName() +" stole one heart from you !");
        }

        AttributeInstance killerHealth = killer.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        assert killerHealth != null;

        killerHealth.setBaseValue(killerHealth.getBaseValue() + 2);
        killer.sendMessage(ChatColor.RED + "[LifeSteal]"+ ChatColor.RESET + "You gained one heart from "+ victim.displayName() +" !");
    }
}
