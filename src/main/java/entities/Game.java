package entities;

public class Game {

    private Team[] teams;

    public Game() {
        teams = new Team[2];
    }

    public Game(Team t0, Team t1) {
        teams = new Team[]{t0, t1};
    }

    public void setTeam0(Team team0) {
        teams[0] = team0;
    }

    public void setTeam1(Team team1) {
        teams[1] = team1;
    }

    public Team getWinner() {
        if (teams[0].getGoals() > teams[1].getGoals()) return teams[0];
        return teams[1];
    }

    public Team[] getTeams() {
        return teams;
    }
}
