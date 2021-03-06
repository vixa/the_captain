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
package fr.bsenac.the_captain_bot.main;

import fr.bsenac.the_captain_bot.audio.AudioConnectionsManager;
import fr.bsenac.the_captain_bot.audio.PlaylistsDatabaseManager;
import fr.bsenac.the_captain_bot.listeners.MessageListener;
import javax.security.auth.login.LoginException;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;

/**
 *
 * @author vixa
 */
public class Main {

    public static void main(String[] args) throws BotTokenException {
        if (0 == args.length) {
            throw new BotTokenException("No token provided in program args");
        }
        try {
            PlaylistsDatabaseManager.getManager().start();
            AudioConnectionsManager.getManager().start();
            JDA jda = new JDABuilder(AccountType.BOT).setToken(args[0])
                    .addEventListener(new MessageListener()).buildBlocking();
        } catch (LoginException ex) {
            throw new BotTokenException("Error while login. Please check the token.");
        } catch (InterruptedException ex) {

        }
    }
}
