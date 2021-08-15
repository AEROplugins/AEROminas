package aero.minas.verificadores;

import aero.minas.utils.Cuboid;
import aero.minas.utils.items.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.inventory.ItemStack;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import java.util.function.BiFunction;

import static aero.minas.main.AEROminas.*;
@SuppressWarnings("deprecation")
public class RestaurarMinerios implements Listener {
    HashMap<Player,Long> cd = new HashMap<>();
    private static final int multiplicador_fortune = getMain().getConfig().getInt("multiplicador-de-drops");
    private static final int drop_base = getMain().getConfig().getInt("drop-base")*multiplicador_fortune;
    Block block_quebrado;
    BiFunction<Location,ArrayList<Cuboid>,Boolean> verificar = (l,c) -> {
        boolean verificador_interno = false;
        for (Cuboid cuboid : c) {
            if (cuboid.contains(l)) {
                verificador_interno = true;
            }
        }
        return  verificador_interno;
    };
    BiFunction<Block,ArrayList<String>,Boolean> verificar_lista_de_blocos = (b,s) ->{
      boolean verificador_interno = false;
      try {
          for (String ids : s) {
              String[] id_data = ids.split(";");
              if (b.getTypeId() == Integer.parseInt(id_data[0]) && b.getData() == Integer.parseInt(id_data[1]))
                  verificador_interno = true;
          }
      }catch (Exception error){
          Bukkit.getConsoleSender().sendMessage("§c[AEROminas] a Config de items permitidos esta Errada!");
      }
      return verificador_interno;
    };

    @EventHandler
    public void aoQuebrarBlcos(BlockBreakEvent e) {
        if (getMain().getLocations().getConfigurationSection("locations") != null) {
            if (verificar.apply(e.getBlock().getLocation(),minas_locs)) {
                Player p = e.getPlayer();
                VerificarInventarioCheio(p);
                block_quebrado = e.getBlock();
                Location block_quebrado_location = block_quebrado.getLocation();
                Material block_quebrado_material = block_quebrado.getType();
                short block_quebrado_data = e.getBlock().getData();
                if (block_quebrado_material != Material.BEDROCK && p.getInventory().firstEmpty() != -1) {
                    if(verificar_lista_de_blocos.apply(block_quebrado,block_list)) {
                        e.setCancelled(true);
                        int fortune = p.getInventory().getItemInMainHand().getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS);
                            inviarItemComFortuna(block_quebrado_material,
                                    fortune, block_quebrado_data, p);
                            setarBlocoQuebrado(block_quebrado_location, Material.getMaterial(getMain()
                                    .getConfig()
                                    .getString("replace-block")));
                            realocarMinerio(block_quebrado_location, block_quebrado_material, (byte) block_quebrado_data);
                    }else e.setCancelled(true);
                } else e.setCancelled(true);
            }
        }
    }

    private  void realocarMinerio(Location block_quebrado_location,Material block_quebrado_material,byte block_quebrado_data){
        Bukkit.getScheduler().scheduleSyncDelayedTask(getMain(),() -> {
            block_quebrado_location.getBlock().setType(block_quebrado_material);
            block_quebrado_location.getBlock().setData(block_quebrado_data);
        }, 20L *getMain().getConfig().getInt("replace-block-delay"));
    }
    private void setarBlocoQuebrado(Location block_quebrado_location,Material replace_material){
        block_quebrado_location.getBlock().setType(replace_material);
    }
    private void inviarItemComFortuna(Material block_quebrado_material,int fortune,short block_quebrado_data,Player p){
        int fortune_final;
        if(fortune == 0) fortune_final = drop_base;
        else fortune_final = drop_base*fortune;
        ItemStack drop = new ItemBuilder(block_quebrado_material,fortune_final,block_quebrado_data).build();
        p.getInventory().addItem(drop);
    }
    private void VerificarInventarioCheio(Player p){
        if(cd.containsKey(p) && !(System.currentTimeMillis() >= cd.get(p))){
            return;
        }else cd.remove(p);

        cd.put(p,System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(2));
        if(p.getInventory().firstEmpty() == -1){
            p.sendMessage("");
            p.sendMessage("§cO seu inventário esta cheio!!");
            p.sendMessage("");
            p.playSound(p.getLocation(), Sound.ENTITY_SLIME_SQUISH,1,1);
        }
    }

    @EventHandler
    public void cancelarFisica(BlockPhysicsEvent e){
        if (getMain().getLocations().getConfigurationSection("locations") != null) {
            if (verificar.apply(e.getBlock().getLocation(),minas_locs)) {
                e.setCancelled(true);
            }
        }
    }

}

