package carrot.dev.customrecipes;

import carrot.dev.customrecipes.Recipes.registerRecipes;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    private static Main plugin;

    public static Main getInstance() {
        return plugin;
    }

    @Override
    public void onEnable() {
        plugin = this;
        registerRecipes.register();
        Bukkit.getPluginManager().registerEvents(new CraftingEvent(), this);
        // Plugin startup logic

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static String chatColor(String s){
        return s.replace("&", "§");
    }

    public static void console(String s){
        Bukkit.getConsoleSender().sendMessage(chatColor(s));
    }

    public static void broadcast(String msg){
        console(msg);
        for(Player target : Bukkit.getOnlinePlayers()){
            target.sendMessage(chatColor(msg));
        }
    }

}
