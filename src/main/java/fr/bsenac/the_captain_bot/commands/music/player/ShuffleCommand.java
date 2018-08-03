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
package fr.bsenac.the_captain_bot.commands.music.player;

import fr.bsenac.the_captain_bot.audio.PlaylistsDatabase;
import fr.bsenac.the_captain_bot.commands.Command;
import fr.bsenac.the_captain_bot.commandsmeta.commands.CommandContext;

/**
 *
 * @author vixa
 */
public class ShuffleCommand extends Command {

    private static final String NAME = "shuffle";

    public ShuffleCommand() {
        super(NAME);
    }

    @Override
    public void run(CommandContext cc) {
        String msg;
        if (cc.hasArgs()) {
            msg = shufflePlaylist(cc);
        } else {
            msg = shuffleQueue(cc);
        }
        cc.getChannel().sendMessage(msg).queue();
    }

    @Override
    public String help() {
        return "shuffle the queue, or a playlist if specified";
    }

    private String shufflePlaylist(CommandContext cc) {
        assert (cc.hasArgs());
        String msg;
        String plName = cc.getArgs()[0];
        if (PlaylistsDatabase.database().containsPlaylist(cc.getAuthor(), plName)) {
            PlaylistsDatabase.database().getPlaylist(cc.getAuthor(), plName).suffle();
            msg = plName + " shuffled !";
        }else{
            msg = plName + " not exist, sorry man.";
        }
        return msg;
    }

    private String shuffleQueue(CommandContext cc) {
        PlaylistsDatabase.database().getQueueOf(cc.getGuild()).suffle();
        return "queue shuffled !";
    }

}
