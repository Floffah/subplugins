package dev.floffah.subplugins.plugins;

public class SubPlugin {
    private PluginInformation plinf;

    public SubPlugin() {

    }

    public void onLoad() {

    }

    public void onEnable() {

    }

    public void onDisable() {

    }

    public void onUnload() {

    }

    public void setInf(PluginInformation i) {
        this.plinf = i;
    }

    public PluginInformation getInformation() {
        return plinf;
    }
}
