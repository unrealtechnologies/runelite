package net.runelite.client.plugins.wildernesstools;

import com.google.inject.Provides;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.Varbits;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.GameTick;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.Notifier;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;
import javax.inject.Inject;

@PluginDescriptor(
        name = "Wilderness Tools",
        description = "A plugin to provide extra tools for the wilderness",
        tags = {"wilderness", "tools", "pvp"}
)

@Slf4j
public class WildernessToolsPlugin extends Plugin {
    @Inject
    private Client client;

    @Inject
    private ClientThread clientThread;
    @Inject
    private WildernessToolsConfig config;

    @Inject
    private Notifier notifier;

    @Inject
    private OverlayManager overlayManager;
    @Inject
    private WildernessOverlay wildernessToolsOverlay;

    @Provides
    WildernessToolsConfig getConfig(ConfigManager configManager) {
        return configManager.getConfig(WildernessToolsConfig.class);
    }

    private boolean wildernessAlertOverlayOpened = false;


    public boolean isInWilderness() {
        return client.getVarbitValue(Varbits.IN_WILDERNESS) == 1;
    }

    public String wildernessLevel() {
        final Widget wildernessLevelWidget = client.getWidget(WidgetInfo.PVP_WILDERNESS_LEVEL);
        if (wildernessLevelWidget == null) {
            return "";
        }
        return wildernessLevelWidget.getText();
    }

    WildernessRangeTuple wildernessRange = new WildernessRangeTuple();

    @Subscribe
    public void onGameTick(GameTick event) {
        if (isInWilderness()) {
            wildernessRange.updateRange(wildernessLevel());
            System.out.println(wildernessRange.minCMB);
            System.out.println(wildernessRange.maxCMB);

            wildernessAlertOverlayOpened = true;
            overlayManager.add(wildernessToolsOverlay);
        } else if (wildernessAlertOverlayOpened) {
            wildernessAlertOverlayOpened = false;
            overlayManager.remove(wildernessToolsOverlay);
        }
    }
}
