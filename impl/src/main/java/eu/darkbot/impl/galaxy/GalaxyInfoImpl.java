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
    private final Map<GalaxyGate, GateInfoImpl> gates = new HashMap<>();

    private int money;
    private int samples;
    private int energyCost;
    private int spinSalePercent;
    private boolean spinOnSale;
    private boolean galaxyGateDay;
    private boolean bonusRewardsDay;

    // FIXME: this method needs to be refactored to remove these
    @SuppressWarnings({"PMD.AssignmentInOperand", "PMD.CyclomaticComplexity", "PMD.NPathComplexity"})
    public void update(Document document) {
        Element rootElement = document.getDocumentElement();

        Integer tempValue;
        if ((tempValue = XmlUtils.childValueToInt(rootElement, "money")) != null)
            this.money = tempValue;

        if ((tempValue = XmlUtils.childValueToInt(rootElement, "samples")) != null)
            this.samples = tempValue;

        if ((tempValue = XmlUtils.childValueToInt(rootElement, "spinSalePercentage")) != null)
            this.spinSalePercent = tempValue;

        if ((tempValue = XmlUtils.childValueToInt(rootElement, "spinOnSale")) != null)
            this.spinOnSale = tempValue == 1;

        if ((tempValue = XmlUtils.childValueToInt(rootElement, "galaxyGateDay")) != null)
            this.galaxyGateDay = tempValue == 1;

        if ((tempValue = XmlUtils.childValueToInt(rootElement, "bonusRewardsDay")) != null)
            this.bonusRewardsDay = tempValue == 1;

        if ((tempValue = XmlUtils.valueToInt(XmlUtils.getChildElement(rootElement, "energy_cost"))) != null)
            this.energyCost = tempValue;

        if (XmlUtils.hasChildElements(rootElement, "items"))
            updateItems(rootElement);

        // Gates must be updated after items, because an item with the last part will set gate to completed
        updateGates(document.getElementsByTagName("gate"));

        if (XmlUtils.hasChildElements(rootElement, "multipliers"))
            updateMultipliers(XmlUtils.getChildElement(rootElement, "multipliers"));

        if (XmlUtils.hasChildElements(rootElement, "setup")) {
            Optional.ofNullable(XmlUtils.attrToInt(XmlUtils.getChildElement(rootElement, "setup"), "gate_id"))
                    .map(i -> getGateInfo(GalaxyGate.of(i)))
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
                        .ifPresent(galaxyGate -> getGateInfo(galaxyGate).update(gate)));
    }

    private void updateMultipliers(Element e) {
        XmlUtils.streamOf(e.getElementsByTagName("multiplier"))
                .forEach(multiplier -> Optional.ofNullable(GalaxyGate.of(multiplier.getAttribute("mode")))
                        .ifPresent(galaxyGate -> getGateInfo(galaxyGate).setMultiplier(XmlUtils.attrToInt(multiplier, "value"))));
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
        return spinSalePercent;
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
    public Map<GalaxyGate, ? extends GateInfo> getGateInfos() {
        return gates;
    }

    @Override
    public GateInfoImpl getGateInfo(GalaxyGate gate) {
        return gates.computeIfAbsent(gate, g -> new GateInfoImpl());
    }
}
