package org.ir.agents.aiguilleur;

import java.util.Iterator;

public class Pair<T> implements Iterable<T> {

    private T first;
    private T second;

    public Pair() {}

    public Pair(T first, T second) {
        this.first = first;
        this.second = second;
    }

    public T first() {
        return this.first;
    }

    public T second() {
        return this.second;
    }

    public void add(T element) {
        if (first == null) first = element;
        else if (second == null) second = element;
        else throw new IllegalStateException("Pair is already set.");
    }

    @Override
    public Iterator<T> iterator() {
        return new PairIterator(this);
    }

    class PairIterator implements Iterator<T> {
        private Pair<T> pair;
        private int cpt;

        PairIterator(Pair<T> pair) {
            this.pair = pair;
            cpt = 0;
        }

        @Override
        public boolean hasNext() {
        return cpt < 2;
    }

        @Override
        public T next() {
            if (cpt == 0) { cpt++; return pair.first; }
            else if (cpt == 1) { cpt++; return pair.second; }
            else return null;
        }

        @Override
        public void remove() {
            // Not needed
            throw new UnsupportedOperationException();
        }
    }
}
