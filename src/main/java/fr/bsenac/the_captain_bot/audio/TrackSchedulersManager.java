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
import java.util.Map;
import net.dv8tion.jda.core.entities.Guild;

/**
 *
 * @author vixa
 */
public class TrackSchedulersManager {

    //Singleton bloc
    private static final TrackSchedulersManager MANAGER = new TrackSchedulersManager();
    
    public static TrackSchedulersManager get() {
        return MANAGER;
    }

    //Object bloc
    private final Map<Guild, TrackScheduler> schedulers;
    
    public TrackSchedulersManager() {
        schedulers = new HashMap<>();
    }
    
    private TrackScheduler activate(Guild g) {
        AudioPlayer player = PlayerManager.get().createPlayer();
        TrackScheduler ts = new TrackScheduler(player);
        g.getAudioManager().setSendingHandler(new AudioPlayerSendHandler(player));
        schedulers.put(g, ts);
        return ts;
    }
    
    /**
     * Get the instance of TrackScheduler for the specified guild.
     * If it not exist, it will create one.
     * @param g the guild
     * @return the TrackScheduler
     */
    public TrackScheduler get(Guild g) {
        if (isActive(g)) {
            return schedulers.get(g);
        }else{
            return activate(g);
        }
    }
    
    public boolean isActive(Guild g) {
        return schedulers.containsKey(g);
    }
    
    public boolean desactivate(Guild g) {
        if (isActive(g)) {
            schedulers.remove(g);
            return true;
        }
        return false;
    }
    
}
