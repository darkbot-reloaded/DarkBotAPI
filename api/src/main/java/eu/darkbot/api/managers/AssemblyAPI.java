package eu.darkbot.api.managers;

import eu.darkbot.api.API;

import java.util.List;

/**
 * Provide access to Assembly data
 */
public interface AssemblyAPI extends API.Singleton {
    int getSelectedRecipeIndex();

    Recipe getSelectedRecipe();

    CheckBox getFilterDropdown();

    List<? extends Recipe> getRecipes();

    List<? extends RowSetting> getRowSettings();

    interface Recipe {
        ItemVo getItemVo();

        List<? extends ItemVo> getRewards();

        List<? extends ResourceRequired> getResourcesRequired();

        boolean getIsCraftable();
    }

    interface ItemVo {
        String getLootId();
    }

    interface ResourceRequired {
        ItemVo getItemVo();

        double getAmountRequired();

        double getAmountRequiredBackup();
    }

    interface CheckBox {
        boolean getIsChecked();
    }

    interface RowSetting {
        RowEntryVO getFirst();

        RowEntryVO getSecond();
    }

    interface RowEntryVO {
        String getFilter();

        CheckBox getCheckBox();

    }
}
