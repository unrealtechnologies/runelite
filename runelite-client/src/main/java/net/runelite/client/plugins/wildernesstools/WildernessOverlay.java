package net.runelite.client.plugins.wildernesstools;

import net.runelite.api.Client;
import net.runelite.client.ui.overlay.Overlay;

import javax.inject.Inject;
import java.awt.*;

public class WildernessOverlay extends Overlay {
    @Inject
    private Client client;

    @Override
    public Dimension render(Graphics2D graphics) {
        final Color color = graphics.getColor();
        graphics.setColor(notificationFlashColor());
        graphics.fill(new Rectangle(client.getCanvas().getSize()));
        graphics.setColor(color);
        return null;
    }

    Color notificationFlashColor() {
        return new Color(255, 0, 0, 70);
    }
}
