package de.obey.zedarPolls;

import de.obey.zedarPolls.data.Database;
import org.bukkit.plugin.java.JavaPlugin;

public final class ZedarPolls extends JavaPlugin {

    public static ZedarPolls getInstance() {
        return getPlugin(ZedarPolls.class);
    }

    private Database database;

    @Override
    public void onLoad() {
        super.onLoad();

        database = new Database(this);
    }

    @Override
    public void onEnable() {
        super.onEnable();
    }

    @Override
    public void onDisable() {
        super.onDisable();

        if(database != null)
            database.disconnectConnection();
    }
}
