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
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;
import net.dv8tion.jda.core.entities.MessageChannel;

/**
 *
 * @author vixa
 */
public class TrackScheduler extends AudioEventAdapter {

    private final AudioPlayer player;
    private final MessageChannel chan;
    private final Queue<AudioTrack> tracks;

    TrackScheduler(AudioPlayer player, MessageChannel chan) {
        this.player = player;
        this.chan = chan;
        tracks = new ConcurrentLinkedDeque<>();
    }

    public void queue(AudioTrack track) {
        if (track != null) {
            tracks.add(track);
        }
    }

    /**
     * Start playing the playlist
     *
     * @return true if started, false else
     */
    public boolean playNextTrack() {
        if (isReadyToPlay()) {
            AudioTrack track = tracks.remove();
            player.playTrack(track);
            chan.sendMessage("Now playing " + track.getInfo().title).queue();
            return true;
        } else {
            return false;
        }
    }

    public boolean isReadyToPlay() {
        return !tracks.isEmpty();
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        playNextTrack();
    }
}
