package scripts.api.nodes;

import scripts.api.interfaces.Nodeable;
import scripts.api.works.Work;

public abstract class Node implements Nodeable {

    private Work work;

    public Node(Work work) {
        this.work = work;
    }

    public Node() {}

    @Override
    public synchronized void execute() {

    }

    @Override
    public synchronized boolean validate() {
        return false;
    }

    @Override
    public String name() {
        return null;
    }

    @Override
    public String toString() {
        return "Node{" +
                "work=" + work +
                '}';
    }

    public Work getWork() {
        return work;
    }

    public void setWork(Work work) {
        this.work = work;
    }
}
