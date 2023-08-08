package net.runelite.client.plugins.wildernesstools;

import net.runelite.api.Client;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;

import javax.inject.Inject;
import java.awt.*;

public class WildernessOverlay extends Overlay {
    @Inject
    private Client client;

    WildernessOverlay() {
        this.setPosition(OverlayPosition.DYNAMIC);
        this.setLayer(OverlayLayer.ALWAYS_ON_TOP);
    }

    @Override
    public Dimension render(Graphics2D graphics) {
        final Color color = graphics.getColor();
        graphics.setColor(notificationFlashColor());
        graphics.fillRect(0, 0, client.getCanvasWidth(), client.getCanvasHeight());
        return null;
    }

    Color notificationFlashColor() {
        return new Color(255, 0, 0, 70);
    }
}
