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
package fr.bsenac.the_captain_bot.commands.music.playlists;

import fr.bsenac.the_captain_bot.audio.Playlist;
import fr.bsenac.the_captain_bot.commands.Command;
import fr.bsenac.the_captain_bot.commandsmeta.commands.CommandContext;
import fr.bsenac.the_captain_bot.audio.PlaylistsDatabase;

/**
 *
 * @author vixa
 */
public class SaveCurrentPlaylistCommand extends Command {

    private static final String NAME = "save-playlist";
    private static final String NO_PLAYLIST_NAME_SPECIFIED
            = "You must provide a playlist name !";

    public SaveCurrentPlaylistCommand() {
        super(NAME);
    }

    @Override
    public void run(CommandContext cc) {
        String message;
        if (cc.hasArgs()) {
            String plName = cc.getArgs()[0];
            Playlist queue = PlaylistsDatabase.database()
                    .getQueueOf(cc.getGuild());
            Playlist newPl = PlaylistsDatabase.database()
                    .createPlaylist(cc.getAuthor(), plName);
            newPl.becameCloneOf(queue);
            message = plName + " created. The playlist contains all songs "
                    + "currently in the queue.";
        } else {
            message = NO_PLAYLIST_NAME_SPECIFIED;
        }
            cc.getChannel().sendMessage(message).queue();
    }

    @Override
    public String help() {
        return "save the queue in you playlist collection.";
    }

}
