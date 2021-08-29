package eu.darkbot.impl.galaxy;

import eu.darkbot.api.game.galaxy.GateInfo;
import eu.darkbot.util.XmlUtils;
import org.w3c.dom.Element;

import java.util.Optional;

public class GateInfoImpl implements GateInfo {
    private int total;
    private int current;
    private int totalWave;
    private int currentWave;
    private int livesLeft;
    private int lifePrice;
    private int multiplier;

    private BonusRewardImpl bonusReward;

    void update(Element e) {
        setTotal(XmlUtils.attrToInt(e, "total"));
        setCurrent(XmlUtils.attrToInt(e, "current"));
        setTotalWave(XmlUtils.attrToInt(e, "totalWave"));
        setCurrentWave(XmlUtils.attrToInt(e, "currentWave"));
        setLivesLeft(XmlUtils.attrToInt(e, "livesLeft"));
        setLifePrice(XmlUtils.attrToInt(e, "lifePrice"));

        if (XmlUtils.hasChildElements(e, "bonusGGReward")) {
            if (bonusReward == null) bonusReward = new BonusRewardImpl();
            bonusReward.update(XmlUtils.getChildElement(e, "bonusGGReward"));

        } else bonusReward = null;
    }

    void onGatePrepare() {
        this.current = 0;
    }

    private void setTotal(Integer total) {
        if (total != null) this.total = total;
    }

    private void setCurrent(Integer current) {
        if (current != null) this.current = current;
    }

    @Override
    public boolean isOnMap() {
        return getLivesLeft() > 0;
    }

    @Override
    public int getTotalParts() {
        return total;
    }

    @Override
    public int getCurrentParts() {
        return current;
    }

    @Override
    public int getTotalWave() {
        return totalWave;
    }

    private void setTotalWave(Integer totalWave) {
        if (totalWave != null) this.totalWave = totalWave;
    }

    @Override
    public int getCurrentWave() {
        return currentWave;
    }

    private void setCurrentWave(Integer currentWave) {
        if (currentWave != null) this.currentWave = currentWave;
    }

    @Override
    public int getLivesLeft() {
        return livesLeft;
    }

    private void setLivesLeft(Integer livesLeft) {
        if (livesLeft != null) this.livesLeft = livesLeft;
    }

    @Override
    public int getLifePrice() {
        return lifePrice;
    }

    private void setLifePrice(Integer lifePrice) {
        if (lifePrice != null) this.lifePrice = lifePrice;
    }

    @Override
    public int getMultiplier() {
        return multiplier;
    }

    void setMultiplier(Integer multiplier) {
        if (multiplier != null) this.multiplier = multiplier;
    }

    @Override
    public Optional<BonusReward> getBonusReward() {
        return Optional.ofNullable(bonusReward);
    }

    private static class BonusRewardImpl implements BonusReward {
        private boolean claimed;
        private int amount, countdown;
        private String lootId;

        private void update(Element e) {
            Integer temp;
            if ((temp = XmlUtils.attrToInt(e, "claimed")) != null)
                claimed = temp == 1;

            if ((temp = XmlUtils.attrToInt(e, "amount")) != null)
                amount = temp;

            if ((temp = XmlUtils.attrToInt(e, "countdown")) != null)
                countdown = temp;

            String lootId = e.getAttribute("lootId");
            if (lootId != null) this.lootId = lootId;
        }

        @Override
        public boolean isClaimed() {
            return claimed;
        }

        @Override
        public int getAmount() {
            return amount;
        }

        @Override
        public int getCountdown() {
            return countdown;
        }

        @Override
        public String getLootId() {
            return lootId;
        }
    }
}
