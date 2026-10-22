package com.guiscaleplus.mixin;

import com.guiscaleplus.GuiScaleManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.Window;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {

    // Skalierung setzen, BEVOR der neue Screen initialisiert wird
    @Inject(method = "setScreen", at = @At("HEAD"))
    private void guiscaleplus$beforeSetScreen(Screen screen, CallbackInfo ci) {
        GuiScaleManager.apply((MinecraftClient) (Object) this, screen);
    }

    // Fenstergröße geändert / Vollbild: Vanilla setzt die Skalierung zurück -> wieder korrigieren
    @Inject(method = "onResolutionChanged", at = @At("TAIL"))
    private void guiscaleplus$afterResolutionChanged(CallbackInfo ci) {
        MinecraftClient mc = (MinecraftClient) (Object) this;
        if (mc.currentScreen == null) return;
        Window w = mc.getWindow();
        int target = GuiScaleManager.targetScale(mc, mc.currentScreen);
        if ((int) w.getScaleFactor() != target) {
            w.setScaleFactor(target);
               mc.currentScreen.resize(w.getScaledWidth(), w.getScaledHeight());
        }
    }
}
