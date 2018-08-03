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
package fr.bsenac.the_captain_bot.waiter;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 *
 * @author vixa
 */
public class Waiter implements Future<Void> {

    private final Set<Future<Void>> toWait = new HashSet<>();

    public void add(Future<Void> toWait) {
        this.toWait.add(toWait);
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        boolean cancel = true;
        for (Future<Void> f : toWait) {
            cancel &= f.cancel(mayInterruptIfRunning);
        }
        return cancel;
    }

    @Override
    public boolean isCancelled() {
        boolean isCanceled = false;
        while (!isCanceled && toWait.iterator().hasNext()) {
            isCanceled &= toWait.iterator().next().isCancelled();
        }
        return isCanceled;
    }

    @Override
    public boolean isDone() {
        boolean isDone = false;
        while (!isDone && toWait.iterator().hasNext()) {
            isDone &= toWait.iterator().next().isDone();
        }
        return isDone;
    }

    @Override
    public Void get() throws InterruptedException, ExecutionException {
        for (Future<Void> f : toWait) {
            f.get();
        }
        return null;
    }

    @Override
    public Void get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        long start = System.currentTimeMillis();
        long end = TimeUnit.MILLISECONDS.convert(timeout, unit);
        for (Future<Void> f : toWait) {
            f.get(timeout / toWait.size(), unit);
        }
        return null;
    }

}
