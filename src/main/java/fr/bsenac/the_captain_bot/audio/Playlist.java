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
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 *
 * @author vixa
 */
public class Playlist {

    private final List<AudioTrack> tracks;
    private int position;

    ;
    public Playlist() {
        this.tracks = new ArrayList<>();
        position = -1;
    }

    public AudioTrack next() {
        if (++position >= tracks.size()) {
            throw new NoSuchElementException("No more element");
        }
        return current();
    }

    public AudioTrack current() {
        return tracks.get(position);
    }

    public boolean hasNext() {
        return position + 1 < tracks.size();
    }

    public void add(AudioTrack track) {
        tracks.add(track);
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
        for (Integer i : indexes) {
            if (isInList(i)) {
                tracks.add(this.tracks.get(i));
            }
        }
        return tracks;
    }

    public void repeat() {
        position = -1;
    }

    public String list() {
        StringBuilder list = new StringBuilder();
        for (int i = 0; i < tracks.size(); ++i) {
            list.append(i).append(": ").append(tracks.get(i).getInfo().title)
                    .append("\n");
        }
        return list.toString();
    }

    private boolean isInList(int index) {
        return index < tracks.size() && index >= 0 && !tracks.isEmpty();
    }
}
