package net.runelite.client.plugins.wildernesstools;

import com.google.inject.Provides;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.Player;
import net.runelite.api.Varbits;
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
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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

    static final int PLAYER_NUMBER_OF_TILES_AWAY = 20;

    @Subscribe
    public void onGameTick(GameTick event) {
        boolean isPlayerInDanger = false;
        if (isInWilderness()) {
            wildernessRange.updateRange(wildernessLevel());

            List<Player> players = client.getPlayers();
            Player localPlayer = client.getLocalPlayer();
            WorldPoint localPoint = localPlayer.getWorldLocation();

            List<Player> nearbyPlayers = players.stream()
                    .filter(player -> localPoint.distanceTo(player.getWorldLocation()) < PLAYER_NUMBER_OF_TILES_AWAY)
                    .collect(Collectors.toList());

            for (Player player : nearbyPlayers) {
                if (Objects.equals(player.getName(), client.getLocalPlayer().getName())) {
                    continue;
                }
                if (player.getCombatLevel() >= wildernessRange.minCMB
                        && player.getCombatLevel() <= wildernessRange.maxCMB) {
                    isPlayerInDanger = true;
                }
            }
        }
        if (isPlayerInDanger) {
            wildernessAlertOverlayOpened = true;
            overlayManager.add(wildernessToolsOverlay);
        } else if (wildernessAlertOverlayOpened) {
            wildernessAlertOverlayOpened = false;
            overlayManager.remove(wildernessToolsOverlay);
        }
    }
}
