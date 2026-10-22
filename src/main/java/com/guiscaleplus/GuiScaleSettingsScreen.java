package com.guiscaleplus;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.text.Text;

import java.util.function.IntConsumer;

public class GuiScaleSettingsScreen extends Screen {
    private final Screen parent;

    public GuiScaleSettingsScreen(Screen parent) {
        super(Text.literal("GUI Scale Plus"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        GuiScaleConfig c = GuiScaleConfig.INSTANCE;
        int x = width / 2 - 100;
        int y = height / 4;

        addDrawableChild(ButtonWidget.builder(toggleText(c), b -> {
            c.enabled = !c.enabled;
            b.setMessage(toggleText(c));
        }).dimensions(x, y, 200, 20).build());

        addDrawableChild(new ScaleSlider(x, y + 28, "Inventar", c.inventoryScale, v -> c.inventoryScale = v));
        addDrawableChild(new ScaleSlider(x, y + 52, "Container", c.containerScale, v -> c.containerScale = v));
        addDrawableChild(new ScaleSlider(x, y + 76, "Andere Menüs", c.otherScale, v -> c.otherScale = v));

        addDrawableChild(ButtonWidget.builder(Text.literal("Fertig"), b -> close())
                .dimensions(x, y + 112, 200, 20).build());
    }

    private static Text toggleText(GuiScaleConfig c) {
        return Text.literal("Mod: " + (c.enabled ? "AN" : "AUS"));
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(textRenderer, title, width / 2, height / 4 - 20, 0xFFFFFFFF);
        context.drawCenteredTextWithShadow(textRenderer,
                Text.literal("0 = wie in den Videoeinstellungen. Gilt ab dem nächsten Öffnen."),
                width / 2, height / 4 + 142, 0xFFAAAAAA);
    }

    @Override
    public void close() {
        GuiScaleConfig.INSTANCE.save();
        client.setScreen(parent);
    }

    private static class ScaleSlider extends SliderWidget {
        private final String label;
        private final IntConsumer onChange;

        ScaleSlider(int x, int y, String label, int start, IntConsumer onChange) {
            super(x, y, 200, 20, Text.empty(), (double) start / GuiScaleConfig.MAX);
            this.label = label;
            this.onChange = onChange;
            updateMessage();
        }

        private int current() {
            return (int) Math.round(value * GuiScaleConfig.MAX);
        }

        @Override
        protected void updateMessage() {
            int v = current();
            setMessage(Text.literal(label + ": " + (v == 0 ? "wie Vanilla" : String.valueOf(v))));
        }

        @Override
        protected void applyValue() {
            onChange.accept(current());
        }
    }
}
