package team.g3.delicacysearch.model;

import java.util.List;

public class KnowledgeGraph {
    private List<String> cuisines;
    private List<String> ingredients;

    private List<FoodTriple> lines;

    public KnowledgeGraph(List<String> cuisines, List<String> ingredients, List<FoodTriple> lines) {
        this.cuisines = cuisines;
        this.ingredients = ingredients;
        this.lines = lines;
    }

    public List<String> getCuisines() {
        return cuisines;
    }

    public void setCuisines(List<String> cuisines) {
        this.cuisines = cuisines;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    public List<FoodTriple> getLines() {
        return lines;
    }

    public void setLines(List<FoodTriple> lines) {
        this.lines = lines;
    }
}
