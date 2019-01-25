package org.redeagle;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;

import javax.security.auth.login.LoginException;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberLeaveEvent;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.core.exceptions.RateLimitedException;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.redeagle.command.CommandHandler;
import org.redeagle.command.core.Avatar;
import org.redeagle.command.core.Donation;
import org.redeagle.command.core.Frame;
import org.redeagle.command.core.Help;
import org.redeagle.command.core.Logo;
import org.redeagle.command.core.Ping;
import org.redeagle.command.games.bet.Bet;
import org.redeagle.command.inventory.Use;
import org.redeagle.command.moderation.Ban;
import org.redeagle.command.moderation.Kick;
import org.redeagle.command.moderation.Mute;
import org.redeagle.command.moderation.SetNickname;
import org.redeagle.command.moderation.Unmute;
import org.redeagle.command.music.Leave;
import org.redeagle.command.music.Play;
import org.redeagle.command.music.Queue;
import org.redeagle.command.music.Skip;
import org.redeagle.command.music.Stop;
import org.redeagle.command.owner.AddRating;
import org.redeagle.command.owner.Clean;
import org.redeagle.command.owner.Eval;
import org.redeagle.command.owner.Generate;
import org.redeagle.command.owner.SetRate;
import org.redeagle.command.owner.SetStatus;
import org.redeagle.command.owner.Verification;
import org.redeagle.command.owner.WVerification;
import org.redeagle.command.owner.Warn;
import org.redeagle.command.ranked.Absen;
import org.redeagle.command.ranked.Clear;
import org.redeagle.command.ranked.Profile;
import org.redeagle.command.ranked.Task;
import org.redeagle.command.ranked.Top;
import org.redeagle.command.ranked.WClear;
import org.redeagle.command.ranked.WTask;
import org.redeagle.command.shop.ItemHandler;
import org.redeagle.command.shop.Shop;
import org.redeagle.command.shop.item.Assassins;
import org.redeagle.command.shop.item.GrandTemplar;
import org.redeagle.command.shop.item.Knight;
import org.redeagle.command.shop.item.Legendary;
import org.redeagle.command.shop.item.Novice;
import org.redeagle.command.shop.item.Paladin;
import org.redeagle.command.shop.item.Rogue;
import org.redeagle.command.shop.item.Templar;
import org.redeagle.event.Event;
import org.redeagle.sqlmanager.SQLManager;

public class Main extends ListenerAdapter {
	public JDA bot;
	public static SQLManager database;
	public static CommandHandler commandHandle = new CommandHandler();
	public static ItemHandler itemHandle = new ItemHandler();
	
	public static CommandHandler getCommandHandler() {
		return commandHandle;
	}
	
	public static ItemHandler getItemHandler() {
		return itemHandle;
	}
	
	public static SQLManager getDB() {
		return database;
	}
	
	public static void main(String[] args) throws LoginException, RateLimitedException, InterruptedException, IOException {
		registerCommand();
		registerItem();
		BotConfiguration.configure();
		
		database = new SQLManager();
		JDA bot = new JDABuilder(AccountType.BOT).setToken(BotConfiguration.BOT_TOKEN).build();
		bot.addEventListener(new Main(bot));
		bot.getPresence().setGame(Game.listening("Lucione | >help"));
		Event.dayChangeEvent(bot);
	}  
	
	public Main(JDA bot) {
		this.bot = bot;
	}
	
