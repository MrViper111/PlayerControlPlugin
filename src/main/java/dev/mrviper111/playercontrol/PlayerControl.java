package dev.mrviper111.playercontrol;

import dev.mrviper111.playercontrol.commands.CloseControlLinkCommand;
import dev.mrviper111.playercontrol.commands.ControlPlayerCommand;
import dev.mrviper111.playercontrol.menus.utils.GuiManager;
import dev.mrviper111.playercontrol.utils.ControlManager;
import dev.mrviper111.playercontrol.utils.Logger;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

public final class PlayerControl extends JavaPlugin {

    private PlayerControl playerControl;
    private ControlManager controlManager;
    private GuiManager guiManager;

    private Map<UUID, UUID> controllingPlayers;
    private Map<UUID, UUID> linkedPlayers;

    private Set<UUID> jitteringPlayers;
    private Map<UUID, BukkitTask> jitteringTasks;

    private Set<UUID> frozenPlayers;

    @SuppressWarnings("ConstantConditions")
    @Override
    public void onEnable() {
        Logger.logInfo("Registering commands...");
        getCommand("controlplayer").setExecutor(new ControlPlayerCommand(this));
        getCommand("closecontrollink").setExecutor(new CloseControlLinkCommand(this));

        Logger.logInfo("Loading control manager...");
        this.controlManager = new ControlManager(this);
        this.linkedPlayers = new HashMap<>();

        Logger.logInfo("Loading GUIs...");
        this.guiManager = new GuiManager(this);

        this.controllingPlayers = new HashMap<>();

        this.jitteringPlayers = new HashSet<>();
        this.jitteringTasks = new HashMap<>();

        this.frozenPlayers = new HashSet<>();

        Logger.logInfo("Plugin enabled!");
    }

    @Override
    public void onDisable() {
        Logger.logInfo("Plugin disabled.");
    }


    public PlayerControl getPlayerControl() {
        return this.playerControl;
    }

    public ControlManager getControlManager() {
        return this.controlManager;
    }

    public GuiManager getGuiManager() {
        return this.guiManager;
    }

    public Set<UUID> getJitteringPlayers() {
        return this.jitteringPlayers;
    }

    public Map<UUID, BukkitTask> getJitteringTasks() {
        return this.jitteringTasks;
    }

    public Set<UUID> getFrozenPlayers() {
        return this.frozenPlayers;
    }

    public Map<UUID, UUID> getControllingPlayers() {
        return this.controllingPlayers;
    }

    public Map<UUID, UUID> getLinkedPlayers() {
        return this.linkedPlayers;
    }

}
