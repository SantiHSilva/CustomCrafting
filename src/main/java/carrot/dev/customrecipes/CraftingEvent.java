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

    private static List<ItemStack> cacheItems(CraftingInventory inv){
        return new ArrayList<>(Arrays.asList(inv.getMatrix()));
    }

    private static boolean isSameItemStack(ItemStack item1, ItemStack item2){
        ItemStack copy1 = item1.clone();
        ItemStack copy2 = item2.clone();
        copy1.setAmount(1);
        copy2.setAmount(1);
        //broadcast("is equals?: " + copy1.equals(copy2));
        return copy1.equals(copy2);
    }

    private static boolean isItemNotStackable16(ItemStack item){
        return item.getMaxStackSize() == 16;
    }

    private static boolean isNotStackable(ItemStack item){
        return item.getMaxStackSize() == 1;
    }

    private static List<ItemStack> getInventoryContentNoArmorAndOffHand(Player target){
        List<ItemStack> items = new ArrayList<>(Arrays.asList(target.getInventory().getContents()));
        //remove armor slots and offhand slot 36-41
        //remover los ultimos 5
        for(int i = 0; i < 5; i++){
            items.remove(items.size() - 1);
        }
        return items;
    }

    private static int getAmountForAddItem(Player target, ItemStack itemForAdd){
        int amount = 0;
        if(isNotStackable(itemForAdd)){
            // Si el item no es stackeable, solo buscara
            // los items de aire en el inventario
            for(ItemStack item : getInventoryContentNoArmorAndOffHand(target)){
                if(item == null) amount++;
            }
            return amount;
        }
        if(isItemNotStackable16(itemForAdd)){
            // Si el item es stackeable de 16, solo buscara
            // los items de aire en el inventario y los items
            // que coincidan con el item que se va a agregar
            // restando la cantidad de items que ya tiene
            for(ItemStack item : getInventoryContentNoArmorAndOffHand(target)){
                if(item == null) amount += 16;
                else if(isSameItemStack(item, itemForAdd)) amount += 16 - item.getAmount();
            }
            return amount;
        }
        // Si el item es stackeable de 64, solo buscara
        // los items de aire en el inventario y los items
        // que coincidan con el item que se va a agregar
        // restando la cantidad de items que ya tiene
        for(ItemStack item : getInventoryContentNoArmorAndOffHand(target)){
            if(item == null){
                broadcast("Adding 64 items for air");
                amount += 64;
            }
            else if(isSameItemStack(item, itemForAdd)){
                broadcast("Adding " + (64 - item.getAmount()) + " items for " + item);
                amount += 64 - item.getAmount();
            }
        }
        return amount;
    }

    private static void removeCustomItemWithShiftClick(CraftingInventory inv, Player target){
        ItemStack result = inv.getResult();
        if(result == null) return;
        if(!isResultItemInHashMap(result)) return;
        broadcast("Removing items with shift click...");
        broadcast("result: " + result);
        inv.setResult(null); // Para que no se cree el item en el inventario
        List<ItemStack> items = cacheItems(inv);
        int[] invMatrixCopy = new int[inv.getMatrix().length];
        int cuantosPuedoCraftear = 0;
        for(int i = 0; i < invMatrixCopy.length; i++) {
            ItemStack copy = items.get(i);
            if(copy == null) continue;
            broadcast("Item: " + copy.getType() + " Amount: " + copy.getAmount());
            ItemStack item = getItemOfIngredient(result, copy, i);
            if(item == null) continue;
            broadcast("Item to ingredient: " + item.getType() + " Amount: " + item.getAmount());
            int costeDeCrafteo = item.getAmount();
            broadcast("Coste de crafteo: " + costeDeCrafteo);
            cuantosPuedoCraftear = (int) Math.floor( (double) copy.getAmount() / item.getAmount() );
            broadcast("Cuantos puedo craftear: " + cuantosPuedoCraftear);
            int cuantosPuedoAgregar = getAmountForAddItem(target, result);
            broadcast("Cuantos puedo agregar: " + cuantosPuedoAgregar);

            if(cuantosPuedoAgregar == 0){
                // Si no tengo espacio para agregar el item
                // no se craftea nada
                target.sendMessage("No tienes espacio suficiente para agregar el item haciendo shitclick");
                inv.setResult(result);
                return;
            }

            if(cuantosPuedoAgregar < cuantosPuedoCraftear){
                // Si la cantidad de items que puedo agregar
                // es menor al que puedo craftear, no se
                broadcast("No tengo espacio suficiente");
                cuantosPuedoCraftear = cuantosPuedoAgregar;
            }

            int remove = cuantosPuedoCraftear * costeDeCrafteo;
            broadcast("Coste total: " + remove);

            broadcast("Remove: " + remove);
            if (remove <= 0)
                inv.setItem(i, null);
            else
                inv.getMatrix()[i].setAmount(copy.getAmount() - remove);
        }
        ItemStack addItem = result.clone();
        addItem.setAmount(cuantosPuedoCraftear);
        broadcast("Adding item: " + addItem.getType() + " Amount: " + addItem.getAmount());
        target.getInventory().addItem(addItem);
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
