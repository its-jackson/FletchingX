package scripts;

import scripts.api.nodes.Node;

import java.util.ArrayList;
import java.util.List;

public class FletchingXVariables {

    private static final FletchingXVariables instance = new FletchingXVariables();

    private FletchingXSettings settings = new FletchingXSettings();
    private List<Node> nodes = new ArrayList<>();
    private List<Integer> waitTimes = new ArrayList<>();

    private boolean start;

    public FletchingXVariables() {}

    public static FletchingXVariables get() {
        return instance;
    }

    public FletchingXSettings getSettings() {
        return settings;
    }

    public void setSettings(FletchingXSettings settings) {
        this.settings = settings;
    }

    public List<Node> getNodes() {
        return nodes;
    }

    public void setNodes(List<Node> nodes) {
        this.nodes = nodes;
    }

    public List<Integer> getWaitTimes() {
        return waitTimes;
    }

    public void setWaitTimes(List<Integer> waitTimes) {
        this.waitTimes = waitTimes;
    }

    public boolean isStart() {
        return start;
    }

    public void setStart(boolean start) {
        this.start = start;
    }

    @Override
    public String toString() {
        return "FletchingXVariables{" +
                "settings=" + settings +
                ", nodes=" + nodes +
                ", waitTimes=" + waitTimes +
                ", start=" + start +
                '}';
    }
}
