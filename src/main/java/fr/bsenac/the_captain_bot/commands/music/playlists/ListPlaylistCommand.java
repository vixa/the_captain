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
import java.util.Collection;

/**
 *
 * @author vixa
 */
public class ListPlaylistCommand extends Command {

    private static final String NAME = "list-playlist", ALIAS = "list-playlists";
    private static final String NO_PLAYLISTS = "You have no playlist. "
            + "But, you can still create one :D";

    public ListPlaylistCommand() {
        super(NAME, ALIAS);
    }

    @Override
    public void run(CommandContext cc) {
        StringBuilder message = new StringBuilder();
        if (PlaylistsDatabase.getManager().containsUser(cc.getAuthor())) {

            usePlaylists(cc, message);
        } else {
            message.append(NO_PLAYLISTS);
        }
        cc.getChannel().sendMessage(message).queue();
    }

    @Override
    public String help() {
        return "list all your playlists.";
    }

    private void usePlaylists(CommandContext cc, StringBuilder message) {
        Collection<Playlist> playlists
                = PlaylistsDatabase.getManager().getPlaylists(cc.getAuthor());

        if (playlists.size() > 0) {
            listPlaylists(cc, message, playlists);
        } else {
            message.append(NO_PLAYLISTS);
        }
    }

    private void listPlaylists(CommandContext cc, StringBuilder message, 
            Collection<Playlist> playlists) {
        message.append("All your playlists:\n");
        playlists.forEach(p -> {
            message.append(p.getName()).append("\n");

        });
    }

}
