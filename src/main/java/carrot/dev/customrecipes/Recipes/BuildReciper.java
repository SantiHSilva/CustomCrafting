package carrot.dev.customrecipes.Recipes;

import carrot.dev.customrecipes.Main;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static carrot.dev.customrecipes.Main.broadcast;
import static carrot.dev.customrecipes.Main.console;

public class BuildReciper {
    protected ShapedRecipe recipe;
    protected boolean haveAmount = false;
    public static HashMap<ItemStack, HashMap<ItemStack, List<Integer>>> ingredientsWithAmount = new HashMap<>();

    //Temp hashmap
    protected HashMap<ItemStack, List<Integer>> materialesParaIngrediente;

    public BuildReciper(String id, ItemStack item) {
        this.materialesParaIngrediente = new HashMap<>();
        this.recipe = new ShapedRecipe(new NamespacedKey(Main.getInstance(), id), item);
        console("Registering recipe: " + this.recipe.getKey().getKey());
    }

    public BuildReciper(String id, ItemStack item, int amount){
        this.materialesParaIngrediente = new HashMap<>();
        if(amount > 64) amount = 64; // No se puede poner mas de 64 items en un slot
        item.setAmount(amount);
        this.recipe = new ShapedRecipe(new NamespacedKey(Main.getInstance(), id), item);
        console("REGISTRANDO CRAFTEO: " + this.recipe.getKey().getKey());
    }

    private static String check(String shape){
        // Esto es para que no haya problemas con los espacios en blanco
        // en la forma de la receta, ya que si no se pone esto, el juego
        // no reconoce la receta.
        switch(shape.length()){
            case 0 -> shape = "   ";
            case 1 -> shape = shape + "  ";
            case 2 -> shape = shape + " ";
            default -> {
                return shape;
            }
        }
        return shape;
    }

    public BuildReciper setShape(String top, String mid, String bot) {
        this.recipe.shape(check(top), check(mid), check(bot));
        return this;
    }

    public BuildReciper setIngredient(char key, ItemStack item) {
        this.recipe.setIngredient(key, item);
        return this;
    }

    public BuildReciper setIngredient(char key, ItemStack item, int amount) {
        this.haveAmount = true;
        item.setAmount(amount);
        this.recipe.setIngredient(key, item);
        //get key in the shape
        console("--------------------");
        console("Registrando con la key: " + key + " el item: " + item + " con la cantidad: " + amount);
        console("shape: " + Arrays.toString(this.recipe.getShape()));
        // Obtener el slot en el que se encuentra el ingrediente
        List<Integer> cacheSlots = new ArrayList<>();
        for (int i = 0; i < this.recipe.getShape().length; i++) {
            String shape = this.recipe.getShape()[i];
            for (int j = 0; j < shape.length(); j++) {
                if (shape.charAt(j) == key) {
                    cacheSlots.add(i * 3 + j);
                }
            }
        }
        broadcast("Slots: " + cacheSlots);
        materialesParaIngrediente.put(item, cacheSlots);
        return this;
    }

    public BuildReciper setIngredient(char key, Material material) {
        this.recipe.setIngredient(key, material);
        return this;
    }

    public void register() {
        // Add recipe to server
        Main.getInstance().getServer().addRecipe(this.recipe);
        // Add recipe to hashmap
        if(this.haveAmount){
            ingredientsWithAmount.put(this.recipe.getResult(), this.materialesParaIngrediente);
            console("hashmap: " + ingredientsWithAmount);
        }
    }

}
