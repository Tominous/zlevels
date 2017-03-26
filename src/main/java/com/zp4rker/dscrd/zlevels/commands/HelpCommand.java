package com.zp4rker.dscrd.zlevels.commands;

import com.zp4rker.dscrd.core.cmd.CommandExecutor;
import com.zp4rker.dscrd.core.cmd.RegisterCommand;
import com.zp4rker.dscrd.core.cmd.handler.CommandHandler;
import com.zp4rker.dscrd.core.logger.ZLogger;
import com.zp4rker.dscrd.zlevels.ZLevels;
import com.zp4rker.dscrd.zlevels.core.config.Config;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;

import java.awt.*;

/**
 * @author ZP4RKER
 */
public class HelpCommand implements CommandExecutor {

    @RegisterCommand(aliases = "help")
    public String onCommand(Message message) {
        // Create embed
        EmbedBuilder embed = new EmbedBuilder();
        // Set author
        embed.setAuthor("ZLevels Help - Command List", null, null);
        // Set colour
        embed.setColor(Color.decode(Config.EMBED_COLOUR));
        // Set footer
        embed.setFooter("Written by ZP4RKER", message.getJDA().getSelfUser().getEffectiveAvatarUrl());
        // Set description
        embed.setDescription(compileList());
        // Catch errors
        try {
            // Open DM channel
            message.getAuthor().openPrivateChannel().complete();
        } catch (Exception e) {
            // Send warning
            ZLogger.warn("Could not open DM channel or already open.");
        }
        // Send DM
        message.getAuthor().getPrivateChannel().sendMessage(embed.build()).complete();
        // Return null
        return null;
    }

    private String compileList() {
        // Start string
        String str = "";
        // Loop through commands
        for (CommandHandler.Command command : ZLevels.handler.getCommands().values()) {
            // Check if help command
            if (command.getCommandAnnotation().aliases()[0].equals("help")) continue;
            // Add to string
            str += compileCommand(command);
        }
        // Return string
        return str;
    }

    private String compileCommand(CommandHandler.Command command) {
        // Get first alias
        String label = command.getCommandAnnotation().aliases()[0];
        // Get usage
        String usage = command.getCommandAnnotation().usage().replace("{prefix}", Config.PREFIX);
        // Get description
        String desc = command.getCommandAnnotation().description();
        // Compile string
        String str = "**" + label + "** - " + desc + "\n__Usage:__ `" + usage + "`\n\n";
        // Return string
        return str;
    }

}
