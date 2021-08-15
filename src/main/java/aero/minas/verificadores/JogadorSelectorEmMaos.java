package aero.minas.verificadores;

import aero.minas.comandos.CriarMina;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class JogadorSelectorEmMaos implements Listener {

    private static Location loc1 = null;
    private static Location loc2 = null;

    @EventHandler
    public void proibirQuebraDeBlocos(BlockBreakEvent e){
        Player p = e.getPlayer();
        if(p.getInventory().getItemInMainHand().isSimilar(CriarMina.seletor))
            e.setCancelled(true);
    }

    @EventHandler
    public void selecionarCuboid(PlayerInteractEvent e){
        Player p = e.getPlayer();
        if(p.hasPermission("aerominas.criarmina") && p.getInventory().getItemInMainHand().isSimilar(CriarMina.seletor)) {
            if(e.getAction() == Action.LEFT_CLICK_BLOCK && e.getClickedBlock().getType() != Material.AIR){
                loc1 = e.getClickedBlock().getLocation();
                p.sendMessage("§6§lpos1 foi marcado com sucesso");
            }
            if(e.getAction() == Action.RIGHT_CLICK_BLOCK && e.getClickedBlock().getType() != Material.AIR){
                loc2 = e.getClickedBlock().getLocation();
                p.sendMessage("§6§lpos2 foi marcado com sucesso");
            }
        }
    }

    public static Location getLoc1() {
        return loc1;
    }

    public static Location getLoc2() {
        return loc2;
    }
}
