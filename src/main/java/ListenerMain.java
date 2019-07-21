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

public class ListenerMain extends ListenerAdapter {

    public ListenerMain() {
        super();

    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        MessageChannel channel = event.getChannel();
        Message inMessage = event.getMessage();
        User author = event.getAuthor();

        if (author.isBot()) return; // dont want to be responding to bots

        // !help
        if (inMessage.getContentRaw().equals("!stats help")) {
            channel.sendMessage("Below are the commands for this bot:\n" +
                    "\t1) !stats view <replay.replay>\n" +
                    "\t2) ~~!stats upload <replay.replay>~~\n" +
                    "\t3) !stats series start ###v###\n" +
                    "\t4) !stats series end\n" +
                    "\t5) !stats help").queue();
        }
        // !stats view <replay.replay>
        else if (inMessage.getContentRaw().startsWith("!stats view")) {
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
        else if (inMessage.getContentRaw().startsWith("!stats upload")) {

        }
        // !stats series start ###v###
        else if (inMessage.getContentRaw().startsWith("!stats series start ###v###")) {

        }
    }
}
