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

import fr.bsenac.the_captain_bot.audio.Playlist;
import fr.bsenac.the_captain_bot.commandsmeta.commands.CommandContext;
import fr.bsenac.the_captain_bot.audio.PlaylistsDatabase;

/**
 *
 * @author vixa
 */
public class ListCommand extends AbstractPlayerCommand {

    private static final String NAME = "list";

    public ListCommand() {
        super(NAME);
    }

    @Override
    public void run(CommandContext cc) {
        PlaylistsDatabase.getManager().getLock(cc.getAuthor()).lock();
        try {
            String message;
            final int plNameIndex = 0;
            if (isAQueueOrAValidPlaylist(cc, plNameIndex)) {
                Playlist pl;
                PlaylistsDatabase manager = PlaylistsDatabase.getManager();
                if (isNeedToUseQueue(cc, plNameIndex)) {
                    pl = manager.getQueueOf(cc.getGuild());
                } else {
                    String plName = cc.getArgs()[plNameIndex];
                    pl = manager.getPlaylist(cc.getAuthor(), plName);
                }
                String list = pl.list();
                message = "Playlist:\n" + list;
            } else {
                message = "Hum… this playlist not exist, "
                        + "I can't list the songs inside the void !";
            }
            cc.getChannel().sendMessage(message).queue();
        } finally {
            PlaylistsDatabase.getManager().getLock(cc.getAuthor()).unlock();
        }
    }

    @Override
    public String help() {
        return "list all tracks in the playlist";
    }

}
