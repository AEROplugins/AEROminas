package aero.minas.main;

import aero.minas.comandos.CriarMina;
import aero.minas.utils.Cuboid;
import aero.minas.verificadores.JogadorDentroDeRegiao;
import aero.minas.verificadores.JogadorSelectorEmMaos;
import aero.minas.verificadores.RestaurarMinerios;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;


public class AEROminas extends JavaPlugin {
    public static ArrayList<String> block_list = new ArrayList<>();
    public static ArrayList<Cuboid> minas_locs = new ArrayList<>();
    private static AEROminas main;
    private Locations locations;
    @Override
    public void onEnable() {
        main = this;
        carregarConfig();
        locations = new Locations();

        CarregarLocations();
        carregarBlocos();
        RegistrarComandos();
        RegistrarEventos();
    }

    @Override
    public void onDisable() {

    }

    public static AEROminas getMain() {
        return main;
    }

    public FileConfiguration getLocations(){
        return locations.getConfig();
    }
    public void saveLocations(){
        locations.saveConfig();
    }

    private void carregarConfig(){
        getConfig().options().copyDefaults(false);
        saveDefaultConfig();
    }

    private void RegistrarEventos(){
        Bukkit.getPluginManager().registerEvents(new JogadorDentroDeRegiao(),this);
        Bukkit.getPluginManager().registerEvents(new JogadorSelectorEmMaos(), this);
        Bukkit.getPluginManager().registerEvents(new RestaurarMinerios(),this);
    }
    private void RegistrarComandos(){
        getCommand("criarmina").setExecutor(new CriarMina());
    }

    private void carregarBlocos(){
        block_list.addAll(getConfig().getStringList("Items-permitidos-para-quebra"));
    }
    public void CarregarLocations(){
       for(String msg: getLocations().getConfigurationSection("locations").getKeys(false)) {
           if (msg != null) {
               int loc1x, loc1y, loc1z, loc2x, loc2y, loc2z;
               World world = Bukkit.getWorld(getLocations().getString("locations." + msg + ".world"));
               loc1x = getLocations().getInt("locations." + msg + ".pos1x");
               loc1y = getLocations().getInt("locations." + msg + ".pos1y");
               loc1z = getLocations().getInt("locations." + msg + ".pos1z");
               loc2x = getLocations().getInt("locations." + msg + ".pos2x");
               loc2y = getLocations().getInt("locations." + msg + ".pos2y");
               loc2z = getLocations().getInt("locations." + msg + ".pos2z");
               Location pos1 = new Location(world, loc1x, loc1y, loc1z);
               Location pos2 = new Location(world, loc2x, loc2y, loc2z);
               Cuboid cuboid = new Cuboid(pos1, pos2);
               minas_locs.add(cuboid);
           }
       }
        Bukkit.getConsoleSender().sendMessage("§2[AEROminas] Todas as minas carregadas com Sucesso!");
    }
}
