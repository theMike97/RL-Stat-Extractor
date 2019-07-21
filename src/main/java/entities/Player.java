package entities;

public class Player {

    private String name;
    private int team;
    private int score;
    private int goals;
    private int assists;
    private int saves;
    private int shots;

    public Player() {
        name = null;
        team = 0;
        score = 0;
        goals = 0;
        assists = 0;
        saves = 0;
        shots = 0;
    }

    public Player(String name) {
        this();
        this.name = name;
    }

    public Player(String name, int goals, int assists, int saves, int shots) {
        this.name = name;
        this.goals = goals;
        this.assists = assists;
        this.saves = saves;
        this.shots = shots;
    }

    public void setPlayerName(String name) {
        this.name = name;
    }

    public void setTeam(int team) {
        this.team = team;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setGoals(int goals) {
        this.goals = goals;
    }

    public void setAssists(int assists) {
        this.assists = assists;
    }

    public void setSaves(int saves) {
        this.saves = saves;
    }

    public void setShots(int shots) {
        this.shots = shots;
    }

    public String getPlayerName() {
        return name;
    }

    public int getTeam() {
        return team;
    }

    public int getScore() {
        return score;
    }

    public int getGoals() {
        return goals;
    }

    public int getAssists() {
        return assists;
    }

    public int getSaves() {
        return saves;
    }

    public int getShots() {
        return shots;
    }

    public boolean isEmpty() {
        return name != null;
    }

    @Override
    public String toString() {
        return "[ Name: " + name + ", " +
                "Score: " + score + ", " +
                "Goals: " + goals + ", " +
                "Assists: " + assists + ", " +
                "Saves: " + saves + ", " +
                "Shots: " + shots + " ]";
    }
}
