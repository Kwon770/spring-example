package tacos.data;

import tacos.Ingredient;

public interface IngredientRepository {
    Iterable<Ingredient> finaAll();
    Ingredient findById(String id);
    Ingredient save(Ingredient ingredient);
}
