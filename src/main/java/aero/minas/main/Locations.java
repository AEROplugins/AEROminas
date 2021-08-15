package aero.minas.main;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import static aero.minas.main.AEROminas.*;
public class Locations {

    private final File file;
    private final FileConfiguration fileConfiguration;

    public Locations(){
        file = new File(getMain().getDataFolder(),"locations.yml");
        fileConfiguration = YamlConfiguration.loadConfiguration(file);
        if(!file.exists()){
            try {
                file.createNewFile();
                getConfig().createSection("locations");
                saveConfig();
                Bukkit.getConsoleSender().sendMessage("§2[AEROminas] sucesso ao criar a locations.yml!");
            }catch (Exception error){
                Bukkit.getConsoleSender().sendMessage("§c[AEROminas] erro ao criar a locations.yml!");
            }
        }
    }

    public FileConfiguration getConfig(){
        return fileConfiguration;
    }

    public void saveConfig(){
        try {
            getConfig().save(file);
            Bukkit.getConsoleSender().sendMessage("§2[AEROminas] sucesso ao salvar a locations.yml!");
        }catch (Exception error){
            Bukkit.getConsoleSender().sendMessage("§c[AEROminas] erro ao salvar a locations.yml!");
        }
    }

}
