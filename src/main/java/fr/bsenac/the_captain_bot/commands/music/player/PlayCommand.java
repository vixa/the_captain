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
import fr.bsenac.the_captain_bot.audio.TrackScheduler;
import fr.bsenac.the_captain_bot.audio.TrackSchedulersManager;
import fr.bsenac.the_captain_bot.commandsmeta.commands.CommandContext;
import fr.bsenac.the_captain_bot.audio.PlaylistsDatabase;

/**
 *
 * @author vixa
 */
public class PlayCommand extends AbstractPlayerCommand {

    public static final String NAME = "play";

    public PlayCommand() {
        super(NAME);
    }

    @Override
    public void run(CommandContext cc) {
        //If it not active, you have not join a channel
        if (cc.hasArgs()) {
            loadPlaylist(cc);
        }
        if (TrackSchedulersManager.getManager().isUp(cc.getGuild())) {
            play(cc);
        } else {
            cc.getChannel().sendMessage("Please join a channel before play.").queue();
        }
    }

    @Override
    public String help() {
        return "play the specified playlist, "
                + "or the current playlist if no one is specified.";
    }

    private void loadPlaylist(CommandContext cc) {
        final int plNamePosition = 0;
        String plName = cc.getArgs()[plNamePosition];
        if (isAValidPlaylist(cc, plNamePosition)) {
            PlaylistsDatabase.database().getLock(cc.getAuthor()).lock();
            try {
                Playlist queue = PlaylistsDatabase.database()
                        .getQueueOf(cc.getGuild());
                Playlist pl = PlaylistsDatabase.database()
                        .getPlaylist(cc.getAuthor(), plName);
                queue.becameCloneOf(pl);
            } finally {
                PlaylistsDatabase.database().getLock(cc.getAuthor()).unlock();
            }
        } else {
            String message = plName + " is not a valid playlist. "
                    + "Starting playing the queue.";
            cc.getChannel().sendMessage(message).queue();
        }
    }

    private void play(CommandContext cc) {
        TrackScheduler ts = TrackSchedulersManager.getManager()
                .getSchedulerOf(cc.getGuild());
        if (ts.isReadyToPlay()) {
            ts.startPlaying();
        } else {
            String msg = "I can't play more, there isn't a next track.";
            cc.getChannel().sendMessage(msg).queue();
        }
    }
}
