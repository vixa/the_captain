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
package fr.bsenac.the_captain_bot.commands.music;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import fr.bsenac.the_captain_bot.audio.PlayerManager;
import fr.bsenac.the_captain_bot.audio.TrackScheduler;
import fr.bsenac.the_captain_bot.audio.TrackSchedulersManager;
import fr.bsenac.the_captain_bot.commands.Command;
import fr.bsenac.the_captain_bot.commandsmeta.CommandContext;
import java.util.concurrent.Future;

/**
 * Add is a command to add a music in the current playlist, or in a specified
 * playlist
 *
 * @author vixa
 */
public class AddCommand extends Command {

    private static final String NAME = "add";

    public AddCommand() {
        super(NAME);
    }

    @Override
    public void run(CommandContext cc) {
        TrackSchedulersManager manager = TrackSchedulersManager.get();
        if (cc.getArgs().length >= 1) {
            TrackScheduler scheduler = manager.get(cc.getGuild());
            Future<Void> wait = PlayerManager.get().loadItem(cc.getArgs()[0],
                    new AudioLoadResultHandler() {
                @Override
                public void trackLoaded(AudioTrack at) {
                    scheduler.queue(at);
                    cc.getChannel()
                            .sendMessage(at.getInfo().title
                                    + " succesfuly added to the queue !")
                            .queue();
                }

                @Override
                public void playlistLoaded(AudioPlaylist ap) {
                    ap.getTracks().forEach(at -> {
                        scheduler.queue(at);
                    });
                    cc.getChannel().sendMessage(ap.getName()
                            + "succesfuly added").queue();
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
            });
            while (!wait.isDone());
            //}else if(cc.getArgs().length > 1){
            //TO-DO, now we just ignore this case
        } else {
            cc.getChannel().sendMessage("Error, no music specified.").queue();
        }
    }

    @Override
    public String help() {
        return "Add a song in the current playlist, or in a specified playlist."
                + "\nUsages:\nadd [song url] [optional: playlist name]";
    }

}
