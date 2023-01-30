package carrot.dev.customrecipes.Recipes;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class registerRecipes {

    public static void register(){
        registerCustomDiamond();
        registerAnother();
        registerFINALFINAL();
    }

    private static ItemStack diamondCustom(){
        return new ItemBuilder(Material.DIAMOND)
                .setDisplayName("&bCursed Diamond")
                .setLore(
                        "&7This diamond is cursed,",
                        "&7it can be used to craft",
                        "&7some cursed items."
                )
                .setCustomModelData(30)
                .build();
    }

    private static ItemStack diamondCustom2(){
        return new ItemBuilder(Material.DIAMOND)
                .setDisplayName("&bCursed Diamond2")
                .setLore(
                        "&7This diamond is cursed,",
                        "&7it can be used to craft",
                        "&7some cursed items."
                )
                .setCustomModelData(30)
                .build();
    }

    private static void registerCustomDiamond(){
        new BuildReciper("cursed_diamond", diamondCustom(), 64)
                .setShape(
                        "DDD",
                        "",
                        ""
                )
                .setIngredient('D', Material.DIAMOND)
                .register();
    }

    private static void registerAnother(){
        new BuildReciper("cursed2", diamondCustom2(), 1)
                .setShape(
                        "DDD",
                        "DDD",
                        "DD"
                )
                .setIngredient('D', diamondCustom(), 10)
                .register();
    }

    private static void registerFINALFINAL(){
        new BuildReciper("FINAL", new ItemBuilder(Material.NETHERITE_SWORD).build(), 2)
                .setShape(
                        "2D2",
                        " D ",
                        " S "
                )
                .setIngredient('S', new ItemStack(Material.STICK), 64)
                .setIngredient('D', diamondCustom(), 16)
                .setIngredient('2', diamondCustom2(), 32)
                .register();
    }

}
