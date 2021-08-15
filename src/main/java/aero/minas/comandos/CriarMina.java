package aero.minas.comandos;

import aero.minas.utils.Cuboid;
import aero.minas.utils.items.ItemBuilder;
import aero.minas.verificadores.JogadorSelectorEmMaos;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import static aero.minas.main.AEROminas.*;
import java.util.Arrays;

public class CriarMina implements CommandExecutor {
    public static Cuboid mina;
    private FileConfiguration locations = getMain().getLocations();
    public final static ItemStack seletor = new ItemBuilder(Material.IRON_AXE,1,(short)0)
            .setDisplayName("&2&lCriador de Regions")
            .setLore(Arrays.asList("&fUtilize para criar uma regiao","&fde uma mina"))
            .build();


    @Override
    public boolean onCommand(CommandSender s, Command c, String label, String[] args) {
        if(!(s instanceof Player)) return false;
        Player p = (Player) s;
        Location p_local = p.getLocation();
        if(!p.hasPermission("aerominas.criarmina")){
            p.sendMessage("§cVoce nao tem permissão!");
            p.playSound(p_local, Sound.ENTITY_BLAZE_HURT,1,1);
            return false;
        }

        if(args.length < 1) {
            p.getInventory().addItem(seletor);
            p.sendMessage("§2Seletor Enviado para o inventario!");
            p.playSound(p_local, Sound.ENTITY_BLAZE_HURT, 1, 1);
            return true;
        }else if(args.length == 2 && args[0].equalsIgnoreCase("set")) {
            if (JogadorSelectorEmMaos.getLoc1() != null && JogadorSelectorEmMaos.getLoc2() != null) {
                locations.createSection("locations." + args[1]);
                locations.set("locations." + args[1] + ".world", p.getWorld().getName());
                locations.set("locations." + args[1] + ".pos1x", JogadorSelectorEmMaos.getLoc1().getX());
                locations.set("locations." + args[1] + ".pos1y", JogadorSelectorEmMaos.getLoc1().getY());
                locations.set("locations." + args[1] + ".pos1z", JogadorSelectorEmMaos.getLoc1().getZ());
                locations.set("locations." + args[1] + ".pos2x", JogadorSelectorEmMaos.getLoc2().getX());
                locations.set("locations." + args[1] + ".pos2y", JogadorSelectorEmMaos.getLoc2().getY());
                locations.set("locations." + args[1] + ".pos2z", JogadorSelectorEmMaos.getLoc2().getZ());
                getMain().saveLocations();
                getMain().CarregarLocations();
                p.sendMessage("§2§lA " + args[1] + " Foi Criada com Sucesso e já está Funcionando!");
                p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_YES, 7, 1);
            } else {
                p.sendMessage("§co Pos1 e Pos2 nao foram setados!");
                p.playSound(p_local, Sound.ENTITY_VILLAGER_NO, 1, 1);
            }
        } else if(args.length == 1 && args[0].equalsIgnoreCase("help")){
            p.playSound(p.getLocation(),Sound.ENTITY_BLAZE_HURT,1,1);
            p.sendMessage("");
            p.sendMessage("§3§l-=-=-- AEROminas --=-=-");
            p.sendMessage("§c/criarmina §7- Pega o item para selecionar a mina");
            p.sendMessage("§c/criarmina set <name> §7- Cria uma mina!");
            p.sendMessage("§3§l-=-=-- AEROminas --=-=-");
            p.sendMessage("");
        }else{
            p.sendMessage("§cUtilize: /criarmina help");
            p.playSound(p.getLocation(),Sound.ENTITY_VILLAGER_NO,1,1);
        }
        return true;
    }
}
