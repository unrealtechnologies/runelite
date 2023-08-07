package net.runelite.client.plugins.wildernesstools;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("wildernesstools")
public interface WildernessToolsConfig extends Config {
    @ConfigItem(
            keyName = "alertPlayerNearby",
            name = "Alert player is nearby",
            description = "Show alert is a player is nearby",
            position = 1
    )
    default boolean alertPlayerNearby() {
        return true;
    }
}
