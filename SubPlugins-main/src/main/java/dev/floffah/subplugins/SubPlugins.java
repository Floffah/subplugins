package dev.floffah.subplugins;

import dev.floffah.subplugins.plugins.LoaderThread;
import org.bukkit.event.EventHandler;
import org.bukkit.plugin.java.JavaPlugin;

public final class SubPlugins extends JavaPlugin {
    LoaderThread loader;

    @Override
    public void onLoad() {
        getLogger().info("Loading sub plugins...");
        loader = new LoaderThread(this);
        loader.run();
    }

    @Override
    public void onEnable() {
        loader.pluginManager.enableAll();
    }

    @Override
    public void onDisable() {
        loader.pluginManager.disableAll();
        loader.pluginManager.unloadAll();
    }
}
