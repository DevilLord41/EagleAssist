package org.redeagle.command.owner;

import java.util.Arrays;
import java.util.List;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import org.redeagle.Main;
import org.redeagle.command.Command;

public class Clean extends Command {

	@Override
	public String getName() {
		return "clean";
	}

	@Override
	public String getDescription() {
		return "Clean pending verification dan task";
	}

	@Override
	public List<String> getParameter() {
		return Arrays.asList("<--pendverif | --task>");
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
		if(!msg.getMember().hasPermission(Permission.ADMINISTRATOR)) return;
		if(args[1].equals("--pendverif")) {
			Main.getDB().delete("pendverif", "");
			msg.getTextChannel().sendMessage("OK, <@&512995573637316608>, semua pending verifikasi telah dibersihkan karna hari berganti..").queue();
		} else if(args[1].equals("--task")) {
			Main.getDB().delete("task", "");
			Main.getDB().delete("taskclear","");
			msg.getTextChannel().sendMessage("Ok, <@&512995573637316608>, semua task / misi telah direset..").queue();
		} else if(args[1].equals("--absen")) {
			Main.getDB().delete("ispresent","");
			msg.getTextChannel().sendMessage("Ok, <@&512995573637316608>, silahkan absent..").queue();
		} else if(args[1].equals("--wtask")) {
			Main.getDB().delete("wtask","");
			msg.getTextChannel().sendMessage("Ok, <@&512995573637316608>, weekly task telah direset..").queue();
		} else if(args[1].equals("--wpendverif")) {
			Main.getDB().delete("wpendverif","");
			msg.getTextChannel().sendMessage("OK, <@&512995573637316608>, semua weekly pending verifikasi telah dibersihkan karna hari berganti..").queue();
		}

	}

}
