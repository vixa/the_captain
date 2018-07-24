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
import fr.bsenac.the_captain_bot.commands.Command;
import fr.bsenac.the_captain_bot.commandsmeta.commands.CommandContext;
import fr.bsenac.the_captain_bot.commandsmeta.playlists.PlaylistsManager;

/**
 *
 * @author vixa
 */
public class ListCommand extends Command {

    private static final String NAME = "list";

    public ListCommand() {
        super(NAME);
    }

    @Override
    public void run(CommandContext cc) {
        String message;
        if (isQueueOrAValidPlaylist(cc)) {
            Playlist pl;
            PlaylistsManager manager = PlaylistsManager.getManager();
            if (isQueue(cc)) {
                pl = manager.getQueueOf(cc.getGuild());
            } else {
                String plName = cc.getArgs()[0];
                pl = manager.getPlaylist(cc.getAuthor(), plName);
            }
            String list = pl.list();
            message = "Playlist:\n" + list;
        } else {
            message = "Hum… this playlist not exist, "
                    + "I can't list the songs inside the void !";
        }
        cc.getChannel().sendMessage(message).queue();
    }

    @Override
    public String help() {
        return "list all tracks in the playlist";
    }

    private boolean isQueue(CommandContext cc) {
        return cc.getArgs().length == 0;
    }

    private boolean isQueueOrAValidPlaylist(CommandContext cc) {
        return isQueue(cc) ? true
                : isAValidPlaylist(cc);
    }

    private boolean isAValidPlaylist(CommandContext cc) {
        return PlaylistsManager.getManager()
                .containsPlaylist(cc.getAuthor(), cc.getArgs()[0]);
    }

}
