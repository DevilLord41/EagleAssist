package org.redeagle.command.ranked;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import org.redeagle.Main;
import org.redeagle.command.Command;
import org.redeagle.debugger.Log;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class WTask extends Command {
    @Override
    public String getName() {
        return "wtask";
    }

    @Override
    public String getDescription() {
        return "Menampilkan weekly task";
    }

    @Override
    public List<String> getParameter() {
        return super.getParameter();
    }

    @Override
    public List<String> getAliases() {
        return super.getAliases();
    }

    @Override
    public boolean ownerOnly() {
        return super.ownerOnly();
    }

    @Override
    public void onExecute(JDA bot, GuildMessageReceivedEvent e, Message msg, String m, User u, Guild g, String... args) {
        ResultSet rs = Main.getDB().select("SELECT * FROM wtask");

        EmbedBuilder eb = new EmbedBuilder();
        eb.setDescription("**Misi / Task Minggu ini**");

        try {
            while(rs.next()) {
                String taskID = rs.getString("taskID");
                String taskName = rs.getString("taskName");
                String taskDescription = rs.getString("taskDescription");
                String taskDiff = rs.getString("taskDiff");

                eb.addField(taskName + " *[id:" + taskID + "]*",taskDescription + "\nTingkat Kesulitan: " + taskDiff ,false);
            }
            rs.close();
            msg.getTextChannel().sendMessage(eb.build()).queue();
        } catch(SQLException sqle) {
            Log.e(sqle.getLocalizedMessage());
        }
    }
}
