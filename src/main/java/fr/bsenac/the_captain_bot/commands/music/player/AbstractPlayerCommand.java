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

import fr.bsenac.the_captain_bot.commands.Command;
import fr.bsenac.the_captain_bot.commandsmeta.commands.CommandContext;
import fr.bsenac.the_captain_bot.audio.PlaylistsDatabase;

/**
 *
 * @author vixa
 */
public abstract class AbstractPlayerCommand extends Command {

    public AbstractPlayerCommand(String name, String... alias) {
        super(name, alias);
    }

    /**
     * Check if we need to use a queue or a valid playlist.
     * @param cc
     * @param index
     * @return true if we use queue or playlist with a valid playlist,
     * else if the playlist not exist
     */
    protected boolean isAQueueOrAValidPlaylist(CommandContext cc, int index) {
        return isNeedToUseQueue(cc, index) ? true
                : isAValidPlaylist(cc, index);
    }

    /**
     * Check if the playlist exist
     * @param cc the context
     * @param index the index of playlist name 
     * @return true if it exist, false else
     */
    protected boolean isAValidPlaylist(CommandContext cc, int index) {
        return PlaylistsDatabase.database()
                .containsPlaylist(cc.getAuthor(), cc.getArgs()[index]);
    }

    /**
     * If there is no args, we use the queue
     * @param cc the context
     * @param index the index of playlist name
     * @return true if we need to use queue, false if we use a playlist 
     */
    protected boolean isNeedToUseQueue(CommandContext cc, int index) {
        return cc.getArgs().length <= index;
    }

}
