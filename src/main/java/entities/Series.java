package entities;

import java.util.ArrayList;

public class Series {

    private ArrayList<Game> games;
    private int t0score;
    private int t1score;

    public Series() {
        games = new ArrayList<>();
        t0score = t1score = 0;
    }

    public void addGame(Game game) {
        games.add(game);
    }

    public int getTeam0Score() {
        Team t0;

        for (Game game : games) {
            t0 = game.getTeams()[0];
            System.out.println("t0 game winner: " + t0.equals(game.getWinner()));
            if (t0.equals(game.getWinner())) {
                t0score++;
            }
        }
        return t0score;
    }

    public int getTeam1Score() {
        Team t1;

        for (Game game : games) {
            t1 = game.getTeams()[1];
            System.out.println("t1 game winner: " + t1.equals(game.getWinner()));
            if (t1.equals(game.getWinner())) {
                t1score++;
            }
        }
        return t1score;
    }

    public int getWinner() {
        if (t0score > t1score) return 0;
        else return 1;
    }

    public ArrayList<Game> getGames() {
        return games;
    }
}
