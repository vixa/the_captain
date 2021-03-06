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
package fr.bsenac.the_captain_bot.commandsmeta.musics;

import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import fr.bsenac.the_captain_bot.audio.Playlist;
import fr.bsenac.the_captain_bot.commandsmeta.commands.CommandContext;

/**
 *
 * @author vixa
 */
public class LogAudioResultHandler extends BasicAudioLoadResultHandler {

    private CommandContext cc;

    public LogAudioResultHandler(Playlist pl, CommandContext cc) {
        super(pl);
        this.cc = cc;
    }

    @Override
    public void trackLoaded(AudioTrack at) {
        super.trackLoaded(at);
        cc.getChannel()
                .sendMessage(at.getInfo().title
                        + " successfully added to " + pl.getName()
                        + " !").queue();
    }

    @Override
    public void playlistLoaded(AudioPlaylist ap) {
        super.playlistLoaded(ap);
        cc.getChannel().sendMessage(ap.getName()
                + " successfully added to " + pl.getName()).queue();
    }

    @Override
    public void noMatches() {
        cc.getChannel()
                .sendMessage("Hum… I can't found your music, sorry."
                        + "\nMaybe an error in the url?").queue();
    }

    @Override
    public void loadFailed(FriendlyException fe) {
        //TO-DO: enhance error messages (network error ? Not available in the country ?)
        cc.getChannel().sendMessage("I can't load this, sorry.").queue();
    }
}
