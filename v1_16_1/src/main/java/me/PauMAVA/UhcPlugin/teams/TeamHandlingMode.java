package me.PauMAVA.UhcPlugin.teams;

public enum TeamHandlingMode {

    PRE_MADE("premade"),
    RANDOM("random"),
    IN_GAME_PROXIMITY("proximity"),
    IN_GAME_RANDOM("random_ingame");

    private final String value;

    TeamHandlingMode(String value) {
       this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static TeamHandlingMode getByValue(String value) {
        for (TeamHandlingMode mode: values()) {
            if (mode.getValue().equalsIgnoreCase(value)) {
                return mode;
            }
        }
        return null;
    }

}
