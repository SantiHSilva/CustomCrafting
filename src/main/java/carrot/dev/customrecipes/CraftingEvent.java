package carrot.dev.customrecipes;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;

import static carrot.dev.customrecipes.Main.broadcast;
import static carrot.dev.customrecipes.Recipes.BuildReciper.ingredientsWithAmount;
import static carrot.dev.customrecipes.Recipes.BuildReciper.listaDeIngredientesConCantidad;

import java.util.*;

public class CraftingEvent implements Listener {

    @EventHandler
    private static void removeCustomRecipeItems(CraftItemEvent e){
        if(e.getInventory().getResult() == null) return;
        boolean isShiftClick = e.isShiftClick();
        if(isShiftClick)
            removeCustomItemWithShiftClick(e.getInventory(), (Player) e.getWhoClicked());
        else
            removeCustomItemsNoShiftClick(e.getInventory());
    }

    public static List<ItemStack> cacheItems(CraftingInventory inv){
        return new ArrayList<>(Arrays.asList(inv.getMatrix()));
    }

    private static void removeCustomItemWithShiftClick(CraftingInventory inv, Player target){
        ItemStack result = inv.getResult();
        if(result == null) return;
        if(isResultItemInHashMap(result)){
            target.sendMessage("No puedes hacer shift click");
            return;
        }
    }

    private static void removeCustomItemsNoShiftClick(CraftingInventory inv){
        ItemStack result = inv.getResult();
        if(isResultItemInHashMap(result)){
            List<ItemStack> items = cacheItems(inv);
            int[] invMatrixCopy = new int[inv.getMatrix().length];
            for(int i = 0; i < invMatrixCopy.length; i++) {
                ItemStack copy = items.get(i);
                if(copy == null) continue;
                broadcast("Item: " + copy.getType() + " Amount: " + copy.getAmount());
                ItemStack item = getItemOfIngredient(result, copy, i);
                if(item == null) continue;
                broadcast("Item to ingredient: " + item.getType() + " Amount: " + item.getAmount());
                int remove = copy.getAmount() - item.getAmount() + 1;
                broadcast("Remove: " + remove);
                if (remove <= 0)
                    inv.setItem(i, null);
                else
                    inv.getMatrix()[i].setAmount(remove);
            }
        }
    }

    @EventHandler
    private static void onPlayerCraftItem(PrepareItemCraftEvent e){
        if(e.getInventory().getResult() == null) return;
        checkCrafts(e.getInventory());
    }

    private static boolean isResultItemInHashMap(ItemStack result){
        return listaDeIngredientesConCantidad.contains(result);
    }

    private static ItemStack getItemOfIngredient(ItemStack result, ItemStack ingredient, int slot){
        Set<Map.Entry<ItemStack, List<Integer>>> entry = ingredientsWithAmount.get(result).entrySet();
        for(Map.Entry<ItemStack, List<Integer>> entry1 : entry){
            if(entry1.getKey().getType().equals(ingredient.getType())){
                if(entry1.getValue().contains(slot)){
                    return entry1.getKey();
                }
            }
        }
        return null;
    }

    private static boolean isSameItemStack(ItemStack item1, ItemStack item2){
        ItemStack copy1 = item1.clone();
        ItemStack copy2 = item2.clone();
        copy1.setAmount(1);
        copy2.setAmount(1);
        //broadcast("is equals?: " + copy1.equals(copy2));
        return copy1.equals(copy2);
    }

    private static Integer getAmountOfIngredient(ItemStack result, ItemStack ingredient, int slot){
        ItemStack item = getItemOfIngredient(result, ingredient, slot);
        if(item == null || !isSameItemStack(item,ingredient)) return -1;
        return item.getAmount();
    }

    private static void checkCrafts(CraftingInventory inv){
        ItemStack result = inv.getResult();
        //broadcast("--------------------");
        //broadcast("result: " + result);
        //broadcast("is in hashmap: " + resultItemsInHashMap().contains(result));
        //broadcast("--------------------");
        if(isResultItemInHashMap(result)){
            inv.setResult(null);
            for(int i = 0; i < inv.getMatrix().length; i++){
                //broadcast("slot: " + i);
                ItemStack ingredient = inv.getMatrix()[i];
                if(ingredient == null) continue;
                ItemStack ingredientOfResult = getItemOfIngredient(result, ingredient, i);
                //broadcast("ingredient: " + ingredient);
                //broadcast("ingredient of result: " + ingredientOfResult);
                if(ingredientOfResult == null || !isSameItemStack(ingredientOfResult, ingredient)) return;
                if(!(ingredient.getAmount() >= getAmountOfIngredient(result, ingredient, i))){
                    //broadcast("no hay suficientes ingredientes");
                    inv.setResult(null);
                    return; // rompe el ciclo y cancela la funcion
                }
                //broadcast("cantidad necesaria: " + getAmountOfIngredient(result, ingredient, i));
                //broadcast("cantidad actual: " + ingredient.getAmount());
            }
            inv.setResult(result);
        }
    }
}
