package scripts.api.utilities;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.tribot.script.sdk.util.ScriptSettings;
import org.tribot.script.sdk.util.serialization.RuntimeTypeAdapterFactory;
import scripts.api.works.Alchemy;
import scripts.api.works.Cutting;
import scripts.api.works.Stringing;
import scripts.api.works.Work;

public class PolymorphicScriptSettings {

    private final ScriptSettings settings;

    public PolymorphicScriptSettings() {
        this.settings = build();
    }

    private static ScriptSettings build() {
        RuntimeTypeAdapterFactory<Work> workAdapterFactory = RuntimeTypeAdapterFactory.of(Work.class)
                .registerSubtype(Cutting.class)
                .registerSubtype(Stringing.class)
                .registerSubtype(Alchemy.class);

        Gson gson = new GsonBuilder()
                .registerTypeAdapterFactory(workAdapterFactory)
                .setPrettyPrinting()
                .create();

        return ScriptSettings.builder()
                .gson(gson)
                .build();
    }

    public ScriptSettings getSettings() {
        return settings;
    }
}
