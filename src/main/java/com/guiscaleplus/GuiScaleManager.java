package com.guiscaleplus;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.util.Window;

public final class GuiScaleManager {
    private GuiScaleManager() {}

    /** GUI-Größe laut Videoeinstellungen (für Hotbar/HUD). */
    public static int vanillaScale(MinecraftClient mc) {
        return mc.getWindow().calculateScaleFactor(
                mc.options.getGuiScale().getValue(), mc.forcesUnicodeFont());
    }

    /** Welche GUI-Größe soll für diesen Screen gelten? (null = kein Screen = HUD) */
    public static int targetScale(MinecraftClient mc, Screen screen) {
        GuiScaleConfig c = GuiScaleConfig.INSTANCE;
        int base = vanillaScale(mc);
        if (!c.enabled || screen == null) return base;

        int wanted;
        if (screen instanceof InventoryScreen || screen instanceof CreativeInventoryScreen) {
            wanted = c.inventoryScale;
        } else if (screen instanceof HandledScreen<?>) {
            wanted = c.containerScale;
        } else {
            wanted = c.otherScale;
        }
        if (wanted <= 0) return base;
        // calculateScaleFactor begrenzt automatisch auf das, was der Bildschirm hergibt
        return mc.getWindow().calculateScaleFactor(wanted, mc.forcesUnicodeFont());
    }

    public static void apply(MinecraftClient mc, Screen screen) {
        Window w = mc.getWindow();
        int target = targetScale(mc, screen);
        if ((int) w.getScaleFactor() != target) {
            w.setScaleFactor(target);
        }
    }
}
