package eu.darkbot.impl.galaxy;

import eu.darkbot.api.game.galaxy.GalaxyGate;
import eu.darkbot.api.game.galaxy.GateInfo;
import eu.darkbot.util.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

public class GalaxyInfoImpl implements eu.darkbot.api.game.galaxy.GalaxyInfo {
    private final SpinResultImpl spinResult = new SpinResultImpl(this);

    private final Map<GalaxyGate, GateInfo> gates = new HashMap<>();
    private final Map<SpinResultImpl.ItemType, SpinResultImpl.SpinInfoImpl> itemCache = new HashMap<>();

    private int money, samples, energyCost, spinSalePercentage;
    private boolean spinOnSale, galaxyGateDay, bonusRewardsDay;

    public void update(Document document) {
        Element rootElement = document.getDocumentElement();

        Integer tempValue;
        if ((tempValue = XmlUtils.childValueToInt(rootElement, "money")) != null)
            this.money = tempValue;

        if ((tempValue = XmlUtils.childValueToInt(rootElement, "samples")) != null)
            this.samples = tempValue;

        if ((tempValue = XmlUtils.childValueToInt(rootElement, "spinSalePercentage")) != null)
            this.spinSalePercentage = tempValue;

        if ((tempValue = XmlUtils.childValueToInt(rootElement, "spinOnSale")) != null)
            this.spinOnSale = tempValue == 1;

        if ((tempValue = XmlUtils.childValueToInt(rootElement, "galaxyGateDay")) != null)
            this.galaxyGateDay = tempValue == 1;

        if ((tempValue = XmlUtils.childValueToInt(rootElement, "bonusRewardsDay")) != null)
            this.bonusRewardsDay = tempValue == 1;

        if ((tempValue = XmlUtils.valueToInt(XmlUtils.getChildElement(rootElement, "energy_cost"))) != null)
            this.energyCost = tempValue;

        updateGates(document.getElementsByTagName("gate"));

        if (XmlUtils.hasChildElements(rootElement, "multipliers"))
            updateMultipliers(XmlUtils.getChildElement(rootElement, "multipliers"));

        if (XmlUtils.hasChildElements(rootElement, "items"))
            updateItems(rootElement);

        if (XmlUtils.hasChildElements(rootElement, "setup")) {
            Optional.ofNullable(XmlUtils.attrToInt(XmlUtils.getChildElement(rootElement, "setup"), "gate_id"))
                    .map(i -> (GateInfoImpl) getGateInfo(GalaxyGate.of(i)))
                    .ifPresent(GateInfoImpl::onGatePrepare);
        }
    }

    public SpinResultImpl getSpinResult() {
        return spinResult;
    }

    private void updateItems(Element rootElement) {
        GalaxyGate gate = GalaxyGate.of(XmlUtils.getChildElement(rootElement, "mode").getTextContent());
        if (gate == null) gate = GalaxyGate.ALPHA;

        Stream<Element> itemStream = XmlUtils.streamOf(XmlUtils.getChildElement(rootElement, "items").getElementsByTagName("item"));

        this.spinResult.update(itemStream, gate);
    }

    private void updateGates(NodeList gates) {
        XmlUtils.streamOf(gates)
                .forEach(gate -> Optional.ofNullable(XmlUtils.attrToInt(gate, "id"))
                        .map(GalaxyGate::of)
                        .ifPresent(galaxyGate ->
                                ((GateInfoImpl) this.gates.computeIfAbsent(galaxyGate, k -> new GateInfoImpl())).update(gate)));
    }

    private void updateMultipliers(Element e) {
        XmlUtils.streamOf(e.getElementsByTagName("multiplier"))
                .forEach(multiplier -> Optional.ofNullable(multiplier.getAttribute("mode"))
                        .map(GalaxyGate::of)
                        .ifPresent(galaxyGate -> ((GateInfoImpl) this.gates.computeIfAbsent(galaxyGate, k -> new GateInfoImpl()))
                                .setMultiplier(XmlUtils.attrToInt(multiplier, "value"))));
    }

    @Override
    public int getUridium() {
        return money;
    }

    @Override
    public int getFreeEnergy() {
        return samples;
    }

    @Override
    public int getEnergyCost() {
        return energyCost;
    }

    @Override
    public int getSpinSalePercentage() {
        return spinSalePercentage;
    }

    @Override
    public boolean isSpinSale() {
        return spinOnSale;
    }

    @Override
    public boolean isGalaxyGateDay() {
        return galaxyGateDay;
    }

    @Override
    public boolean isBonusRewardsDay() {
        return bonusRewardsDay;
    }

    @Override
    public Map<GalaxyGate, GateInfo> getGateInfos() {
        return gates;
    }
}
