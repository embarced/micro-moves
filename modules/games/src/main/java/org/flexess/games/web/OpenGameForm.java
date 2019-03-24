package org.flexess.games.web;

import javax.validation.constraints.NotNull;

public class OpenGameForm {

    @NotNull
    private String colour;

    @NotNull
    private String opponent;

    public String getColour() {
        return colour;
    }

    public String getOpponent() {
        return opponent;
    }

    public void setColour(String colour) {
        this.colour = colour;
    }

    public void setOpponent(String opponent) {
        this.opponent = opponent;
    }

    public boolean isComputerOpponent() {
        return (this.opponent.equals("computer"));
    }

    public Character getColourAsCharacter() {
        switch (this.colour) {
            case "black":
                return 'b';
            case "white":
                return 'w';
            default:
                throw new IllegalStateException("Unknown Colour");
        }
    }
}
