package org.redeagle.command.core;

import java.util.Arrays;
import java.util.List;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import org.redeagle.command.Command;

public class Donation extends Command {

	@Override
	public String getName() {
		return "donation";
	}

	@Override
	public String getDescription() {
		return "Support this bot by read text after use this command";
	}

	@Override
	public List<String> getParameter() {
		return super.getParameter();
	}

	@Override
	public List<String> getAliases() {
		return Arrays.asList("donate","donasi","support");
	}

	@Override
	public boolean ownerOnly() {
		return super.ownerOnly();
	}

	@Override
	public void onExecute(JDA bot, GuildMessageReceivedEvent e, Message msg, String m, User u, Guild g, String... args) {
		EmbedBuilder embedded = new EmbedBuilder();
		embedded.setTitle("Support Bot Ini !");
		embedded.setDescription(
				"**EagleAssist** adalah bot unik yang dimiliki oleh komunitas kita tersendiri.\n"
				+ "Bot ini juga merupakan **maskot dan identitas komunitas kita**, karna hanya komunitas PUBGM kitalah yang punya bot pribadi (mungkin)\n"
				+ "Hal tersebut menjadi suatu **kebanggaan** bagi kita tersendiri.\n"
				+ "Tetapi, menjalankan dan merawat sebuah bot tidaklah mudah, dan membutuhkan biaya.\n"
				+ "Bot ini, berjalan dengan biaya **25rb/bulan** menggunakan Hosting VPS arubacloud.it\n"
				+ "Dengan spesifikasi 1gb ram, dan 1 vcpu, 2gb swap memory.\n"
				+ "Untuk membantu pembiayaan bot ini (untuk perpanjang masa server, dan upgrade spesifikasi),\n"
				+ "Anda bisa donasi **TANPA MINIMUM** ke Darius (Lucione).\n"
				+ "Jika anda ingin donasi untuk mensupport bot ini, silahkan DM ke Darius."
		);
		embedded.addField("Total Donatur", "3 Donatur", true);
		embedded.addField("Dengan Total Donasi", "Rp. 110.000",true);
		embedded.addBlankField(true);
		embedded.addField("List Donatur",
				"RE×Nexus\n"
				+ "RE×shiroxx\n"
				+ "RE×Kece"
		,true);
		embedded.addField("Total Donasi",
				"25.000\n"
				+ "35.000\n"
				+ "50.000"
		,true);
		e.getChannel().sendMessage(embedded.build()).queue();
	}

}
