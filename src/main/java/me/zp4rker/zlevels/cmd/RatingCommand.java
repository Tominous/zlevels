package me.zp4rker.zlevels.cmd;

import me.zp4rker.core.command.RegisterCommand;
import me.zp4rker.core.command.ICommand;
import me.zp4rker.zlevels.config.Config;
import me.zp4rker.zlevels.db.StaffRating;
import me.zp4rker.zlevels.util.MessageUtil;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;

import java.awt.*;

/**
 * @author ZP4RKER
 */
public class RatingCommand implements ICommand {

    @RegisterCommand(aliases = {"rating", "ratings"},
                    usage = "{prefix}rating @User",
                    description = "Displays the ratings for the specified staff member.")
    public String onCommand(Message message, String[] args) {
        // Check arguments
        if (args.length == 0) {
            // Check if staff
            if (message.getGuild().getMember(message.getAuthor()).getRoles().stream().noneMatch(role -> role.getName()
                    .equals(Config.STAFF_ROLE))) {
                // Send error
                MessageUtil.sendError("You are not staff!", "Only staff members have ratings.", message);
                // Return null
                return null;
            }
            // Get user
            User user = message.getAuthor();
            // Send embed
            sendEmbed(user, message);
        } else if (args.length >= 1) {
            // Check for mentions
            if (message.getMentionedUsers().size() != 1) {
                // Send error
                MessageUtil.sendError("Invalid arguments!", "Invalid Arguments!\nUsage: ```-rating @User```",
                        message);
                // Return null
                return null;
            }
            // Get user
            User user = message.getMentionedUsers().get(0);
            // Check if staff
            if (message.getGuild().getMember(user).getRoles().stream().noneMatch(role -> role.getName()
                    .equals(Config.STAFF_ROLE))) {
                // Send error
                MessageUtil.sendError("That member is not staff!", "Only staff members have ratings.", message);
                // Return null
                return null;
            }
            // Send embed
            sendEmbed(user, message);
        } else {
            // Send error
            MessageUtil.sendError("Invalid arguments!", "Invalid Arguments!\nUsage: ```-rating @User```",
                    message);
        }
        // Return null
        return null;
    }

    private void sendEmbed(User user, Message message) {
        // Get the staff rating
        StaffRating rating = StaffRating.fromId(user.getId());
        // Check if exists
        if (rating == null) {
            // Send error
            MessageUtil.sendError("Invalid data!", "Could not get a staff rating for **" + user.getName() + "**!",
                    message);
            // Return
            return;
        }
        // Create embed
        EmbedBuilder embed = new EmbedBuilder();
        // Set author
        embed.setAuthor(user.getName(), null, user.getEffectiveAvatarUrl());
        // Set colour
        embed.setColor(Color.decode(Config.EMBED_COLOUR));
        // Compile ratings string
        String ratings = rating.getRatings() + (rating.getRatings() == 1 ? " rating." : " ratings.");
        // Add ratings field
        embed.addField("All Ratings", ratings, false);
        // Compile monthly ratings string
        String monthly = rating.getMonthlyRatings() + (rating.getMonthlyRatings() == 1 ? " rating." : " ratings.");
        // Add monthly ratings field
        embed.addField("Monthly Ratings", monthly, false);
        // Set footer
        embed.setFooter("Staff Rating", message.getJDA().getSelfUser().getEffectiveAvatarUrl());
        // Send embed
        message.getTextChannel().sendMessage(embed.build()).complete();
    }

}
