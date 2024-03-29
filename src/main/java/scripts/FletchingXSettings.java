package scripts;

import scripts.api.works.Work;

import java.util.LinkedList;
import java.util.List;

/**
 * Purpose of class: Store the scripting settings that determine core functionality
 */

public class FletchingXSettings {

    private boolean repeat;
    private boolean repeatShuffle;
    private boolean doNotRepeat;

    private boolean fatigue;
    private boolean microSleep;

    private boolean worldHopPlayerCount;
    private boolean worldHopRandom;
    private double worldHopFactor;

    private List<Work> work = new LinkedList<>();

    public FletchingXSettings() {}

    public FletchingXSettings(FletchingXSettings settings) {
        this.repeat = settings.isRepeat();
        this.repeatShuffle = settings.isRepeatShuffle();
        this.doNotRepeat = settings.isDoNotRepeat();
        this.fatigue = settings.isFatigue();
        this.microSleep = settings.isMicroSleep();
        this.worldHopPlayerCount = settings.isWorldHopPlayerCount();
        this.worldHopRandom = settings.isWorldHopRandom();
        this.worldHopFactor = settings.getWorldHopFactor();
        this.work = settings.getWork();
    }

    public boolean isRepeat() {
        return repeat;
    }

    public void setRepeat(boolean repeat) {
        this.repeat = repeat;
    }

    public boolean isRepeatShuffle() {
        return repeatShuffle;
    }

    public void setRepeatShuffle(boolean repeatShuffle) {
        this.repeatShuffle = repeatShuffle;
    }

    public boolean isDoNotRepeat() {
        return doNotRepeat;
    }

    public void setDoNotRepeat(boolean doNotRepeat) {
        this.doNotRepeat = doNotRepeat;
    }

    public boolean isFatigue() {
        return fatigue;
    }

    public void setFatigue(boolean fatigue) {
        this.fatigue = fatigue;
    }

    public boolean isMicroSleep() {
        return microSleep;
    }

    public void setMicroSleep(boolean microSleep) {
        this.microSleep = microSleep;
    }

    public boolean isWorldHopPlayerCount() {
        return worldHopPlayerCount;
    }

    public void setWorldHopPlayerCount(boolean worldHopPlayerCount) {
        this.worldHopPlayerCount = worldHopPlayerCount;
    }

    public boolean isWorldHopRandom() {
        return worldHopRandom;
    }

    public void setWorldHopRandom(boolean worldHopRandom) {
        this.worldHopRandom = worldHopRandom;
    }

    public double getWorldHopFactor() {
        return worldHopFactor;
    }

    public void setWorldHopFactor(double worldHopFactor) {
        this.worldHopFactor = worldHopFactor;
    }

    public List<Work> getWork() {
        return work;
    }

    public void setWork(List<Work> work) {
        this.work = work;
    }

    @Override
    public String toString() {
        return "FletchingXSettings{" +
                "repeat=" + repeat +
                ", repeatShuffle=" + repeatShuffle +
                ", doNotRepeat=" + doNotRepeat +
                ", fatigue=" + fatigue +
                ", microSleep=" + microSleep +
                ", worldHopPlayerCount=" + worldHopPlayerCount +
                ", worldHopRandom=" + worldHopRandom +
                ", worldHopFactor=" + worldHopFactor +
                ", work=" + work +
                '}';
    }
}
