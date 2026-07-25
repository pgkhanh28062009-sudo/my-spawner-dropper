package com.example.addon;

import com.example.addon.modules.SpawnerDropper;
import meteordevelopment.meteorclient.addons.MeteorAddon;
import meteordevelopment.meteorclient.systems.modules.Category;
import meteordevelopment.meteorclient.systems.modules.Modules;

public class AddonTemplate extends MeteorAddon {
    public static final Category CATEGORY = new Category("Spawner Module");

    @Override
    public void onInitialize() {
        Modules.get().add(new SpawnerDropper(CATEGORY));
    }

    @Override
    public String getPackage() {
        return "com.example.addon";
    }
}
