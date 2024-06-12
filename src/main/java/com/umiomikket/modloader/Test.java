package com.umiomikket.modloader;

public class Test {
    public static void main(String[] args) {
        ModLoader.loadMod("mods/mod.jar");

        System.out.println("Now we start this mod :)");
        ModLoader.getMods().values().forEach(modClass -> modClass.init());
    }
}
