package com.umiomikket.modloader;

public class ModInfo {
    public final String name;
    public final float version;
    public final String description;
    public final String author;
    public final String website;

    public ModInfo(
        String name,
        float version,
        String description,
        String author,
        String website
    ) {
        this.name = name;
        this.version = version;
        this.description = description;
        this.author = author;
        this.website = website;
    }
}
