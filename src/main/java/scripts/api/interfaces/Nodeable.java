package scripts.api.interfaces;

import org.tribot.script.sdk.Log;

public interface Nodeable extends Executable, Validatable {

    // Critical method for running the node
    @Override
    void execute();

    // Critical method for determining when to run the node
    @Override
    boolean validate();

    // Node name
    String name();

    // Logger
    default void log(Object msg) {
        Log.log(name() + " " + msg);
    }
}
