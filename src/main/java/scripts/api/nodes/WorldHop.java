package scripts.api.nodes;

import org.apache.commons.lang3.time.StopWatch;
import org.tribot.api.General;
import org.tribot.script.sdk.*;
import org.tribot.script.sdk.query.Query;
import org.tribot.script.sdk.types.Area;
import org.tribot.script.sdk.types.WorldTile;
import scripts.FletchingXSettings;
import scripts.FletchingXVariables;
import scripts.api.antiban.AntiBan;
import scripts.api.interfaces.Workable;
import scripts.api.works.Work;

import java.util.Optional;

public class WorldHop extends Node implements Workable {

    private StopWatch stopWatch = new StopWatch();
    private long worldHopTimeInterval = General.randomLong(300000, 1200000); // 5 - 20 minutes

    private final FletchingXVariables variables = FletchingXVariables.get();

    public WorldHop(Work work) {
        super(work);
    }

    public WorldHop() {
    }

    @Override
    public void execute() {
        int sleepTime = AntiBan.sleep(getVariables().getWaitTimes());
        String successful = "World hopped successful";
        String unsuccessful = "World hopped unsuccessful";

        if (MyPlayer.isMember()) {
            Worlds.getRandomMembers()
                    .ifPresent(world -> {
                        int worldNumber = world.getWorldNumber();
                        String worldHopMember = "World hopping to members world " + worldNumber;
                        log(worldHopMember);
                        getVariables().setState(worldHopMember);
                        boolean result = WorldHopper.hop(world.getWorldNumber());
                        if (result) {
                            log(successful);
                            getVariables().setState(successful);
                        } else {
                            log(unsuccessful);
                            getVariables().setState(unsuccessful);
                        }
                    });
        } else {
            Worlds.getRandomNonMembers()
                    .ifPresent(world -> {
                        int worldNumber = world.getWorldNumber();
                        String worldHopNonMember = "World hopping to non members world " + worldNumber;
                        log(worldHopNonMember);
                        getVariables().setState(worldHopNonMember);
                        boolean result = WorldHopper.hop(worldNumber);
                        if (result) {
                            log(successful);
                            getVariables().setState(successful);
                        } else {
                            log(unsuccessful);
                            getVariables().setState(unsuccessful);
                        }
                    });
        }
    }

    @Override
    public boolean validate() {
        FletchingXSettings settings = getVariables().getSettings();
        // start stop watch if not started
        if (getStopWatch().isStopped()) {
            getStopWatch().start();
        }
        // validate world hopping
        if (Login.isLoggedIn()) {
            // world hop player count
            if (settings.isWorldHopPlayerCount()) {
                Optional<WorldTile> worldTileOptional = convertRSTileToWorldTile(getWork().getBankLocation().getPosition());
                if (worldTileOptional.isPresent()) {
                    WorldTile tile = worldTileOptional.get();
                    Area area = Area.fromRadius(tile, 15);
                    int playerCount = Query.players()
                            .filter(player -> area.contains(player.getTile()))
                            .count();
                    if (playerCount >= settings.getWorldHopFactor()) {
                        return true;
                    }
                }
            }
            if (isAtBank(getWork().getBankLocation())) {
                // world hop random time
                if (settings.isWorldHopRandom()) {
                    if (getStopWatch().getTime() >= getWorldHopTimeInterval()) {
                        setWorldHopTimeInterval(General.randomLong(300000, 1200000));
                        getStopWatch().reset();
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public String name() {
        return "[World Hop Control]";
    }

    public StopWatch getStopWatch() {
        return stopWatch;
    }

    public void setStopWatch(StopWatch stopWatch) {
        this.stopWatch = stopWatch;
    }

    public long getWorldHopTimeInterval() {
        return worldHopTimeInterval;
    }

    public void setWorldHopTimeInterval(long worldHopTimeInterval) {
        this.worldHopTimeInterval = worldHopTimeInterval;
    }

    public FletchingXVariables getVariables() {
        return variables;
    }
}
