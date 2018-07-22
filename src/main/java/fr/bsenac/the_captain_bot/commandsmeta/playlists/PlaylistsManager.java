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
package fr.bsenac.the_captain_bot.commandsmeta.playlists;

import fr.bsenac.the_captain_bot.audio.Playlist;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import net.dv8tion.jda.core.entities.User;

/**
 *
 * @author vixa
 */
public class PlaylistsManager {

    private static final PlaylistsManager MANAGER = new PlaylistsManager();

    public static PlaylistsManager getManager() {
        return MANAGER;
    }

    private final Map<User, Map<String, Playlist>> database;

    private PlaylistsManager() {
        database = new HashMap<>();
    }

    public void addUser(User u) {
        Map<String, Playlist> usersPlaylist = new HashMap<>();
        database.put(u, usersPlaylist);
    }

    public boolean containsUser(User u) {
        return database.containsKey(u);
    }

    private void createUserIfNotExist(User u){
            if (!containsUser(u)) {
            addUser(u);
        }
    }
    
    public void createPlaylist(User u, String name) {
        createUserIfNotExist(u);
        database.get(u).put(name, new Playlist(name));
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
}
