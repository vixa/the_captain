/*
 * The MIT License
 *
 * Copyright 2018 vixa.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package fr.bsenac.the_captain_bot.commandsmeta.parser;

import fr.bsenac.the_captain_bot.commandsmeta.commands.CommandContext;
import java.util.ArrayList;
import java.util.Arrays;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 *
 * @author vixa
 */
public class CommandParser {

    private static final String MULTICOMMAND_SEPARATOR = " then ";

    public CommandContext parse(MessageReceivedEvent event) {
        User author = event.getAuthor();
        Member member = event.getMember();
        MessageChannel channel = event.getChannel();
        Message message = event.getMessage();
        Guild guild = event.getGuild();
        String line = event.getMessage().getContentRaw();
        line = removeFirstElement(line);

        CommandContext current = null;
        String[] commands = line.split(MULTICOMMAND_SEPARATOR);
        for (int i = commands.length - 1; i >= 0; i--) {
            current = createContext(author, member, channel, message,
                    guild, commands[i], current);
        }
        return current;
    }

    /**
     * Remove the first element of a command
     *
     * @param line
     * @return
     */
    private String removeFirstElement(String line) {
        int firstSpace = line.indexOf(' ');
        return line.substring(firstSpace + 1, line.length());
    }

    private CommandContext createContext(User author, Member member,
            MessageChannel chanel, Message message, Guild guild, String line,
            CommandContext next) {
        ArrayList<String> splitedLine = new ArrayList<>(Arrays.asList(line.split(" ")));
        if (splitedLine.size() < 1) {
            throw new CommandParseException("Empty line");
        }
        String command;
        if (splitedLine.size() >= 1) {
            command = splitedLine.remove(0); //Remove and save the command
        } else {
            command = "";
        }
        String[] args = splitedLine.toArray(new String[splitedLine.size()]);
        if (next != null) {
            return new CommandContext(author, member, chanel, message, guild, command, args, next);
        }
        return new CommandContext(author, member, chanel, message, guild, command, args);
    }
}
