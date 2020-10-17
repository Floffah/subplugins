package dev.floffah.subplugins.plugins;

import dev.floffah.subplugins.SubPlugins;
import dev.floffah.subplugins.errors.InvalidDescriptionException;
import dev.floffah.subplugins.errors.InvalidSubPluginException;
import org.apache.commons.lang.Validate;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class PluginManager {
    SubPlugins main;
    File baseDir = new File("subplugins/");
    List<File> jars;
    URL[] urls;

    List<String> disabled = new ArrayList<>();

    Map<String, SubPlugin> plugins = new HashMap<>();;

    URLClassLoader childLoader;

    public PluginManager(SubPlugins main) {
        this.main = main;
    }

    public void loadAll() {
        plugins = new HashMap<>();

        if(!baseDir.exists()) {
            baseDir.mkdir();
        }
        jars = Arrays.asList(baseDir.listFiles());
        urls = new URL[jars.size() + 1];
        for(int i = 0; i < jars.size(); i++) {
            try {
                urls[i] = jars.get(i).toURI().toURL();
            } catch (Exception e) {
                main.getLogger().severe("Could get url of file " + jars.get(i).getName());
                e.printStackTrace();
            }
        }
        urls[jars.size()] = getClass().getProtectionDomain().getCodeSource().getLocation();
        childLoader = new URLClassLoader(urls, ClassLoader.getSystemClassLoader());
        childLoader = new URLClassLoader(urls, PluginManager.class.getClassLoader());
        Thread.currentThread().setContextClassLoader(childLoader);
        for(int i = 0; i < jars.size(); i++) {
            try {
                File jar = jars.get(i);
                SubPlugin plugin = loadPlugin(jar);
                plugin.onLoad();
            } catch (Exception e) {
                main.getLogger().severe("Could not load file " + jars.get(i).getName());
                e.printStackTrace();
            }
        }
    }

    public void enableAll() {
        plugins.forEach((s, subPlugin) -> {
            disabled.remove(s);
            subPlugin.onEnable();
        });
    }

    public void disableAll() {
        plugins.forEach((s, subPlugin) -> {
            disabled.add(s);
            subPlugin.onDisable();
        });
    }

    public void unloadAll() {
        plugins.forEach((s, subPlugin) -> {
            if(!disabled.contains(s)) {
                disabled.add(s);
                subPlugin.onDisable();
            }
            subPlugin.onUnload();
            disabled.remove(s);
            plugins.remove(s, subPlugin);
        });
    }

    public SubPlugin loadPlugin(File jar) throws InvalidDescriptionException, InvalidSubPluginException {

        PluginInformation plugininfo = loadPluginInf(jar);
        if(plugininfo == null) {
            main.getLogger().severe("Could not get plugin info from subplugin.yml of plugin " + jar.getName());
            return null;
        } else {
            main.getLogger().info("Loading plugin " + plugininfo.getName() + " v" + plugininfo.getVersion());
            try {
                Class<?> uncheck = Class.forName(plugininfo.getMain(), true, childLoader);
                Constructor cons = uncheck.getConstructor();
                SubPlugin plugin = (SubPlugin) cons.newInstance();
                plugin.setInf(plugininfo);
                plugin.onLoad();
                plugins.put(plugin.getInformation().getName(), plugin);
                return plugin;
            } catch(ClassNotFoundException e) {
                throw new InvalidDescriptionException("main '" + plugininfo.getMain() + "' was not found in subplugin " + jar.getName());
            } /*catch(ClassCastException e) {
                throw new InvalidSubPluginException("main '" + plugininfo.getMain() + "' does not extend SubPlugin");
            }*/ catch (IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public PluginInformation loadPluginInf(File pluginjar) throws InvalidDescriptionException {
        Validate.notNull(pluginjar, "Plugin file cannot be null");

        JarFile jar = null;
        InputStream infstr = null;

        try {
            jar = new JarFile(pluginjar);
            JarEntry info = jar.getJarEntry("subplugin.yml");
            if(info == null) {
                throw new IOException();
            }
            infstr = jar.getInputStream(info);
            return new PluginInformation(infstr);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(jar != null) {
                try {
                    jar.close();
                } catch (IOException e) {}
            }
            if(infstr != null) {
                try {
                    infstr.close();
                } catch (IOException e) {}
            }
        }
        return null;
    }
}
