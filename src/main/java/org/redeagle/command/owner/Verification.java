package org.redeagle.command.owner;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import org.redeagle.Main;
import org.redeagle.command.Command;

public class Verification extends Command {

	@Override
	public String getName() {
		return "verification";
	}

	@Override
	public String getDescription() {
		return "Verifikasi hasil kiriman user";
	}

	@Override
	public List<String> getParameter() {
		return super.getParameter();
	}

	@Override
	public List<String> getAliases() {
		return Arrays.asList("verif","ver");
	}

	@Override
	public boolean ownerOnly() {
		return !super.ownerOnly();
	}

	@Override
	public void onExecute(JDA bot, GuildMessageReceivedEvent e, Message msg, String m, User u, Guild g, String... args) {
		if(args.length < 4 || msg.getMentionedMembers().size() == 0)  {
			msg.getTextChannel().sendMessage("An error occured...").queue();
			return;
		}
		
		Member target = msg.getMentionedMembers().get(0);
		String taskId = args[2];
		String status = args[3];
		if(status.equals("--invalid")) {
			Pattern pattern = Pattern.compile("\\{(.+?)\\}");
			Matcher matches = pattern.matcher(msg.getContentRaw());
			matches.find();
			if(Main.getDB().select("pendverif", "taskid","where id='" + target.getUser().getId() + "' AND taskid='" + taskId + "'").size() == 0) {
				msg.getTextChannel().sendMessage("User & Task ID not found... error occured..").queue();
				return;
			}
			Main.getDB().delete("pendverif", "where id='" + target.getUser().getId() + "' AND taskid='" + taskId + "'");
			msg.getTextChannel().sendMessage("Ok.. verifikasi invalid task " + taskId + " dan user " + target.getUser().getName()).queue();
			target.getUser().openPrivateChannel().queue((succ) -> {
				succ.sendMessage(taskId + " telah dibatalkan karna invalid... harap check misi dan screenshot anda !\n" + "Reason : " + matches.group(1)).queue();
			});
			return;
		} else if(status.equals("--confirm")) {
			if(Main.getDB().select("pendverif","taskid","where id='" + target.getUser().getId() + "' AND taskid='" + taskId + "'").size() == 0) {
				msg.getTextChannel().sendMessage("User dan Task tersebut tidak sedang dalam pending verif").queue();
				return;
			}
			
			if(Main.getDB().select("taskclear","taskid","where id='" + target.getUser().getId() + "' AND taskid='" + taskId + "'").size() != 0) {
				msg.getTextChannel().sendMessage("User dan Task tersebut sudah terverifikasi").queue();
			}
			
			Main.getDB().insert("INSERT INTO taskclear VALUES('" + target.getUser().getId() + "','" + taskId + "')");
			Main.getDB().delete("pendverif", "where id='" + target.getUser().getId() + "' AND taskid='" + taskId + "'");
			
			long currentRating = Long.parseLong(Main.getDB().select("userdata","rating","where discordID='" + target.getUser().getId() + "'").get(0));
			String diff = Main.getDB().select("task", "taskDiff", "where taskID='" + taskId + "'").get(0);
			long point = 0;
			
			if(diff.equals("easy")) point = 25;
			if(diff.equals("medium")) point = 40;
			if(diff.equals("hard")) point = 50;
			if(diff.equals("extreme")) point = 80;
			
			if(SetRate.dailyRate) point = (int)((double)point * (double) 1.25);
			
			currentRating += point;
			Main.getDB().update("UPDATE userdata SET rating='" + currentRating + "' where discordID='" + target.getUser().getId() + "'");
			
			long currentMission = Long.parseLong(Main.getDB().select("userdata","mission","where discordID='" + target.getUser().getId() + "'").get(0));
			currentMission += 1;
			Main.getDB().update("UPDATE userdata SET mission='" + currentMission + "' where discordID='" + target.getUser().getId() + "'");
			
			msg.getTextChannel().sendMessage("Ok.. verifikasi selesai dengan task " + taskId + " dan user " + target.getUser().getName()).queue();
			
			target.getUser().openPrivateChannel().queue(success -> {
				long p = 0;
				if(diff.equals("easy")) p = 10;
				if(diff.equals("medium")) p = 15;
				if(diff.equals("hard")) p = 20;
				if(diff.equals("extreme")) p = 30;
				if(SetRate.dailyRate) p = (int)((double)p * (double) 1.25);
				success.sendMessage("Task ID " + taskId + " telah diverifikasi, anda mendapatkan " + p + " rating.").queue();
			});
		}
	}

}
