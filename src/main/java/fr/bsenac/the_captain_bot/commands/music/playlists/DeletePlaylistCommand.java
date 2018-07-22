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

import fr.bsenac.the_captain_bot.commands.Command;
import fr.bsenac.the_captain_bot.commandsmeta.commands.CommandContext;
import fr.bsenac.the_captain_bot.commandsmeta.playlists.PlaylistsManager;

/**
 *
 * @author vixa
 */
public class DeletePlaylistCommand extends Command {

    private static final String NAME = "delete-playlist";

    public DeletePlaylistCommand() {
        super(NAME);
    }

    @Override
    public void run(CommandContext cc) {
        if (cc.getArgs().length > 0) {
            deletePlaylist(cc);
        } else {
            String message = "I delete what ? TELL ME !";
            cc.getChannel().sendMessage(message).queue();
        }
    }

    @Override
    public String help() {
        return "delete the specified playlist";
    }

    private void deletePlaylist(CommandContext cc) {
        String playlistName = cc.getArgs()[0];
        String message;
        if (PlaylistsManager.getManager()
                .containsPlaylist(cc.getAuthor(), playlistName)) {
            PlaylistsManager.getManager().removePlaylist(cc.getAuthor(), playlistName);
            message = "I removed " + playlistName + ", you will never heard it anymore.";
        } else {
            message = "I don't know this playlist, sorry.";
        }
        cc.getChannel().sendMessage(message).queue();
    }

}
