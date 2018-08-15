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

import fr.bsenac.the_captain_bot.tools.IOTools;
import fr.bsenac.the_captain_bot.waiter.Waiter;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.User;

/**
 *
 * @author vixa
 */
public class PlaylistsDatabase {

    private static final PlaylistsDatabase MANAGER = new PlaylistsDatabase();

    public static PlaylistsDatabase database() {
        return MANAGER;
    }

    private final Map<User, Map<String, Playlist>> database;
    //UserLocks is telling if User is loading or not
    private final Map<User, Lock> userLocks;
    private final Map<Guild, Playlist> queues;

    private PlaylistsDatabase() {
        database = Collections.synchronizedMap(new HashMap<>());
        queues = Collections.synchronizedMap(new HashMap<>());
        userLocks = Collections.synchronizedMap(new HashMap<>());
    }

    public void addUser(User u) {
        Map<String, Playlist> usersPlaylist = new HashMap<>();
        database.put(u, usersPlaylist);
    }

    public boolean containsUser(User u) {
        return database.containsKey(u);
    }

    private void createUserIfNotExist(User u) {
        if (!containsUser(u)) {
            addUser(u);
        }
    }

    public Playlist createPlaylist(User u, String name) {
        createUserIfNotExist(u);
        Playlist pl = new Playlist(name);
        pushPlaylist(u, pl);
        return pl;
    }

    private void pushPlaylist(User u, Playlist pl) {
        database.get(u).put(pl.getName(), pl);
    }

    public boolean removePlaylist(User u, String name) {
        if (containsPlaylist(u, name)) {
            database.get(u).remove(name);
            return true;
        }
        return false;
    }

    public boolean containsPlaylist(User u, String name) {
        if (!containsUser(u)) {
            return false;
        }
        return database.get(u).containsKey(name);
    }

    public Playlist getPlaylist(User u, String name) {
        return database.get(u).get(name);
    }

    public Collection<Playlist> getPlaylists(User u) {
        return database.get(u).values();
    }

    /**
     * Get the queue playlist of the guild if it exist, or create one
     *
     * @param g the guild
     * @return the queue
     */
    public Playlist getQueueOf(Guild g) {
        if (queues.containsKey(g)) {
            return queues.get(g);
        } else {
            Playlist pl = new Playlist("queue");
            queues.put(g, pl);
            return pl;
        }
    }

    /*
     * IO PART
     */
    /**
     * Save all playlists
     *
     * @param folder the save folder
     */
    public void saveAll(String folder) {
        // Save Format: folder/user/playlistname.txt

        IOTools.createFolder(folder);
        database.keySet().forEach(u -> {
            save(folder, u);
        });
    }

    /**
     * Save a user
     *
     * @param folder the save folder of all datas
     * @param u the user to save
     */
    public void save(String folder, User u) {
        getLock(u).lock();
        try {
            String path = folder + File.separator + u.getId();
            IOTools.createFolder(path);
            database.get(u).values().forEach(playlist -> {
                playlist.save(path);
            });
        } finally {
            getLock(u).unlock();
        }
    }

    /**
     * Unload a user from the ram. Keep warning to save user's data before
     * unload it.
     *
     * @param u the user to unload
     */
    public void unloadUser(User u) {
        database.remove(u);
    }

    /**
     * Load an user from is folder
     *
     * @param folder the folder where users data are stored
     * @param u the user to load
     */
    public void loadUser(String folder, User u) {
        Lock wait = new ReentrantLock();
        Condition c = wait.newCondition();
        wait.lock();
        Thread t = new Thread(() -> {
            wait.lock();
            try {
                getLock(u).lock();
                c.signalAll();
            } finally {
                wait.unlock();
            }
            String path = folder + File.separator + u.getId();
            File f = new File(path);
            if (f.exists() && f.isDirectory()) {
                try {
                    loadUserData(f, u);
                } catch (IOException ex) {
                    Logger.getLogger(PlaylistsDatabase.class.getName()).log(Level.SEVERE, null, ex);
                } finally {
                    getLock(u).unlock();
                }
            } else {
                getLock(u).unlock();
            }
        });
        t.start();
        try {
            c.await();
        } catch (InterruptedException ex) {
            Logger.getLogger(PlaylistsDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Load user's data from a directory
     *
     * @param dir user's directory
     * @param u the user
     */
    private void loadUserData(File dir, User u)
            throws IOException {
        Waiter w = new Waiter();
        addUser(u);
        String[] playlists = dir.list();
        for (String playlist : playlists) {
            String path = dir.getPath() + File.separator + playlist;
            File f = new File(path);
            if (f.isFile()) {
                Playlist pl = Playlist.load(path, w);
                pushPlaylist(u, pl);
            }
        }
        wait(w);
    }

    private void wait(Waiter w) {
        try {
            w.get();
        } catch (InterruptedException | ExecutionException ex) {
            Logger.getLogger(PlaylistsDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Lock getLock(User u) {
        if (!userLocks.containsKey(u)) {
            userLocks.put(u, new ReentrantLock());
        }
        return userLocks.get(u);
    }
}
