package parser;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.regex.*;

import entities.Player;
import entities.Team;

public class Parser {

    private String replayName;
    private Team team0;
    private Team team1;
    private int lastEndedWith;
    private boolean statsCollected;


    private static String[] KEYWORDS = {
            "UnfairTeamSize", // 0
            "TeamSize", // 1
            "Team1Score", // 2
            "Team0Score", // 3
            "PlayerStats", // 4
            "Name", // 5
            "OnlineID", // 6
            "Team", // 7
            "Score", // 8
            "Goals", // 9
            "Assists", // 10
            "Saves", // 11
            "Shots", // 12
            "ReplayName" // 13
    };
    private static String[] DATA_TYPES = {
            "IntProperty",
            "StrProperty"
    };
    private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();

    public Parser(File file) {
        replayName = null;
        team0 = null;
        team1 = null;
        lastEndedWith = -1;
        statsCollected = false;

        parse(file);
    }

    public Parser(String filename) {
        this(new File(filename));
    }

    private void parse(File file) {

        try {

            byte[] content = Files.readAllBytes(file.toPath());
            String streamedStr = "";
//			String content = bytesToHex(Files.readAllBytes(file.toPath()));
//			System.out.println(content.length());
//			for (int i = 1; i < 3201; i++) {
//				System.out.print(content.charAt(i-1));
//				if (i % 4 == 0) System.out.print(" ");
//				if (i % 32 == 0) System.out.print("\n");
//			}

            // parse through the header first
            // always start with TeamSize
            int i = 0;
            while (!statsCollected) { //100 will soon become the header size
                streamedStr += (char) content[i];
//				System.out.println(lastEndedWith + ": " + streamedStr);

                for (int j = 0; j < KEYWORDS.length; j++) {
                    if (streamedStr.endsWith(KEYWORDS[j])) {
                        lastEndedWith = j;
                        break;
                    }
                }
//				System.out.println(streamedStr);

                if (streamedStr.endsWith("IntProperty")) {
                    int[] arr = getIntProperty(i, content);
                    i += arr[1];

                    switch (lastEndedWith) {
                        case 0:
							System.out.println("Unfair team size: " + arr[0]);
                            break;

                        case 1: // teamsize
							System.out.println("Team size: " + arr[0]);
                            team0 = new Team(arr[0]);
                            team1 = new Team(arr[0]);
                            break;

                        case 2: // team1score
							System.out.println("Team 1 score: " + arr[0]);
                            team1.setGoals(arr[0]);
                            break;

                        case 3: // team0score
							System.out.println("Team 2 score: " + arr[0]);
                            team0.setGoals(arr[0]);
                            break;

                        default:
                            // who cares
                            break;
                    }
//					System.out.println("Byte: " + i);
                    lastEndedWith = -1;
                    streamedStr = "";
                }

                if (streamedStr.endsWith("ArrayProperty")) {
//					System.out.println(lastEndedWith);
                    // skipping highlights
                    if (lastEndedWith == 9) { // Goals
                        // maybe add stuff here later, but for now, SKIP
                        lastEndedWith = -1;
                        streamedStr = "";

                    } else if (lastEndedWith == 4) { //PlayerStats
                        boolean playerStatsOver = false;
                        streamedStr = "";
                        int index = i;
                        int players = team0.getTeamSize() + team1.getTeamSize();
//						System.out.println("Num of players: " + players);

                        while (!playerStatsOver) { //marks the end of playerstats

                            Player player = new Player();
                            while (!streamedStr.endsWith("None")) { // do this teamsize * 2 times
//                                System.out.println(streamedStr);
//                                System.out.println(playerStatsOver);

//                                streamedStr = "";
                                index++;
                                streamedStr += (char) content[index];

                                if (streamedStr.endsWith("ReplayName") || streamedStr.endsWith("ReplayVersion")) { // exit condition
//                                    System.out.println("here");
                                    player = null;
                                    playerStatsOver = true;
                                    break;
                                }

                                // look for keywords again
                                for (int j = 0; j < KEYWORDS.length; j++) {
                                    if (streamedStr.endsWith(KEYWORDS[j])) {
                                        lastEndedWith = j;
                                        streamedStr = "";
                                    }
                                }



                                if (streamedStr.endsWith("StrProperty")) {
                                    Object[] arr = getStrProperty(index, content);
                                    index += (int) arr[1];
                                    switch (lastEndedWith) {
                                        case 5: // name
                                            player.setPlayerName(arr[0].toString());
											System.out.println("\nName: " + arr[0]);
                                            break;
                                    }


                                } else if (streamedStr.endsWith("IntProperty")) {
                                    int[] arr = getIntProperty(index, content);
                                    index += arr[1];

                                    switch (lastEndedWith) {
                                        case 7: // team
                                            player.setTeam(arr[0]);
											System.out.println("Team: " + arr[0]);
                                            break;
                                        case 8: // score
                                            player.setScore(arr[0]);
											System.out.println("Score: " + arr[0]);
                                            break;
                                        case 9: // goals
                                            player.setGoals(arr[0]);
											System.out.println("Goals: " + arr[0]);
                                            break;
                                        case 10: // assists
                                            player.setAssists(arr[0]);
											System.out.println("Assists: " + arr[0]);
                                            break;
                                        case 11: // saves
                                            player.setSaves(arr[0]);
											System.out.println("Saves: " + arr[0]);
                                            break;
                                        case 12: // shots
                                            player.setShots(arr[0]);
											System.out.println("Shots: " + arr[0]);
                                            break;

                                    }
//									System.out.println("Byte: " + index);
                                    lastEndedWith = -1;
                                    streamedStr = "";
                                }
                            }
//                            playerStatsOver = streamedStr.endsWith("ReplayName");
//                            playerStatsOver = streamedStr.endsWith("ReplayName");
//                            System.out.println(playerStatsOver);
                            i = index;
                            index++;
                            streamedStr = "";


                            if (player != null) {
//                                System.out.println(player.getTeam());
                                if (player.getTeam() == 0) team0.addPlayer(player);
                                else if (player.getTeam() == 1) team1.addPlayer(player);
                                else throw new NullPointerException("Tried to add player to a team that doesn't exist!");
                            }
                            System.out.println(playerStatsOver);
                            statsCollected = true;
//							System.out.println(streamedStr);
                        }

                        // team stats
                        Team[] teamarr = {team0, team1};

                        for (int y = 0; y < teamarr.length; y++) { // 2 teams
                            int teamAssists = 0;
                            int teamSaves = 0;
                            int teamShots = 0;

                            for (int z = 0; z < teamarr[y].getTeamSize(); z++) {
                                if (teamarr[y].getPlayers()[z] != null) {
                                    teamAssists += teamarr[y].getPlayers()[z].getAssists();
                                    teamSaves += teamarr[y].getPlayers()[z].getSaves();
                                    teamShots += teamarr[y].getPlayers()[z].getShots();
                                }
                            }
                            teamarr[y].setAssists(teamAssists);
                            teamarr[y].setSaves(teamSaves);
                            teamarr[y].setShots(teamShots);
                        }

                    }
                }
                i++;
            }
//			System.out.println(streamedStr);


        } catch (IOException e) {
            System.err.println("\nMaybe the file is missing?  Generic IOException");
            e.printStackTrace();
        }

    }

