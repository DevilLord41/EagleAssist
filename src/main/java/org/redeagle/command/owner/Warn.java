package org.redeagle.command.owner;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import org.apache.commons.lang3.math.NumberUtils;
import org.redeagle.Main;
import org.redeagle.command.Command;

public class Warn extends Command {
    @Override
    public String getName() {
        return "warn";
    }

    @Override
    public String getDescription() {
        return "Memberikan Warning Kepada Member";
    }

    @Override
    public List<String> getParameter() {
        return Arrays.asList("mentions", "point","reason");
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
        if(!msg.getMember().hasPermission(Permission.ADMINISTRATOR)) {
            if(Main.getDB().select("warn","count","where id='" + u.getId() + "'").size() == 0) {
                e.getChannel().sendMessage("Jumlah warn anda : 0").queue();
            } else {
                int warnCount = Integer.parseInt(Main.getDB().select("warn","count","where id='" + u.getId() + "'").get(0));
                e.getChannel().sendMessage("Jumlah warn anda : " + warnCount).queue();
            }
            return;
        }
        if(msg.getMentionedMembers().size() == 0) return;
        if(msg.getMentionedMembers().get(0).getUser().getId().equals("358262219349295115")) {
            msg.getTextChannel().sendMessage("Mau mati lo ngewarn dia?").queue();
            return;
        }
        if(args.length == 3) {
        	if(args[2].equals("check")) {
        		Member target = msg.getMentionedMembers().get(0);
        		int warnCount = Integer.parseInt(Main.getDB().select("warn","count","where id='" + target.getUser().getId() + "'").get(0));
        		e.getChannel().sendMessage(target.getUser().getName() + " : " + warnCount + " warn").queue();
        	}
        	return;
        }
        if(args.length < 4) {
        	msg.getTextChannel().sendMessage("Gunakan `>warn <mentions> <point> <{reason}>\nExample: >warn @Lucione 1 {bad word}").queue();
        	return;
        }
        
        Member target = msg.getMentionedMembers().get(0);
        
        if(!NumberUtils.isNumber(args[2])) {
        	e.getChannel().sendMessage("Warn count should be number").queue();
        	return;
        }
        
        Pattern match = Pattern.compile("\\{(.+?)\\}");
        Matcher matcher = match.matcher(msg.getContentRaw());
        matcher.find();
        int count = Integer.parseInt(args[2]);
        String reason = matcher.group(1);
        
        boolean hasData = Main.getDB().select("warn","count","where id='" + target.getUser().getId() + "'").size() > 0;
        
        if(hasData) {
        	int warnCount = Integer.parseInt(Main.getDB().select("warn","count","where id='" + target.getUser().getId() + "'").get(0));
        	warnCount += count;
        	
        	Main.getDB().update("UPDATE warn SET count='" + warnCount + "' where id='" + target.getUser().getId() + "'");
        	
        	e.getChannel().sendMessage(target.getUser().getName() + " telah diberikan " + count + " warn oleh " + u.getName()).queue();
        	target.getUser().openPrivateChannel().queue(ss -> {
        		ss.sendMessage("Anda telah diberikan " + count + " warn oleh " + u.getName()).queue();
        		ss.sendMessage("Reason : " + reason).queue();
        		int w = Integer.parseInt(Main.getDB().select("warn","count","where id='" + target.getUser().getId() + "'").get(0));
        		if(w >= 10) {
        			ss.sendMessage("Anda telah mencapai 10 warn... anda dalam masa pertimbangan").queue();
        		}
        	});
        	g.getMemberById("239627873701330944").getUser().openPrivateChannel().queue(ss-> {
        		ss.sendMessage(target.getUser().getName() + " telah diberikan " + count + " warn oleh " + u.getName()).queue();
        		int wc = Integer.parseInt(Main.getDB().select("warn","count","where id='" + target.getUser().getId() + "'").get(0));
        		ss.sendMessage("Reason : " + reason + "\n" + "Jumlah warn : " + wc).queue();
        	});
        } else {
        	Main.getDB().insert("insert into warn values('" + count + "','" + target.getUser().getId() + "')");
        	e.getChannel().sendMessage(target.getUser().getName() + " telah diberikan " + count + " warn oleh " + u.getName()).queue();
        	target.getUser().openPrivateChannel().queue(ss -> {
        		ss.sendMessage("Anda telah diberikan " + count + " warn oleh " + u.getName()).queue();
        		ss.sendMessage("Reason : " + reason).queue();
        	});
        	g.getMemberById("239627873701330944").getUser().openPrivateChannel().queue(ss-> {
        		ss.sendMessage(target.getUser().getName() + " telah diberikan " + count + " warn oleh " + u.getName()).queue();
        		ss.sendMessage("Reason : " + reason + "\n" + "Jumlah warn : " + count).queue();
        	});
        }
    }
}
