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

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.dv8tion.jda.core.entities.Guild;

/**
 * This class is a thread in charge of disconnect from a audio channel after a
 * time without user in the audio channel
 *
 * @author vixa
 */
public class AudioConnectionsManager extends Thread {

    private static final AudioConnectionsManager MANAGER
            = new AudioConnectionsManager();

    public static AudioConnectionsManager getManager() {
        return MANAGER;
    }

    private AudioConnectionsManager() {
        toRemove = Collections.synchronizedSet(new HashSet<>());
    }

    private static final TimeUnit TIME_UNIT = TimeUnit.MINUTES;
    private static final int WAIT_TIME = 1;
    private final Set<Guild> toRemove;

    @Override
    public void run() {
        System.out.println("[CAPTAIN] starting AudioConnectionsManager");
        while (true) {
            deleteInactives();
            fillInactives();
            sleep();
        }
    }

    private void deleteInactives() {
        toRemove.forEach(g -> {
            if (!TrackSchedulersManager.getManager().isActive(g)) {
                System.out.println(g.getName() + " disconnected");
                TrackSchedulersManager.getManager().desactivate(g);
            }
        });
        toRemove.clear();
    }

    private void fillInactives() {
        TrackSchedulersManager.getManager().forEach(g -> {
            if (!TrackSchedulersManager.getManager().isActive(g)) {
                System.out.println(g.getName() + " added to inactives");
                toRemove.add(g);
            }
        });
    }

    private void sleep() {
        long wait = TimeUnit.MILLISECONDS.convert(WAIT_TIME, TIME_UNIT);
        try {
            Thread.sleep(wait);
        } catch (InterruptedException ex) {
            Logger.getLogger(AudioConnectionsManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
