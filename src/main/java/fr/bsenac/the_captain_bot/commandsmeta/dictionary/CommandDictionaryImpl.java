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
package fr.bsenac.the_captain_bot.commandsmeta.dictionary;

import fr.bsenac.the_captain_bot.commands.Command;
import fr.bsenac.the_captain_bot.commands.NullCommand;
import fr.bsenac.the_captain_bot.commandsmeta.commands.CommandContext;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 *
 * @author vixa
 */
public class CommandDictionaryImpl implements CommandDictionary {

    private final Map<String, Command> dictionary;

    private static final Command NULL = new NullCommand();

    public CommandDictionaryImpl() {
        dictionary = new TreeMap<>();
    }

    @Override
    public Command get(String s) {
        if (dictionary.containsKey(s)) {
            return dictionary.get(s);
        }
        return NULL;
    }

    @Override
    public Command get(CommandContext cc) {
        return get(cc.getCommand());
    }

    @Override
    public CommandDictionary add(Command c) {
        dictionary.put(c.getName(), c);
        for (String alias : c.getAlias()) {
            dictionary.put(alias, c);
        }
        return this;
    }

    @Override
    public boolean remove(Command c) {
        List<String> toRemove = new ArrayList<>();
        dictionary.forEach((String s, Command com) -> {
            if (c.equals(com)) {
                toRemove.add(s);
            }
        });
        toRemove.forEach(s -> dictionary.remove(s));
        return !toRemove.isEmpty();
    }

    @Override
    public boolean removeAlias(String cName) {
        if (dictionary.containsKey(cName)) {
            dictionary.remove(cName);
            return true;
        }
        return false;
    }

    @Override
    public boolean remove(String cName) {
        if (dictionary.containsKey(cName)) {
            Command c = dictionary.get(cName);
            return remove(c);
        }
        return false;
    }

    @Override
    public Iterator<Command> iterator() {
        final Set<Command> commands
                = new TreeSet<>(dictionary.values());
        return commands.iterator();
    }

}
