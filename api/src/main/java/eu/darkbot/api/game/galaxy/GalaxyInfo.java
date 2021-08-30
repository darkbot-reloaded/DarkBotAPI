package eu.darkbot.api.game.galaxy;

import java.util.Map;

public interface GalaxyInfo {

    int getUridium();
    int getFreeEnergy();
    int getEnergyCost();
    int getSpinSalePercentage();

    boolean isSpinSale();
    boolean isGalaxyGateDay();
    boolean isBonusRewardsDay();

    Map<GalaxyGate, ? extends GateInfo> getGateInfos();

    GateInfo getGateInfo(GalaxyGate galaxyGate);
}
