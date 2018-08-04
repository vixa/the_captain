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
package fr.bsenac.the_captain_bot.commands;

import fr.bsenac.the_captain_bot.commandsmeta.commands.CommandContext;
import fr.bsenac.the_captain_bot.commandsmeta.dictionary.CommandDictionary;

/**
 *
 * @author vixa
 */
public class HelpCommand extends Command{

    private static final String NAME = "help";
    
    private final CommandDictionary dic;
    
    public HelpCommand(CommandDictionary dic){
        super(NAME);
        this.dic = dic;
    }
    
    @Override
    public void run(CommandContext cc) {
        StringBuilder msg = new StringBuilder();
        msg.append("Sure, I can do all of this:\n```\n");
        dic.forEach(c -> {
            msg.append(c.getName()).append(": ")
                    .append(c.help()).append("\n");
        });
        msg.append("```");
        cc.getChannel().sendMessage(msg).queue();
    }

    @Override
    public String help() {
        return "show general help";
    }
    
}
