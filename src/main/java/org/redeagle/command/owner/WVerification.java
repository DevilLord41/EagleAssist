package org.redeagle.command.owner;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import org.redeagle.Main;
import org.redeagle.command.Command;

public class WVerification extends Command {

    @Override
    public String getName() {
        return "wverification";
    }

    @Override
    public String getDescription() {
        return "Verifikasi weekly hasil kiriman user";
    }

    @Override
    public List<String> getParameter() {
        return super.getParameter();
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("wverif","wver");
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
            if(Main.getDB().select("wpendverif", "taskid","where id='" + target.getUser().getId() + "' AND taskid='" + taskId + "'").size() == 0) {
                msg.getTextChannel().sendMessage("User & Task ID not found... error occured..").queue();
                return;
            }
            Main.getDB().delete("wpendverif", "where id='" + target.getUser().getId() + "' AND taskid='" + taskId + "'");
            msg.getTextChannel().sendMessage("Ok.. verifikasi invalid task " + taskId + " dan user " + target.getUser().getName()).queue();
            target.getUser().openPrivateChannel().queue((succ) -> {
                succ.sendMessage(taskId + " telah dibatalkan karna invalid... harap check misi dan screenshot anda !\n" + "Reason : " + matches.group(1)).queue();
            });
            return;
        } else if(status.equals("--confirm")) {
            if(Main.getDB().select("wpendverif","taskid","where id='" + target.getUser().getId() + "' AND taskid='" + taskId + "'").size() == 0) {
                msg.getTextChannel().sendMessage("User dan Task tersebut tidak sedang dalam pending verif").queue();
                return;
            }

            if(Main.getDB().select("wtaskclear","taskid","where id='" + target.getUser().getId() + "' AND taskid='" + taskId + "'").size() != 0) {
                msg.getTextChannel().sendMessage("User dan Task tersebut sudah terverifikasi").queue();
            }

            Main.getDB().insert("INSERT INTO wtaskclear VALUES('" + target.getUser().getId() + "','" + taskId + "')");
            Main.getDB().delete("wpendverif", "where id='" + target.getUser().getId() + "' AND taskid='" + taskId + "'");

            long currentRating = Long.parseLong(Main.getDB().select("userdata","rating","where discordID='" + target.getUser().getId() + "'").get(0));
            String diff = Main.getDB().select("wtask", "taskDiff", "where taskID='" + taskId + "'").get(0);
            long point = 0;
            if(diff.equals("easy")) point = 20;
            if(diff.equals("medium")) point = 30;
            if(diff.equals("hard")) point = 40;
            if(diff.equals("extreme")) point = 60;

            currentRating += point;
            Main.getDB().update("UPDATE userdata SET rating='" + currentRating + "' where discordID='" + target.getUser().getId() + "'");

            long currentMission = Long.parseLong(Main.getDB().select("userdata","mission","where discordID='" + target.getUser().getId() + "'").get(0));
            currentMission += 1;
            Main.getDB().update("UPDATE userdata SET mission='" + currentMission + "' where discordID='" + target.getUser().getId() + "'");

            if(currentRating >= 4200) { //LEGENDARY
                boolean can = true;
                for(Role r : target.getRoles()) {
                    if(r.getId().equals("497296090798751745")) {
                        can = false;
                        break;
                    }
                }
                if(can) {
                    g.getController().addSingleRoleToMember(target, g.getRoleById("497296090798751745")).queue();
                    g.getController().removeSingleRoleFromMember(target, g.getRoleById("497296092740845583")).queue();
                    target.getUser().openPrivateChannel().queue((scs) -> {
                        scs.sendMessage("Kongretulesion, anda telah rank up ke Legendary...").queue();
                    });
                }
            } else if(currentRating >= 3400) { //PALADIN
                boolean can = true;
                for(Role r : target.getRoles()) {
                    if(r.getId().equals("497296092740845583")) {
                        can = false;
                        break;
                    }
                }
                if(can) {
                    g.getController().addSingleRoleToMember(target, g.getRoleById("497296092740845583")).queue();
                    g.getController().removeSingleRoleFromMember(target, g.getRoleById("497296093088972800")).queue();
                    target.getUser().openPrivateChannel().queue((scs) -> {
                        scs.sendMessage("Kongretulesion, anda telah rank up ke Paladin...").queue();
                    });
                }
            } else if(currentRating >= 2800) { //GRAND TEMPLAR
                boolean can = true;
                for(Role r : target.getRoles()) {
                    if(r.getId().equals("497296093088972800")) {
                        can = false;
                        break;
                    }
                }
                if(can) {
                    g.getController().addSingleRoleToMember(target, g.getRoleById("497296093088972800")).queue();
                    g.getController().removeSingleRoleFromMember(target, g.getRoleById("497296093223190529")).queue();
                    target.getUser().openPrivateChannel().queue((scs) -> {
                        scs.sendMessage("Kongretulesion, anda telah rank up ke Grand Templar...").queue();
                    });
                }

            } else if(currentRating >= 2200) { //TEMPLAR
                boolean can = true;
                for(Role r : target.getRoles()) {
                    if(r.getId().equals("497296093223190529")) {
                        can = false;
                        break;
                    }
                }
                if(can) {
                    g.getController().addSingleRoleToMember(target, g.getRoleById("497296093223190529")).queue();
                    g.getController().removeSingleRoleFromMember(target, g.getRoleById("497298003384598530")).queue();
                    target.getUser().openPrivateChannel().queue((scs) -> {
                        scs.sendMessage("Kongretulesion, anda telah rank up ke Templar...").queue();
                    });
                }
            } else if(currentRating >= 1800) { //KNIGHT
                boolean can = true;
                for(Role r : target.getRoles()) {
                    if(r.getId().equals("497298003384598530")) {
                        can = false;
                        break;
                    }
                }
                if(can) {
                    g.getController().addSingleRoleToMember(target, g.getRoleById("497298003384598530")).queue();
                    g.getController().removeSingleRoleFromMember(target, g.getRoleById("497296093680500744")).queue();
                    target.getUser().openPrivateChannel().queue((scs) -> {
                        scs.sendMessage("Kongretulesion, anda telah rank up ke Knight...").queue();
                    });
                }
            } else if(currentRating >= 1200) { //ASSASSINS
                boolean can = true;
                for(Role r : target.getRoles()) {
                    if(r.getId().equals("497296093680500744")) {
                        can = false;
                        break;
                    }
                }
                if(can) {
                    g.getController().addSingleRoleToMember(target, g.getRoleById("497296093680500744")).queue();
                    g.getController().removeSingleRoleFromMember(target, g.getRoleById("497296094007656448")).queue();
                    target.getUser().openPrivateChannel().queue((scs) -> {
                        scs.sendMessage("Kongretulesion, anda telah rank up ke Assassins...").queue();
                    });
                }

            } else if(currentRating >= 800) { //ROGUE
                boolean can = true;
                for(Role r : target.getRoles()) {
                    if(r.getId().equals("497296094007656448")) {
                        can = false;
                        break;
                    }
                }
                if(can) {
                    g.getController().addSingleRoleToMember(target, g.getRoleById("497296094007656448")).queue();
                    g.getController().removeSingleRoleFromMember(target, g.getRoleById("497296096943669258")).queue();
                    target.getUser().openPrivateChannel().queue((scs) -> {
                        scs.sendMessage("Kongretulesion, anda telah rank up ke Rogue...").queue();
                    });
                }

            } else if(currentRating >= 300) { //NOVICE
                boolean can = true;
                for(Role r : target.getRoles()) {
                    if(r.getId().equals("497296096943669258")) {
                        can = false;
                        break;
                    }
                }
                if(can) {
                    g.getController().addSingleRoleToMember(target, g.getRoleById("497296096943669258")).queue();
                    target.getUser().openPrivateChannel().queue((scs) -> {
                        scs.sendMessage("Kongretulesion, anda telah rank up ke Novice...").queue();
                    });
                }
            }

            msg.getTextChannel().sendMessage("Ok.. verifikasi selesai dengan task " + taskId + " dan user " + target.getUser().getName()).queue();

            target.getUser().openPrivateChannel().queue(success -> {
                long p = 0;
                if(diff.equals("easy")) p = 10;
                if(diff.equals("medium")) p = 15;
                if(diff.equals("hard")) p = 20;
                if(diff.equals("extreme")) p = 30;
                success.sendMessage("Task ID " + taskId + " telah diverifikasi, anda mendapatkan " + p + " rating.").queue();
            });
        }
    }

}
