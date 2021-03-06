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
package fr.bsenac.the_captain_bot.commands;

import fr.bsenac.the_captain_bot.commandsmeta.commands.CommandContext;

/**
 *
 * @author vixa
 */
public abstract class Command implements Comparable<Command>{

    protected final String name;
    protected final String[] alias;

    public Command(String name, String... alias) {
        this.name = name;
        this.alias = alias;
    }

    /**
     *
     * @param cc
     */
    public abstract void run(CommandContext cc);

    /**
     *
     * @return
     */
    public abstract String help();

    public final String getName() {
        return name;
    }

    public final String[] getAlias() {
        return alias;
    }
    
    /**
     * Check if the command need to wait a load.
     * @return true if the command need to wait data load 
     */
    public boolean needWait(){
        return false;
    }

    @Override
    public int compareTo(Command o) {
        return this.name.compareTo(o.name);
    }
    
    
}
