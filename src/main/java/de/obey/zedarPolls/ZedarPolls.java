package de.obey.zedarPolls;

import de.obey.zedarPolls.data.Database;
import de.obey.zedarPolls.logic.PollHandler;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

@Getter
public final class ZedarPolls extends JavaPlugin {

    public static ZedarPolls getInstance() {
        return getPlugin(ZedarPolls.class);
    }

    private Database database;
    private PollHandler pollHandler;

    private ExecutorService executor;

    @Override
    public void onLoad() {
        super.onLoad();

        executor = Executors.newFixedThreadPool(4, r -> {
            final Thread t = new Thread(r);
            t.setName("ZedarPollsThread-" + t.getId());
            return t;
        });

        database = new Database(this);
        database.initiializeConnection();
    }

    @Override
    public void onEnable() {
        super.onEnable();

        pollHandler = new PollHandler(executor, database);
        pollHandler.loadPolls().thenAccept(PollHandler::loadVotes);
    }

    @Override
    public void onDisable() {
        super.onDisable();

        if(database != null)
            database.disconnectConnection();
    }
}
