package com.apogames.game.archeologic;

public enum Won {

    MASTER(20, "won_level_master"),
    JEDI(30, "won_level_jedi"),
    PADAWAN(45, "won_level_padawan"),
    SOLVER(10000, "won_level_solver");

    private final String text;

    private final int maxPoints;

    Won(final int maxPoints, final String text) {
        this.maxPoints = maxPoints;
        this.text = text;
    }

    public int getMaxPoints() {
        return maxPoints;
    }

    public String getText() {
        return text;
    }

    public static Won getRankForPoints(int points) {
        for (Won won : Won.values()) {
            if (points < won.getMaxPoints()) {
                return won;
            }
        }

        return SOLVER;
    }
}
