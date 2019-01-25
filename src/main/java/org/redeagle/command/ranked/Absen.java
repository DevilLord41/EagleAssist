package org.redeagle.command.ranked;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import org.redeagle.Main;
import org.redeagle.command.Command;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Absen extends Command {
    @Override
    public String getName() {
        return "absen";
    }

    @Override
    public String getDescription() {
        return "absen harian..";
    }

    @Override
    public List<String> getParameter() {
        return super.getParameter();
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("present","absence");
    }

    @Override
    public boolean ownerOnly() {
        return super.ownerOnly();
    }

    @Override
    public void onExecute(JDA bot, GuildMessageReceivedEvent e, Message msg, String m, User u, Guild g, String... args) {

        if(Main.getDB().select("ispresent","id","where id='" + u.getId() + "'").size() == 0) {
            Random rnd = new Random();
            int bonus = rnd.nextInt(3)+1;

            long currentRating = Long.parseLong(Main.getDB().select("userdata","rating", "where discordId='" + u.getId() + "'").get(0));
            currentRating += bonus;

            Main.getDB().update("UPDATE userdata SET rating='" + currentRating +"' WHERE discordId='" + u.getId() + "'");

            if(Main.getDB().select("totalAbsen","total","where id='" + u.getId() + "'").size() == 0) {
                Main.getDB().insert("INSERT INTO totalAbsen VALUES('" + u.getId() + "', '1')");
            } else {
                long currentAbsen = Long.parseLong(Main.getDB().select("totalAbsen","total","where id='" + u.getId() + "'").get(0));
                currentAbsen += 1;

                Main.getDB().update("UPDATE totalAbsen SET total='" + currentAbsen + "' WHERE id='" + u.getId() + "'");
            }
            Main.getDB().insert("INSERT INTO ispresent VALUES('" + u.getId() + "')");
            msg.getTextChannel().sendMessage(bonus + " rating telah ditambahkan kerating sebagai bonus harian.").queue();
        } else {
            msg.getTextChannel().sendMessage("Anda telah absent harian, tunggu besok lagi...").queue();
        }
    }
}
