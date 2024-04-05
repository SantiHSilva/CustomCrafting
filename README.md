# ¿Por qué existe esto?

La construcción de crafteos está limitado en la creación cuando se tratan de valores más de 1.

Lo que trata de hacer este repositorio es la implementación de una clase para reemplazarlo de la API de Spigot de crafteos para solucionar este problema. Para poder implementarlo en tu plugin debes copiar la clase [BuildReciper](https://github.com/SantiHSilva/CustomCrafting/blob/master/src/main/java/carrot/dev/customrecipes/Recipes/BuildReciper.java) y registrar el evento de los crafteos de la siguiente clase [CraftingEvent](https://github.com/SantiHSilva/CustomCrafting/blob/master/src/main/java/carrot/dev/customrecipes/CraftingEvent.java)

Ejemplos de utilizar esta clase está localizado en este archivo [registerRecipes](https://github.com/SantiHSilva/CustomCrafting/blob/master/src/main/java/carrot/dev/customrecipes/Recipes/registerRecipes.java).

# ¿Puedo usarlo?

Todos pueden utilizar este sistema, lo unico que te puedo a pedir a tí es una estrella al repositorio y me referencies en tu proyecto :3
