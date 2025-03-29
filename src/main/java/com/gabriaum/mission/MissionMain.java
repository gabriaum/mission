package com.gabriaum.mission;

import com.gabriaum.mission.command.MissionCommand;
import com.gabriaum.mission.command.PartyCommand;
import com.gabriaum.mission.event.list.MissionTimeEvent;
import com.gabriaum.mission.listener.InventoryListener;
import com.gabriaum.mission.listener.MissionListener;
import com.gabriaum.mission.mission.controller.MissionController;
import com.gabriaum.mission.user.controller.UserController;
import com.gabriaum.mission.user.listener.UserListener;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public class MissionMain extends JavaPlugin {

    @Getter
    protected static MissionMain instance;

    private UserController userController;
    private MissionController missionController;

    @Override
    public void onLoad() {

        instance = this;

        setNative(true);
    }

    @Override
    public void onEnable() {

        userController = new UserController();
        missionController = new MissionController();

        getServer().getPluginCommand("mission").setExecutor(new MissionCommand());
        getServer().getPluginCommand("party").setExecutor(new PartyCommand());

        getServer().getPluginManager().registerEvents(new UserListener(), this);
        getServer().getPluginManager().registerEvents(new InventoryListener(), this);
        getServer().getPluginManager().registerEvents(new MissionListener(), this);

        getServer().getScheduler().runTaskTimer(this, () -> new MissionTimeEvent().pulse(), 0, 20);
    }
}
