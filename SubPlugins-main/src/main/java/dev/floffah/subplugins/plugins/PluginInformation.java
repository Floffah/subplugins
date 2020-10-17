package dev.floffah.subplugins.plugins;

import dev.floffah.subplugins.errors.InvalidDescriptionException;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.Map;

public class PluginInformation {
    Yaml yaml = new Yaml();

    String name;
    String version;
    String main;

    public PluginInformation(final InputStream infstream) throws InvalidDescriptionException {
        Map<String, Object> map = yaml.load(infstream);
        loadInfo(map);
    }

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }

    public String getMain() {
        return main;
    }

    public void loadInfo(Map<String, Object> map) throws InvalidDescriptionException {
        if(map == null) {
            throw new InvalidDescriptionException("Could not load subplugin.yml.");
        }

        try {
            name = map.get("name").toString();

            if (!name.matches("^[A-Za-z0-9 _-]+$")) {
                throw new InvalidDescriptionException("name \"" + name + "\"contains invalid characters");
            }
        } catch(NullPointerException eo) {
            throw new InvalidDescriptionException(eo, "name wasnt defined in subplugin.yml");
        } catch(ClassCastException et) {
            throw new InvalidDescriptionException(et, "name is not a string or text");
        }

        try {
            version = map.get("version").toString();
        } catch(NullPointerException eo) {
            throw new InvalidDescriptionException(eo, "version wasnt defined in subplugin.yml");
        } catch(ClassCastException et) {
            throw new InvalidDescriptionException(et, "version is not a string or text");
        }

        try {
            main = map.get("main").toString();

            if (main.startsWith("dev.floffah.") && !main.startsWith("dev.floffah.subplugins.SubPluginTest")) {
                throw new InvalidDescriptionException("main may not be within the dev.floffah namespace");
            }
        } catch(NullPointerException eo) {
            throw new InvalidDescriptionException(eo, "main wasnt defined in subplugin.yml");
        } catch(ClassCastException et) {
            throw new InvalidDescriptionException(et, "main is not a string or text");
        }
    }
}
