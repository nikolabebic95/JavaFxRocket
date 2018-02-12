package object.powerups;

import util.RandomUtility;

import java.util.ArrayList;

public class PowerUpsMap {
    private static ArrayList<PowerUp> powerUps = new ArrayList<>();

    static {
        powerUps.add(new Heart());
        powerUps.add(new StormtrooperHelmet());
        powerUps.add(new R2D2());
    }

    public static PowerUp getRandom() {
        return powerUps.get(RandomUtility.getRandom().nextInt(powerUps.size())).copy();
    }
}
