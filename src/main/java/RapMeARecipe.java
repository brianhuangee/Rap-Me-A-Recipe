import mccormick.Pull;
import mccormick.Recipe;

public class RapMeARecipe {

    public static void main(String[] args) {
        try {
            String recipe = Recipe.getRecipeDetails("pie");
            System.out.println(Pull.getElementData("title", recipe));
            System.out.println(Pull.getElementData("description", recipe));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
