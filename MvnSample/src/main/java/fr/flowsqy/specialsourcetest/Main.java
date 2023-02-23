package fr.flowsqy.specialsourcetest;

import net.minecraft.world.entity.player.Player;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_19_R2.entity.CraftPlayer;

public class Main {

    public static void main(String[] args) {
        final org.bukkit.entity.Player player = Bukkit.getOnlinePlayers().iterator().next();
        if(player == null) {
            return;
        }
        final Player nmsPlayer = ((CraftPlayer) player).getHandle();
        System.out.println(nmsPlayer.bob);
        System.out.println(nmsPlayer.getSleepTimer());
    }

}