    private int[] getIntProperty(int index, byte[] content) {
        int advance = 0; // advance the counter on return
        index++;
        int offset = 0;
        // stream bytes until word offset is reached
        while (index < content.length && content[index] == 0) {
//			System.out.println(content[index] + "\n" + index);
            index++;
            offset++;
        }

        advance += (offset + content[index] * 2); // compute total counter advance
        index += content[index] * 2; // move local counter to the intproperty field
        byte[] temp = {content[index], content[index+1], content[index + 2], content[index + 3]}; // int is 4 bytes
        int intproperty = Integer.parseInt(bytesToHex(temp), 16); // converts little endian hex string to int
        index+=4;
//		System.out.println(index);
        int[] out = {intproperty, advance};
        return out; // return inproperty field and counter advance
    }

    private Object[] getStrProperty(int index, byte[] content) { //TODO make this more robust - doesn't capture name correctly if garbage binary happens to be ASCII char
        index++;
        boolean strOver = false;
        boolean foundAscii = false;
        int advance = 1;
        String name = "";
        // advance until we find ASCII characters
        while (!foundAscii) {
            foundAscii = Pattern.matches("[\\x20-\\x7F]", "" + (char)content[index]);
            index++;
            advance++;
        }
        index--; // go back to first location of ASCII text
        advance--;
        // advance until ASCII becomes zeros
        while (!strOver) {
            strOver = Pattern.matches("[^\\x20-\\x7F]", "" + (char)content[index]);
            name += (char)content[index];
            index++;
            advance++;
        }
        name = name.substring(0, name.length() - 1);
        Object[] out = {name, advance};
        return out;
    }

    // still broken
    public String getReplayName() {
        return replayName;
    }

    public Team getTeam0() {
        return team0;
    }

    public Team getTeam1() {
        return team1;
    }

    private static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return toLittleEndian(new String(hexChars));
    }

    private static String toLittleEndian(final String hex) {
        String hexLittleEndian = "";
        if (hex.length() % 2 != 0) return "0";
        for (int i = hex.length() - 2; i >= 0; i -= 2) {
            hexLittleEndian += hex.substring(i, i + 2);
        }
        return hexLittleEndian;
    }

}
