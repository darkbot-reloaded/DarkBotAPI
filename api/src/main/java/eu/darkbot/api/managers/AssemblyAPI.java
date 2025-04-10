package eu.darkbot.api.managers;

import eu.darkbot.api.API;

import java.util.List;

/**
 * Provide access to Assembly data
 */
public interface AssemblyAPI extends API.Singleton {
    /**
     * @return index of currently selected recipe in assembly window
     */
    int getSelectedRecipeIndex();

    /**
     * @return {@code Recipe}  that is currently selected
     */
    Recipe getSelectedRecipe();

    /**
     * @return check if filter drop down window is currently open
     */
    boolean isFilterDropDownOpen();

    /**
     * @return {@code List} of all Recipe available
     */
    List<? extends Recipe> getRecipes();

    /**
     * @return {@code List} of all category of filters
     */
    List<? extends Filter> getFilters();

    /**
     * Opens assembly GUI and selects a specific recipe by index, requires calling until true
     * @param index index of the recipe to select
     * @return false if operation has not completed, true if done
     */
    boolean clickRecipeIndex(int index);

    /**
     * Opens assembly GUI and clicks the craft/collect button, requires calling until true
     * @return false if operation has not completed, true if done
     */
    boolean clickCraftCollect();

    /**
     * Provide access to Recipe data in Assembly containing id, rewards and resources required to make the item
     */
    interface Recipe {
        /**
         * @return recipe in game id
         */
        String getRecipeId();

        /**
         * @return {@code List} of Reward ids when recipe is collected
         */
        List<? extends String> getRewards();

        /**
         * @return {@code List} of Resources required to build the recipe
         */
        List<? extends ResourceRequired> getResourcesRequired();

        /**
         * @return if the recipe is craftable, false if recipe is ready to be collected
         */
        boolean isCraftable();

        /**
         * @return if the recipe is building in progress
         */
        boolean isInProgress();

        /**
         * @return if the recipe is collectable
         */
        boolean isCollectable();

        /**
         * @return the visibility data of the recipe
         */
        String getVisibility();

        /**
         * @return the time left to complete recipe craft
         */
        int getCraftTimeLeft();

        /**
         * @return the time required to craft the recipe
         */
        int getCraftTimeRequired();
    }

    /**
     * Provide access to Resources Required data in Recipe
     */
    interface ResourceRequired {
        /**
         * @return resource id for required material for recipe
         */
        String getResourceId();

        /**
         * @return amount of resource required for recipe
         */
        double getAmountRequired();
    }

    /**
     * Mapping of {@code ItemFilter} into row, col indexed
     */
    interface Filter {
        /**
         * @return name of item filter in assembly window
         */
        String getFilterName();

        /**
         * @return row index of filter
         */
        int getRow();

        /**
         * @return col index of filter
         */
        int getCol();

        /**
         * @return if the filter is applied
         */
        boolean isChecked();

        /**
         * @return the x offset location
         */
        double getX();

        /**
         * @return the y offset location
         */
        double getY();
    }

}
