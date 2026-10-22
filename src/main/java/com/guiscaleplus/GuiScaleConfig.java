package com.guiscaleplus;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class GuiScaleConfig {
    /** 0 = wie in den Videoeinstellungen, 1..MAX = feste GUI-Größe */
    public static final int MAX = 10;

    public boolean enabled = true;
    public int inventoryScale = 4;
    public int containerScale = 4;
    public int otherScale = 0;

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path PATH =
            FabricLoader.getInstance().getConfigDir().resolve("guiscaleplus.json");

    public static final GuiScaleConfig INSTANCE = load();

    private static GuiScaleConfig load() {
        try {
            if (Files.exists(PATH)) {
                GuiScaleConfig c = GSON.fromJson(Files.readString(PATH), GuiScaleConfig.class);
                if (c != null) return c;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        GuiScaleConfig c = new GuiScaleConfig();
        c.save();
        return c;
    }

    public void save() {
        try {
            Files.writeString(PATH, GSON.toJson(this));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
