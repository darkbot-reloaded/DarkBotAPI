package eu.darkbot.api.game.galaxy;

import org.jetbrains.annotations.Nullable;

import java.util.Map;

public interface GalaxyInfo {

    int getUridium();
    int getFreeEnergy();
    int getEnergyCost();
    int getSpinSalePercentage();

    boolean isSpinSale();
    boolean isGalaxyGateDay();
    boolean isBonusRewardsDay();

    Map<GalaxyGate, GateInfo> getGateInfos();

    @Nullable
    default GateInfo getGateInfo(GalaxyGate galaxyGate) {
        return getGateInfos().get(galaxyGate);
    }
}
