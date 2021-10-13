package scripts;

import scripts.api.works.Cutting;
import scripts.api.works.Work;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class FletchingXSettings {

    private boolean repeat;
    private boolean repeatShuffle;
    private boolean doNotRepeat;

    private String antiBanSeed;
    private boolean useAntiBanSeed;
    private boolean fatigue;
    private boolean microSleep;

    private boolean worldHop;
    private boolean worldHopRandom;
    private boolean worldHopNoResources;
    private double worldHopFactor;

    private List<Cutting> fletchCuttingWork = new LinkedList<>();
    private List<Work> work = new ArrayList<>();

    public FletchingXSettings() {}

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

    public String getAntiBanSeed() {
        return antiBanSeed;
    }

    public void setAntiBanSeed(String antiBanSeed) {
        this.antiBanSeed = antiBanSeed;
    }

    public boolean isUseAntiBanSeed() {
        return useAntiBanSeed;
    }

    public void setUseAntiBanSeed(boolean useAntiBanSeed) {
        this.useAntiBanSeed = useAntiBanSeed;
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

    public boolean isWorldHop() {
        return worldHop;
    }

    public void setWorldHop(boolean worldHop) {
        this.worldHop = worldHop;
    }

    public boolean isWorldHopRandom() {
        return worldHopRandom;
    }

    public void setWorldHopRandom(boolean worldHopRandom) {
        this.worldHopRandom = worldHopRandom;
    }

    public boolean isWorldHopNoResources() {
        return worldHopNoResources;
    }

    public void setWorldHopNoResources(boolean worldHopNoResources) {
        this.worldHopNoResources = worldHopNoResources;
    }

    public double getWorldHopFactor() {
        return worldHopFactor;
    }

    public void setWorldHopFactor(double worldHopFactor) {
        this.worldHopFactor = worldHopFactor;
    }

    public List<Cutting> getFletchCuttingWork() {
        return fletchCuttingWork;
    }

    public void setFletchCuttingWork(List<Cutting> fletchCuttingWork) {
        this.fletchCuttingWork = fletchCuttingWork;
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
                ", antiBanSeed='" + antiBanSeed + '\'' +
                ", useAntiBanSeed=" + useAntiBanSeed +
                ", fatigue=" + fatigue +
                ", microSleep=" + microSleep +
                ", worldHop=" + worldHop +
                ", worldHopRandom=" + worldHopRandom +
                ", worldHopNoResources=" + worldHopNoResources +
                ", worldHopFactor=" + worldHopFactor +
                ", fletchCuttingWork=" + fletchCuttingWork +
                ", work=" + work +
                '}';
    }
}
