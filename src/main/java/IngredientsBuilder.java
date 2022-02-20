import org.apache.commons.lang3.RandomStringUtils;

import java.util.Random;

public class IngredientsBuilder {

    String[] ingredientsList = {"61c0c5a71d1f82001bdaaa6d",
            "61c0c5a71d1f82001bdaaa6f",
            "61c0c5a71d1f82001bdaaa70",
            "61c0c5a71d1f82001bdaaa71",
            "61c0c5a71d1f82001bdaaa72",
            "61c0c5a71d1f82001bdaaa74",
            "61c0c5a71d1f82001bdaaa79"};

    private String[] ingredients;

    public IngredientsBuilder setIngredient(String[] ingredients) {
        this.ingredients = ingredients;
        return this;
    }

    public IngredientsBuilder setRandomIngredient() {
        return setIngredient(new String[] {ingredientsList[new Random().nextInt(ingredientsList.length)]});

    }

    public IngredientsBuilder setRandomInvalidIngredient() {
        final String invalidIngredient = RandomStringUtils.randomAlphabetic(20);
        String[] array = {invalidIngredient};
        return setIngredient(array);
    }

    public Ingredients build() {
        return new Ingredients(ingredients);
    }
}
