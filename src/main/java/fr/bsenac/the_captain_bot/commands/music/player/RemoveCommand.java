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
package fr.bsenac.the_captain_bot.commands.music.player;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import fr.bsenac.the_captain_bot.audio.Playlist;
import fr.bsenac.the_captain_bot.audio.TrackScheduler;
import fr.bsenac.the_captain_bot.audio.TrackSchedulersManager;
import fr.bsenac.the_captain_bot.commands.Command;
import fr.bsenac.the_captain_bot.commandsmeta.CommandContext;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author vixa
 */
public class RemoveCommand extends Command {

    private static final String NAME = "remove";

    public RemoveCommand() {
        super(NAME);
    }

    @Override
    public void run(CommandContext cc) {
        if (cc.getArgs().length > 0) {
            TrackScheduler scheduler = TrackSchedulersManager.getSchedulerManager()
                    .getOrCreate(cc.getGuild(), cc.getChannel());
            HashSet<Integer> toRemove = fillRemoveSet(cc);
            removeAll(cc, scheduler.getPlaylist(), toRemove);
        }
    }

    private void removeAll(CommandContext cc, Playlist pl, Set<Integer> indexes) {
        Set<AudioTrack> tracksToRemove = pl.getTracks(indexes);
        final StringBuilder message = new StringBuilder();
        tracksToRemove.forEach(t -> {
            if (pl.remove(t)) {
                message.append(t.getInfo().title).append(" deleted !\n");
            } else {
                message.append("Unknown error when removing ").append(t.getInfo().title);
            }
        });
        cc.getChannel().sendMessage(message).queue();

    }

    private HashSet<Integer> fillRemoveSet(CommandContext cc) {
        HashSet<Integer> toRemove = new HashSet<>();
        for (String s : cc.getArgs()) {
            try {
                toRemove.add(Integer.parseInt(s));
            } catch (NumberFormatException e) {
                cc.getChannel().sendMessage(s + " is not a track number !").queue();
            }
        }
        return toRemove;
    }

    @Override
    public String help() {
        return "remove the specified tracks with there numbers";
    }

}
