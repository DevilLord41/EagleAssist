package org.redeagle.command.ranked;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import org.redeagle.Main;
import org.redeagle.command.Command;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class WClear extends Command {
    @Override
    public String getName() {
        return
                "wclear";
    }

    @Override
    public String getDescription() {
        return "Verifikasi weekly task";
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
        if(args.length == 1) {
            msg.delete().queue();
            msg.getTextChannel().sendMessage("Harus disertai task id, >clear <taskId>").queue((success) -> {
                success.delete().queueAfter(3000, TimeUnit.MILLISECONDS);
            });
            return;
        }
        if(msg.getAttachments().size() == 0) {
            msg.delete().queue();
            msg.getTextChannel().sendMessage("Harus disertai 1 bukti screenshoot").queue((success) -> {
                success.delete().queueAfter(3000, TimeUnit.MILLISECONDS);
            });
            return;
        }

        if(!msg.getAttachments().get(0).isImage()) {
            msg.delete().queue();
            msg.getTextChannel().sendMessage("Harus disertai bukti screenshot").queue((success) -> {
                success.delete().queueAfter(3000, TimeUnit.MILLISECONDS);
            });
            return;
        }

        String taskId = args[1];
        if(Main.getDB().select("wtask","taskid","where taskid='" + taskId + "'").size() == 0) {
            msg.getTextChannel().sendMessage("Tidak ada task ID tersebut").queue((success) -> {
                success.delete().queueAfter(3000, TimeUnit.MILLISECONDS);
            });
            return;
        }
        if(Main.getDB().select("wtaskclear", "taskid", "where id='" + u.getId() + "' AND taskid='" + taskId + "'").size() != 0) {
            msg.delete().queue();
            msg.getTextChannel().sendMessage("Anda sudah menyelesaikan task/misi itu..").queue((success) -> {
                success.delete().queueAfter(3000, TimeUnit.MILLISECONDS);
            });
            return;
        }

        if(Main.getDB().select("wpendverif","taskid", "where id='" + u.getId() + "' AND taskid='" + taskId + "'").size() != 0) {
            msg.delete().queue();
            msg.getTextChannel().sendMessage("Sabar lah lagi diverif ni, coba task laen dlu kek.").queue((success) -> {
                success.delete().queueAfter(3000, TimeUnit.MILLISECONDS);
            });
            return;
        }

        Main.getDB().insert("INSERT INTO wpendverif VALUES('" + u.getId() + "', '" + taskId + "')");
        e.getAuthor().openPrivateChannel().queue(success -> {
            success.sendMessage("Ok, mohon tunggu diverifikasi oleh Darius (Lucione#4283). [Task ID: " + taskId + "]").queue();
        });
    }
}
