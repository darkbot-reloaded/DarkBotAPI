package eu.darkbot.api.game.galaxy;

import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

public interface GalaxyInfo {

    @Nullable Integer getUridium();
    @Nullable Integer getFreeEnergy();
    @Nullable Integer getEnergyCost();
    @Nullable Integer getSelectedSpinAmount();
    @Nullable Integer getSpinSalePercentage();

    boolean isSpinSale();
    boolean isGalaxyGateDay();
    boolean isBonusRewardsDay();

    /**
     * On each spin list is cleared out and filled with new items.
     *
     * @return list of spin items
     */
    List<SpinItem> getSpinItems();

    Map<GalaxyGate, GateInfo> getGateInfos();

    default @Nullable GateInfo getGateInfo(GalaxyGate galaxyGate) {
        return getGateInfos().get(galaxyGate);
    }
}
