package scripts.api.works;

import org.tribot.script.sdk.Skill;
import org.tribot.script.sdk.Tribot;

/**
 * Purpose of class: Cache all important information relating to the worker
 * (used for online database/leaderboard)
 */

public class Worker {

    private final static Worker instance = new Worker();
    private final static String user_name = Tribot.getUsername();

    private long resourceCount;
    private int levelCount;
    private int fletchingStartExperience;
    private int magicStartExperience;
    private int experienceGained;
    private int totalProfit;

    private Worker() {}

    public int calculateFletchingExperienceGained() {
        if (getFletchingStartExperience() > 0) {
            return Skill.FLETCHING.getXp() - getFletchingStartExperience();
        }
        return 0;
    }

    public int calculateMagicExperienceGained() {
        if (getMagicStartExperience() > 0) {
            return Skill.MAGIC.getXp() - getMagicStartExperience();
        }
        return 0;
    }

    public void incrementResourceCount() {
        this.resourceCount++;
    }

    public void incrementLevelCount() {
        this.levelCount++;
    }

    public static Worker get() {
        return instance;
    }

    public static String getUserName() {return user_name;}

    public long getResourceCount() {
        return resourceCount;
    }

    public void setResourceCount(long resourceCount) {
        this.resourceCount = resourceCount;
    }

    public int getLevelCount() {
        return levelCount;
    }

    public void setLevelCount(int levelCount) {
        this.levelCount = levelCount;
    }

    public int getFletchingStartExperience() {
        return fletchingStartExperience;
    }

    public void setFletchingStartExperience(int fletchingStartExperience) {
        this.fletchingStartExperience = fletchingStartExperience;
    }

    public int getMagicStartExperience() {
        return magicStartExperience;
    }

    public void setMagicStartExperience(int magicStartExperience) {
        this.magicStartExperience = magicStartExperience;
    }

    public int getExperienceGained() {
        return experienceGained;
    }

    public void setExperienceGained(int experienceGained) {
        this.experienceGained = experienceGained;
    }

    public int getTotalProfit() {
        return totalProfit;
    }

    public void setTotalProfit(int totalProfit) {
        this.totalProfit = totalProfit;
    }

    @Override
    public String toString() {
        return "Worker{" +
                "resourceCount=" + resourceCount +
                ", levelCount=" + levelCount +
                ", fletchingStartExperience=" + fletchingStartExperience +
                ", magicStartExperience=" + magicStartExperience +
                ", experienceGained=" + experienceGained +
                ", totalProfit=" + totalProfit +
                '}';
    }
}
