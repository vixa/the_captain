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
package fr.bsenac.the_captain_bot.commandsmeta.commands;

import fr.bsenac.the_captain_bot.audio.PlaylistsDatabaseManager;
import fr.bsenac.the_captain_bot.commandsmeta.dictionary.CommandDictionary;
import fr.bsenac.the_captain_bot.commandsmeta.dictionary.CommandDictionaryImpl;
import fr.bsenac.the_captain_bot.commands.music.player.AddCommand;
import fr.bsenac.the_captain_bot.commands.music.player.SkipCommand;
import fr.bsenac.the_captain_bot.commands.music.player.RemoveCommand;
import fr.bsenac.the_captain_bot.commands.music.player.ListCommand;
import fr.bsenac.the_captain_bot.commands.music.player.PlayCommand;
import fr.bsenac.the_captain_bot.commands.Command;
import fr.bsenac.the_captain_bot.commands.music.*;
import fr.bsenac.the_captain_bot.commands.music.playlists.*;

/**
 *
 * @author vixa
 */
public final class CommandsIndex {

    private final CommandDictionary dictionary;

    public CommandsIndex() {
        //Initialisation of index
        dictionary = new CommandDictionaryImpl();

        //Fill the index
        dictionary.add(new JoinCommand()).add(new AddCommand())
                .add(new PlayCommand()).add(new SkipCommand())
                .add(new ListCommand()).add(new RemoveCommand())
                .add(new CreatePlaylistCommand())
                .add(new DeletePlaylistCommand())
                .add(new ListPlaylistCommand())
                .add(new SaveCurrentPlaylistCommand());
    }

    public void findAndExecute(CommandContext cc) {
        Command c = dictionary.get(cc);
        PlaylistsDatabaseManager.getManager().update(cc.getAuthor());
        c.run(cc);
        cc.executeNextCommand();
    }

    private static final CommandsIndex INDEX = new CommandsIndex();

    public static CommandsIndex getIndex() {
        return INDEX;
    }

}
