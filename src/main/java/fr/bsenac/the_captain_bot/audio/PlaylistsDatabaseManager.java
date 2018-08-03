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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import net.dv8tion.jda.core.entities.User;

/**
 *
 * @author vixa
 */
public class PlaylistsDatabaseManager extends Thread {

    private static final PlaylistsDatabaseManager DATABASE = new PlaylistsDatabaseManager();

    public static PlaylistsDatabaseManager getManager() {
        return DATABASE;
    }

    private PlaylistsDatabaseManager() {
        this.lastActions = Collections.synchronizedMap(new HashMap<>());
    }

    private static final String SAVE_FOLDER = "dats";

    private static final TimeUnit TIME_UNIT = TimeUnit.MINUTES;
    private static final int WAIT_TIME = 1, UNLOAD_TIME = 20;
    private final Map<User, Long> lastActions;

    public void update(User u) {
        if (lastActions.containsKey(u)) {
            lastActions.replace(u, System.currentTimeMillis());
        } else {
            PlaylistsDatabase.getManager().loadUser(SAVE_FOLDER, u);
            lastActions.put(u, System.currentTimeMillis());
        }
    }

    @Override
    public void run() {
        System.out.println("Starting PlaylistsDatabaseManager…");
        while (true) {
            HashSet<User> toRemove = new HashSet<>();
            lastActions.keySet().forEach(u -> {
                long lastAction
                        = System.currentTimeMillis() - lastActions.get(u);
                lastAction = TIME_UNIT.convert(lastAction, TimeUnit.MILLISECONDS);
                if (lastAction <= WAIT_TIME || lastAction >= UNLOAD_TIME) {
                    PlaylistsDatabase.getManager().save(SAVE_FOLDER, u);
                }
                if (lastAction > UNLOAD_TIME) {
                    PlaylistsDatabase.getManager().unloadUser(u);
                    toRemove.add(u);
                }
            });
            toRemove.forEach(u -> {
                lastActions.remove(u);
            });
            sleep();
        }
    }

    private void sleep() {
        try {
            Thread.sleep(TimeUnit.MILLISECONDS.convert(WAIT_TIME, TIME_UNIT));
        } catch (InterruptedException ex) {
            System.err.println("FATAL ERROR: PlaylistsDatabaseManager Interrupted.");
        }
    }

}