	@Override
	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
		if (event.getAuthor().isBot()) return;
		if (event.getMessage().getContentRaw().startsWith(BotConfiguration.BOT_PREFIX)) {
			commandHandle.handle(event, bot);
		}

	}

	@Override
	public void onGuildMessageReactionAdd(GuildMessageReactionAddEvent event) {
		if(event.getMessageId().equals("523552923871870986")) {
			for(Role r : event.getMember().getRoles()) {
				if(r.getId().equals("512995253842608154")) {
					event.getGuild().getController().removeSingleRoleFromMember(event.getMember(),event.getGuild().getRoleById("512995253842608154")).queue();
					event.getUser().openPrivateChannel().queue(s -> {
						s.sendMessage("Anda meninggalkan **The Dark Club**, goodbye...").queue();
					});
					return;
				}
			}

			event.getGuild().getController().addSingleRoleToMember(event.getMember(),event.getGuild().getRoleById("512995253842608154")).queue();
			event.getUser().openPrivateChannel().queue(s -> {
				s.sendMessage("Anda bergabung ke **The Dark Club**, welcome...").queue();
			});
		}
	}

	@Override
	public void onGuildMemberJoin(GuildMemberJoinEvent event) {
		event.getGuild().getTextChannelById("512997550563917824").sendMessage(event.getMember().getAsMention() + ", silahkan check #intro-diri dan #new-member").queue();
	}

	@Override
	public void onGuildMemberLeave(GuildMemberLeaveEvent event) {
		event.getGuild().getTextChannelById("512997550563917824").sendMessage(event.getMember().getUser().getName() + ", goodbye...").queue();
	}

	public static void registerCommand() {
		
		commandHandle.register(new Help());
		commandHandle.register(new Ping());
		commandHandle.register(new Avatar());
		commandHandle.register(new Frame());
		commandHandle.register(new Logo());
		commandHandle.register(new Donation());
		
		commandHandle.register(new Play());
		commandHandle.register(new Queue());
		commandHandle.register(new Skip());
		commandHandle.register(new Leave());
		commandHandle.register(new Stop());
		
		commandHandle.register(new Kick());
		commandHandle.register(new Ban());
		commandHandle.register(new SetNickname());
		commandHandle.register(new Mute());
		commandHandle.register(new Unmute());
		commandHandle.register(new Warn());
		
		commandHandle.register(new Eval());
		commandHandle.register(new SetStatus());
		commandHandle.register(new Verification());
		commandHandle.register(new Clean());
		commandHandle.register(new AddRating());
		commandHandle.register(new WVerification());
		commandHandle.register(new Generate());
		commandHandle.register(new SetRate());
		
		commandHandle.register(new Task());
		commandHandle.register(new WTask());
		commandHandle.register(new WClear());
		commandHandle.register(new Clear());
		commandHandle.register(new Profile());
		commandHandle.register(new Top());
		commandHandle.register(new Absen());
		
		commandHandle.register(new Bet());
		
		commandHandle.register(new Shop());
		
		commandHandle.register(new Use());
	}
	
	public static void registerItem() {
		
		/* Title */
		itemHandle.addItem(new Novice());
		itemHandle.addItem(new Rogue());
		itemHandle.addItem(new Assassins());
		itemHandle.addItem(new Knight());
		itemHandle.addItem(new Templar());
		itemHandle.addItem(new GrandTemplar());
		itemHandle.addItem(new Paladin());
		itemHandle.addItem(new Legendary());
		
		/* Usable */
		
		
		/* Collections */
		
		
	}
	
	public static void printExcel() {
		try {
			File excelFile = new File("D:/Data RedEagle/Member List.xlsx");
		    FileInputStream fis = new FileInputStream(excelFile);
		    XSSFWorkbook workbook = new XSSFWorkbook(fis);
		    XSSFSheet sheet = workbook.getSheetAt(0);

		    Iterator<Row> rowIt = sheet.iterator();

		    while(rowIt.hasNext()) {
		      Row row = rowIt.next();
		      Iterator<Cell> cellIterator = row.cellIterator();
		      String data = "INSERT INTO userdata VALUES(";
		      DataFormatter fmt = new DataFormatter();
		      while (cellIterator.hasNext()) {
		        Cell cell = cellIterator.next();
		        if(cell.toString().equals("")) continue;
		        data += "'" + fmt.formatCellValue(cell) + "',";
		      }
		      data = data.substring(0, data.length()-1);
		      data += ",'0','0');";
		      System.out.println(data);
		    }

		    workbook.close();
		    fis.close();
		} catch(Exception e) {
			
		}
	}
}