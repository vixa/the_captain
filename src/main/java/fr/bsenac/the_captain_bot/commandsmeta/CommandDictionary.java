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
package fr.bsenac.the_captain_bot.commandsmeta;

import fr.bsenac.the_captain_bot.commands.Command;

/**
 *
 * @author vixa
 */
public interface CommandDictionary{
    /**
     * Get a command from a command name or allias.
     * @param s the command name
     * @return the associated command, or a NullCommand if not found
     */
    Command get(String s);
    
    /**
     * Get a command from a CommandContext.
     * @param cc the command context
     * @return the associated command, or a NullCommand if not found.
     */
    Command get(CommandContext cc);
    
    /**
     * Add a command to the dictionnary. 
     * @param c the command to add
     */
    CommandDictionary add(Command c);
    
    /**
     * Remove a command
     * @param c the command to remove
     * @return true if the command is succesfuly removed, false else
     */
    boolean remove(Command c);
    
    
    /**
     * Remove a command from an name or alias.
     * @param cName the name of the command to remove
     * @return true if the command is succesfuly removed, false else
     */
    boolean remove(String cName);
    
    /**
     * Remove only an alias of a command
     * @param cName the alias to remove
     * @return true if the alias is succesfuly removed, false else
     */
    boolean removeAlias(String cName);
    
}
