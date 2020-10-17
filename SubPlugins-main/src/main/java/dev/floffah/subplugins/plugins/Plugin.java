package dev.floffah.subplugins.plugins;

public class Plugin {
    private PluginInformation plinf;

    public Plugin() {

    }

    public void setInf(PluginInformation i) {
        this.plinf = i;
    }

    public PluginInformation getInformation() {
        return plinf;
    }
}
