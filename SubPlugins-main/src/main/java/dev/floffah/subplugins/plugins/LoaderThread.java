package dev.floffah.subplugins.plugins;

import dev.floffah.subplugins.SubPlugins;

public class LoaderThread implements Runnable {
    public PluginManager pluginManager;
    SubPlugins main;

    public LoaderThread(SubPlugins main) {
        this.main = main;
    }

    @Override
    public void run() {
        pluginManager = new PluginManager(main);
        pluginManager.loadAll();
    }
}
