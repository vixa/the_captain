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
package fr.bsenac.the_captain_bot.audio;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.MessageChannel;

/**
 *
 * @author vixa
 */
public class TrackScheduler extends AudioEventAdapter {

    private final AudioPlayer player;
    private final MessageChannel chan;
    private final Playlist playlist;
    private final Guild guild;
    private boolean repeat;

    TrackScheduler(AudioPlayer player, MessageChannel chan, Guild g) {
        this.player = player;
        this.chan = chan;
        guild = g;
        playlist = PlaylistsDatabase.database().getQueueOf(g);
        repeat = false;
    }

    /**
     * Start playing the playlist
     *
     * @return true if started, false else
     */
    public boolean playNextTrack() {
        if (isReadyToPlay()) {
            player.playTrack(playlist.next());
            return true;
        }
        return false;
    }

    /**
     * Pause the player if it not already paused
     * @return true if the player is now paused
     */
    public boolean pause() {
        if (!player.isPaused()) {
            player.setPaused(true);
            return true;
        }
        return false;
    }

    /**
     * Resume the player if it is paused
     * @return true if the player is now resumed
     */
    public boolean resume() {
        if (player.isPaused()) {
            player.setPaused(false);
            return true;
        }
        return false;
    }

    public void setRepeat(boolean repeat) {
        this.repeat = repeat;
    }

    public boolean isReadyToPlay() {
        return playlist.hasNext();
    }

    @Override
    public void onTrackStart(AudioPlayer player, AudioTrack track) {
        chan.sendMessage("Now playing " + track.getInfo().title).queue();
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        if (endReason.mayStartNext) {
            if (isReadyToPlay()) {
                playNextTrack();
            } else if (repeat) {
                playlist.repeat();
                playNextTrack();
            } else {
                chan.sendMessage("We reach the end of the playlist !").queue();
            }
        }
    }

    @Override
    public void onTrackException(AudioPlayer player, AudioTrack track, FriendlyException exception) {
    }

    @Override
    public void onTrackStuck(AudioPlayer player, AudioTrack track, long thresholdMs) {
    }
}
