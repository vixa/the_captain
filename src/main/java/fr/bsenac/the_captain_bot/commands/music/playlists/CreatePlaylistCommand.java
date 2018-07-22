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
import fr.bsenac.the_captain_bot.commandsmeta.CommandContext;
import fr.bsenac.the_captain_bot.commandsmeta.PlaylistsManager;
import net.dv8tion.jda.core.entities.User;

/**
 *
 * @author vixa
 */
public class CreatePlaylistCommand extends Command {

    private static final String NAME = "create-playlist";

    public CreatePlaylistCommand() {
        super(NAME);
    }

    @Override
    public void run(CommandContext cc) {
        if (cc.getArgs().length > 0) {
            createPlaylist(cc);
        } else {
            cc.getChannel().sendMessage("Hum… you don't set a playlist name ! "
                    + "Please specify the name, like this: " + NAME + " name")
                    .queue();
        }
    }

    @Override
    public String help() {
        return "create a new playlist";
    }

    private void createPlaylist(CommandContext cc) {
        PlaylistsManager manager = PlaylistsManager.getManager();
        User u = cc.getAuthor();
        if (!manager.containsUser(u)) {
            manager.addUser(u);
        }
        String playlistName = cc.getArgs()[0];
        manager.createPlaylist(u, playlistName);
        cc.getChannel().sendMessage(playlistName + " is now created ! :D")
                .queue();
    }

}
