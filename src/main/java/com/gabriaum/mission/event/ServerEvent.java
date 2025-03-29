package com.gabriaum.mission.event;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Getter
public class ServerEvent extends Event implements Cancellable {

    @Getter
    private static final HandlerList handlerList = new HandlerList();

    private boolean cancelled = false;

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        cancelled = b;
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

    public void pulse() {
        Bukkit.getServer().getPluginManager().callEvent(this);
    }
}
