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
package fr.bsenac.the_captain_bot.commandsmeta.commands;

import java.util.Optional;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;

/**
 *
 * @author vixa
 */
public class CommandContext {

    private final Member member;
    private final MessageChannel channel;
    private final Message message;
    private final Guild guild;
    private final String command;
    private final String[] args;
    private final Optional<CommandContext> next;

    public CommandContext(User author, Member member, MessageChannel channel,
            Message message, Guild guild, String command, String[] args) {
        this.member = member;
        this.channel = channel;
        this.message = message;
        this.guild = guild;
        this.command = command;
        this.args = args;
        this.next = Optional.empty();
    }

    public CommandContext(User author, Member member, MessageChannel channel,
            Message message, Guild guild, String command, String[] args,
            CommandContext next) {
        this.member = member;
        this.channel = channel;
        this.message = message;
        this.guild = guild;
        this.command = command;
        this.args = args;
        this.next = Optional.of(next);
    }

    public void executeNextCommand() {
        if (next.isPresent()) {
            CommandsIndex.getIndex().findAndExecute(next.get());
        }
    }

    public User getAuthor() {
        return member.getUser();
    }

    public Member getMember() {
        return member;
    }

    public MessageChannel getChannel() {
        return channel;
    }

    public Message getMessage() {
        return message;
    }

    public Guild getGuild() {
        return guild;
    }

    public String getCommand() {
        return command;
    }

    public String[] getArgs() {
        return args;
    }

    /**
     * Check if there are args
     * @return true if there are args, false else
     */
    public boolean hasArgs() {
        return args.length != 0;
    }
}
