package scripts.api.antiban;

import org.apache.commons.lang3.RandomStringUtils;
import org.tribot.script.sdk.antiban.PlayerPreferences;

public class Seed {

    private int preference;
    private String seed;

    public Seed(String seed) {
        this.seed = seed;
        this.preference = PlayerPreferences.preference(seed, generator -> generator.uniform(1, 100));
    }

    private Seed() {}

    public static Seed generateRandomSeed() {
        return new Seed(RandomStringUtils.randomAlphanumeric(1, 60));
    }

    public int getPreference() {
        return preference;
    }

    public void setPreference(int preference) {
        this.preference = preference;
    }

    public String getSeed() {
        return seed;
    }

    public void setSeed(String seed) {
        this.seed = seed;
    }

    @Override
    public String toString() {
        return "PlayerPreference{" +
                "preference=" + preference +
                ", seed='" + seed + '\'' +
                '}';
    }
}
