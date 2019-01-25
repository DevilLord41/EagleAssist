package org.redeagle.command.owner;

import java.util.List;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import org.redeagle.BotConfiguration;
import org.redeagle.Main;
import org.redeagle.command.Command;

public class Eval extends Command {

	@Override
	public String getName() {
		return "eval";
	}

	@Override
	public String getDescription() {
		return super.getDescription();
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
		return true;
	}

	@Override
	public void onExecute(JDA bot, GuildMessageReceivedEvent e, Message msg,
			String m, User u, Guild g, String... args) {
		ScriptEngine se = new ScriptEngineManager().getEngineByName("nashorn");
        se.put("e", e);
        se.put("bot", bot);
        se.put("g", g);
        se.put("ch", e.getChannel());
        se.put("db", Main.getDB());
        	
        try {
            e.getChannel().sendMessage("Output : \n" + se.eval(msg.getContentRaw().toString().replace(BotConfiguration.BOT_PREFIX + "eval ", ""))).queue();
        } 
        catch(Exception exc) {
            e.getChannel().sendMessage("Error occured..." + exc).queue();
        }
	}

}
