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
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.MessageChannel;

/**
 *
 * @author vixa
 */
public class TrackSchedulersManager implements Iterable<Guild> {

    //Singleton bloc
    private static final TrackSchedulersManager MANAGER = new TrackSchedulersManager();

    public static TrackSchedulersManager getManager() {
        return MANAGER;
    }

    //Object bloc
    private final Map<Guild, TrackScheduler> schedulers;

    public TrackSchedulersManager() {
        schedulers = new HashMap<>();
    }

    public TrackScheduler activate(Guild g, MessageChannel chan) {
        AudioPlayer player = PlayerManager.get().createPlayer();
        TrackScheduler ts = new TrackScheduler(player, chan, g);
        player.addListener(ts);
        g.getAudioManager().setSendingHandler(new AudioPlayerSendHandler(player));
        schedulers.put(g, ts);
        return ts;
    }

    /**
     * Get the instance of TrackScheduler for the specified guild. If it not
     * exist. Suppose you check than the guild have a TrackScheduler
     *
     * @param g the guild
     * @return the TrackScheduler
     */
    public TrackScheduler getSchedulerOf(Guild g) {
        return schedulers.get(g);
    }

    public TrackScheduler getOrCreate(Guild g, MessageChannel chan) {
        if (isUp(g)) {
            return getSchedulerOf(g);
        } else {
            return activate(g, chan);
        }
    }

    /**
     * Check if the scheduler is up for a guild
     *
     * @param g the guild
     * @return true if the sceduler is up
     */
    public boolean isUp(Guild g) {
        return schedulers.containsKey(g);
    }

    /**
     * Check if the scheduler is not playing in the void
     *
     * @param g the guild
     * @return true if there is User inside the voice channel
     */
    public boolean isActive(Guild g) {
        return isUp(g)
                ? g.getAudioManager().getConnectedChannel().getMembers().size() > 1
                : false;
    }

    public boolean desactivate(Guild g) {
        if (isUp(g)) {
            getSchedulerOf(g).stop();
            g.getAudioManager().closeAudioConnection();
            schedulers.remove(g);
            return true;
        }
        return false;
    }

    @Override
    public Iterator<Guild> iterator() {
        return schedulers.keySet().iterator();
    }

}
