package ru.eltex.entity;

public class GameMode {

    public String lore(String mode) {
        if (mode.equals("0"))
            return "";
        else /*if (mode.equals("1"))*/
            return "";
    }

    public String dayIsComing(String mode) {
        if (mode.equals("0"))
            return "Day is coming...";
        else /*if (mode.equals("1"))*/
            return "";
    }

    public String nightIsComing(String mode) {
        if (mode.equals("0"))
            return "Night is coming... Only MAFIA in chat!";
        else /*if (mode.equals("1"))*/
            return "";
    }

    public String votingBegins(String mode) {
        if (mode.equals("0"))
            return "Voting begins...";
        else /*if (mode.equals("1"))*/
            return "";
    }

    public String mafiaWon(String mode) {
        if (mode.equals("0"))
            return "Mafia won!!!";
        else /*if (mode.equals("1"))*/
            return "";
    }

    public String civilianWon(String mode) {
        if (mode.equals("0"))
            return "Civilians won!!!";
        else /*if (mode.equals("1"))*/
            return "";
    }
}
