package carrot.dev.customrecipes;

import carrot.dev.customrecipes.Recipes.registerRecipes;
import org.bukkit.Bukkit;
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

    }

}
