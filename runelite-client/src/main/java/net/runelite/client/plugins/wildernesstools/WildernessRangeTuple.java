package net.runelite.client.plugins.wildernesstools;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WildernessRangeTuple {

    private final Pattern wildernessRangeFinderPattern = Pattern.compile("Level:.*<br>(\\d*)-(\\d*)");

    public int minCMB = 3;
    public int maxCMB = 126;

    public void updateRange(String wildernessString) {
        Matcher matcher = wildernessRangeFinderPattern.matcher(wildernessString);
        ArrayList<Integer> list = new ArrayList<Integer>();
        while (matcher.find()) {
            minCMB = Integer.parseInt(matcher.group(1));
            maxCMB = Integer.parseInt(matcher.group(2));
        }
    }
}
