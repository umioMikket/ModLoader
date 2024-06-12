package com.umiomikket.modloader;

import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ModLoader {
    private static HashMap<ModInfo, ModClass> mods = new HashMap<>();

    public static void loadMod(String entry) {
        Path entryPath = Paths.get(entry);
        loadMod(entryPath);
    }

    public static void loadMod(Path entry) {
        try (JarFile jarFile = new JarFile(entry.toFile())) {
            JarEntry jsonEntry = jarFile.getJarEntry("mod.json");

            if (jsonEntry == null) {
                System.out.println("Mod file " + entry.getFileName() + ", not have mod.json in resources!");
                return;
            }

            InputStream input = jarFile.getInputStream(jsonEntry);
            JSONObject jsonObject = new JSONObject(new JSONTokener(input));

            if (!jsonObject.has("main") || jsonObject.isNull("main")) {
                System.out.println("File mod.json in resources of mod '" + entry.getFileName() + "' not have main value or key!");
                return;
            }

            ModInfo modInfo = new ModInfo(
                jsonObject.optString("name", null),
                jsonObject.optFloat("version", 0),
                jsonObject.optString("description", null),
                jsonObject.optString("author", null),
                jsonObject.optString("website", null)
            );

            System.out.println();

            String mainClassPath = jsonObject.getString("main");
            Class<?> modClass = Class.forName(mainClassPath, true, new URLClassLoader(new URL[] {entry.toUri().toURL()}));
            ModClass modClassConverted = (ModClass) modClass.cast(modClass.getConstructors()[0].newInstance());

            mods.put(modInfo, modClassConverted);

            System.out.println(String.format(
                "Mod added! Here mod info:\n - name: %s\n - version: %s\n - description: %s\n - author: %s\n - website: %s",
                modInfo.name, modInfo.version, modInfo.description, modInfo.author, modInfo.website
            ));
        } catch (Exception e) { e.printStackTrace(); }
    }

    public static void loadMods(String path) {
        Path modsDir = Paths.get(path);
        loadMods(modsDir);
    }

    public static void loadMods(Path path) {
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(path, "*.jar")) {
            for (Path entry : stream) loadMod(entry);
        } catch (Exception e) { e.printStackTrace(); }
    }

    public static HashMap<ModInfo, ModClass> getMods() { return mods; }
}