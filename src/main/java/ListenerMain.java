import entities.Game;
import entities.Series;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import parser.Parser;
import entities.Player;
import entities.Team;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ListenerMain extends ListenerAdapter {

    private static String[] TEAM_CODES = {
            "ATL", "BAL", "BUF", "CAR", "CHI", "CLE",
            "WRY", "DAL", "DET", "KCC", "LVT", "LAG",
            "MAU", "MCV", "MIA", "NSH", "NEV", "NOC",
            "NYI", "ONT", "OTT", "PHI", "PHX", "POR",
            "SAR", "SDA", "SFK", "SEA", "UTA", "WAR"
    };

    private boolean uploadingSeries;
    private ArrayList<Message.Attachment> games;
    private String team0;
    private String team1;

    public ListenerMain() {
        super();
        uploadingSeries = false;
        games = new ArrayList<>();
        team0 = team1 = "";
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        MessageChannel channel = event.getChannel();
        Message inMessage = event.getMessage();
        String command = inMessage.getContentRaw();
        User author = event.getAuthor();

        if (author.isBot()) return; // dont want to be responding to bots

        // !help
        if (command.equals("!stats help")) {
            channel.sendMessage("Below are the commands for this bot:\n" +
                    "\t1) !stats view <replay.replay>\n" +
                    "\t2) ~~!stats upload <replay.replay>~~\n" +
                    "\t3) !stats series start [AAAvBBB]\n" +
                    "\t4) !stats series end\n" +
                    "\t5) !stats help").queue();
        }
        // !stats view <replay.replay>
        else if (command.startsWith("!stats view")) {
            if (uploadingSeries) {
                channel.sendMessage("_Finish uploading series (_`!stats series end`_)_").queue();
                return;
            }

            if (event.getMessage().getAttachments().isEmpty()) {
                channel.sendMessage("Must upload .replay file").queue();
                return;
            }

            Message.Attachment attachment = event.getMessage().getAttachments().get(0);


            File file = new File(attachment.getFileName());
            attachment.download(file);

            try {
                Parser parser = new Parser(file);
                Team t0 = parser.getTeam0();
                Team t1 = parser.getTeam1();

                channel.sendMessage("**" + t0.getTeamSize() + " v " + t0.getTeamSize() + "**").queue();
                channel.sendMessage("_Get map name here maybe someday_").queue();
                channel.sendMessage("**Team: " + t0.getTeamName() + " - " + t0.getGoals() + "**").queue();
                Player player;
                for (int i = 0; i < t0.getTeamSize(); i++) {
                    player = t0.getPlayers()[i];
                    if (player != null) {
                        channel.sendMessage(player.getPlayerName() + "\n" +
                                "\t\tScore: " + player.getScore() + "\n" +
                                "\t\tGoals: " + player.getGoals() + "\n" +
                                "\t\tAssists: " + player.getAssists() + "\n" +
                                "\t\tSaves: " + player.getSaves() + "\n" +
                                "\t\tShots: " + player.getShots()).queue();
                    }
                }
                channel.sendMessage("\n**Team: " + t1.getTeamName() + " - " + t1.getGoals() + "**").queue();
                for (int i = 0; i < t1.getTeamSize(); i++) {
                    player = t1.getPlayers()[i];
                    if (player != null) {
                        channel.sendMessage(player.getPlayerName() + "\n" +
                                "\t\tScore: " + player.getScore() + "\n" +
                                "\t\tGoals: " + player.getGoals() + "\n" +
                                "\t\tAssists: " + player.getAssists() + "\n" +
                                "\t\tSaves: " + player.getSaves() + "\n" +
                                "\t\tShots: " + player.getShots()).queue();
                    }
                }
            } catch (Exception e) {
                channel.sendMessage("_Uh oh!  Some kind of error occurred while parsing your replay!_");
            }

            file.delete();
        }
        // !stats upload
        else if (command.startsWith("!stats upload")) {

        }
        // !stats series start ###v###
        /* If i can get two-way communication with google spreadsheets then I can figure out the
           team names based on the players */
        else if (command.equals("!stats series start")) {
            channel.sendMessage("_No command_ `!stats series start`").queue();
            channel.sendMessage("_Maybe you meant_ `!stats series start [AAAvBBB]` _or_ `!stats series end`").queue();

        } else if (command.startsWith("!stats series start")) {
            boolean spacesExist = true;
            String args = command.substring(19) ;
            args = args.replaceAll("\\s", "");
            // check if string contains two unique team codes
            System.out.println(args);
            if (args.length() == 7) {
//                System.out.println("here!");
                if (args.charAt(3) == 'v') {
                    team0 = args.substring(0, 3);
                    team1 = args.substring(4,7);
                }
            } else if (args.length() == 8) {
                if (args.substring(3,5).equals("vs")) {
                    team0 = args.substring(0, 3);
                    team1 = args.substring(5,8);
//                    System.out.println(team0 + "," + team1);
                }
            } else {
                channel.sendMessage("_Error parsing team names!  Be sure to follow the format *AAAvBBB!*_").queue();
                return;
            }
            if (team0.equals(team1)) {
                channel.sendMessage("_Team codes were identical_").queue();
                return;
            }

            System.out.println(team0 + "," + team1);

            boolean team0Exists= false;
            boolean team1Exists = false;
            for (String code : TEAM_CODES) {
                if (team0Exists) break;
                team0Exists = team0.equals(code);
            }
            for (String code : TEAM_CODES) {
                if (team1Exists) break;
                team1Exists = team1.equals(code);
            }
            if (!(team0Exists && team1Exists)) {
                channel.sendMessage("_Team codes are incorrect._").queue();
                return;
            }
            channel.sendMessage("_Upload replay files._").queue();
            uploadingSeries = true;

//            channel.sendMessage("Team 0: " + team0).queue();
//            channel.sendMessage("Team 1: " + team1).queue();

//            if (event.getMessage().getAttachments().isEmpty()) {
//                channel.sendMessage("_Must upload .replay file._").queue();
//                return;
//            }


//            Message.Attachment attachment = inMessage.getAttachments().get(0);
//            System.out.println(inMessage.getAttachments().size());


//            File file = new File(attachment.getFileName());
//            attachment.download(file);
//
//            Parser parser = new Parser(file);
//
//            Team t0 = parser.getTeam0();
//            t0.setTeamName(team0);
//
//            Team t1 = parser.getTeam1();
//            t1.setTeamName(team1);
//
//            channel.sendMessage("**" + t0 + "**").queue();
//            for (Player player : t0.getPlayers()) {
//                if (player != null) channel.sendMessage("\t" + player).queue();
//            }
//            channel.sendMessage("**" + t1 + "**").queue();
//            for (Player player : t1.getPlayers()) {
//                if (player != null) channel.sendMessage("\t" + player).queue();
//            }
//
//            file.delete();
//            channel.sendMessage("\n _Done!_").queue();
        } else if (inMessage.getContentRaw().equals("!stats series end")) {
            if (!uploadingSeries) {
                channel.sendMessage("_No series is being uploaded.  Maybe you meant_ `!stats series start [AAAvBBB]`_?_").queue();
                return;
            }
//            System.out.println(games.size());
            if (games.size() < 3) {
                channel.sendMessage("_Too few replays uploaded! D:  Retry uploading the series._").queue();
                return;
            } else if (games.size() > 7) {
                channel.sendMessage("_Too many replays uploaded! D:  Retry uploading the series._").queue();
                return;
            }

            File file;
            Parser parser;
            Team t0 = null;
            Team t1 = null;
            Series series = new Series();

            for (int i = 0; i < games.size(); i++) {
                file = new File(games.get(i).getFileName());
                games.get(i).download(file);

                parser = new Parser(file);

                t0 = parser.getTeam0();
                t0.setTeamName(team0);
                t1 = parser.getTeam1();
                t1.setTeamName(team1);

                series.addGame(new Game(t0, t1));

                channel.sendMessage("**Game " + (i+1) + "**").queue();

                channel.sendMessage("**" + t0 + "**").queue();
                for (Player player : t0.getPlayers()) {
                    if (player != null) channel.sendMessage("\t" + player).queue();
                }

                channel.sendMessage("**" + t1 + "**").queue();
                for (Player player : t1.getPlayers()) {
                    if (player != null) channel.sendMessage("\t" + player).queue();
                }
                if (i < games.size() - 1) channel.sendMessage("---------------------").queue();

                file.delete();
            }
            try {
//                System.out.println(series.getGames().size());
                channel.sendMessage("**" + t0.getTeamName() + ": " + series.getTeam0Score() + "\n" + t1.getTeamName() + ": " + series.getTeam1Score() + "**").queue();
            } catch (NullPointerException e) {
                System.err.println("Uh oh!  No games in the attachments list!");
                e.printStackTrace();
            }
            uploadingSeries = false;

        } else {
            if (uploadingSeries) {
                games.add(inMessage.getAttachments().get(0));
            }
        }
    }
}
