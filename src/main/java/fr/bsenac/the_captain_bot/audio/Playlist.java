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

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import fr.bsenac.the_captain_bot.commandsmeta.musics.BasicAudioLoadResultHandler;
import fr.bsenac.the_captain_bot.waiter.Waiter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author vixa
 */
public class Playlist {

    private final List<AudioTrack> tracks;
    private int position;
    private final String name;

    public Playlist(String name) {
        this.name = name;
        this.tracks = Collections.synchronizedList(new ArrayList<>());
        position = -1;
    }

    public AudioTrack next() {
        if (++position >= tracks.size()) {
            throw new NoSuchElementException("No more element");
        }
        return current();
    }

    public AudioTrack current() {
        return get(position);
    }
    
    public AudioTrack get(int i){
        return tracks.get(i).makeClone();
    }
    
    public void suffle(){
        Collections.shuffle(tracks);
        restart();
    }

    public boolean hasNext() {
        return position + 1 < tracks.size();
    }

    public void add(AudioTrack track) {
        if (track != null) {
            tracks.add(track);
        }
    }

    /**
     * Add all songs of a playlist
     *
     * @param pl
     */
    public void add(Playlist pl) {
        synchronized (pl.tracks) {
            pl.tracks.forEach(t -> {
                this.tracks.add(t);
            });
        }
    }

    /**
     * All of the tracks of this playlist are removed, and put the tracks of pl
     * playlist inside this. The playlist restart.
     *
     * @param pl the playlist to
     */
    public void becameCloneOf(Playlist pl) {
        tracks.clear();
        add(pl);
        position = -1;
    }

    public void jump(int index) {
        if (isInList(index)) {
            position = index;
        }
    }

    public boolean remove(AudioTrack track) {
        return tracks.remove(track);
    }

    public boolean remove(int index) {
        if (isInList(index)) {
            tracks.remove(index);
            return true;
        }
        return false;
    }

    public Set<AudioTrack> getTracks(Set<Integer> indexes) {
        final Set<AudioTrack> tracks = new HashSet<>();
        synchronized (this.tracks) {
            indexes.stream().filter((i) -> (isInList(i))).forEachOrdered((i) -> {
                tracks.add(this.tracks.get(i));
            });
        }
        return tracks;
    }

    public void restart() {
        position = -1;
    }

    public String list() {
        StringBuilder list = new StringBuilder();
        synchronized (tracks) {
            for (int i = 0; i < tracks.size(); ++i) {
                list.append(i).append(": ").append(tracks.get(i).getInfo().title)
                        .append("\n");
            }
        }
        return list.toString();
    }

    private boolean isInList(int index) {
        return index < tracks.size() && index >= 0 && !tracks.isEmpty();
    }

    public String getName() {
        return name;
    }

    /*
     * IO code
     */
    /**
     * Save the current playlist in a file
     *
     * @param folder the folder of the playlist
     */
    public void save(String folder) {
        String path = folder + File.separator + name;
        File save = new File(path);
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new FileWriter(save));
            write(bw);
        } catch (IOException ex) {
            Logger.getLogger(Playlist.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            close(bw);
        }
    }

    private void write(BufferedWriter bw) throws IOException {
        synchronized (tracks) {
            for (AudioTrack t : tracks) {
                bw.write(t.getInfo().uri);
                bw.newLine();
            }
        }
    }

    private static void close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException ex) {
                Logger.getLogger(Playlist.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Create a playlist from a saved playlist
     *
     * @param file the file of the playlist
     * @param w
     * @return the playlist created
     */
    public static Playlist load(String file, Waiter w) {
        File f = new File(file);
        Playlist pl = new Playlist(f.getName());
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(f));
            read(br, pl, w);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Playlist.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            close(br);
        }
        return pl;
    }

    private static void read(BufferedReader br, Playlist pl, Waiter w) {
        br.lines().forEach(url -> {
            Future<Void> result = PlayerManager.get().loadItem(url,
                    new BasicAudioLoadResultHandler(pl));
            w.add(result);
        });
    }

}
