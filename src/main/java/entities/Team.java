package entities;

public class Team {

    private String name;
    private Player[] players;
    private int size;
    private int goals;
    private int assists;
    private int saves;
    private int shots;

    public Team(int size) {
        this.size = size;
        name = null;
        players = new Player[size];
        goals = 0;
        assists = 0;
        saves = 0;
        shots = 0;
    }

    public Team(String name, Player[] players) {
        this(players.length);
        this.name = name;
        this.players = players;
    }

    public int getTeamSize() {
        return size;
    }

    public void addPlayer(Player player) {
        boolean playerAdded = false;
        int i = 0;
//        System.out.println(players.length);
        while (i < players.length && !playerAdded) {
            if (players[i] == null) {
                players[i] = player;
                playerAdded = true;
//				System.out.println(playerAdded);
            }
            i++;
        }
        if (!playerAdded) throw new IndexOutOfBoundsException("Tried to add too many players!");
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

    public String getTeamName() {
        return name;
    }

    public Player[] getPlayers() {
        return players;
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

    @Override
    public String toString() {
        return "[ Name: " + name + ", " +
                "Size: " + size + ", " +
                "Score: " + goals + ", " +
                "Player: " + players[0].getPlayerName() + ", " +
                "Player: " + players[1].getPlayerName() + ", " +
                "Player: " + players[2].getPlayerName() + " ]";
    }

}
