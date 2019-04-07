package it.polimi.ingsw.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DeckTest {

    Deck<Object> deck;

    @BeforeEach
    void setUp() {
        deck = new Deck<>();
    }

    @Test
    void shuffle() {
        final int N = 10;
        List<Object> original = new ArrayList<>();
        for (int i = 0; i < N; ++i) {
            Object obj = new Object();
            original.add(obj);
            if (i%2 == 0) {
                deck.add(obj);
            } else {
                deck.discard(obj);
            }
        }

        deck.shuffle();

        List<Object> after = new ArrayList<>();
        for (int i = 0; i < N; i++) {
            after.add(deck.draw());
        }

        assertTrue(after.containsAll(original));
    }

    @Test
    void deckInitialization() {
        final int N = 10;
        List<Object> original = new ArrayList<>();
        for (int i = 0; i < N; ++i) {
            Object obj = new Object();
            original.add(obj);
        }

        deck = new Deck<>(original);

        List<Object> after = new ArrayList<>();
        for (int i = 0; i < N; i++) {
            after.add(deck.draw());
        }

        assertTrue(after.containsAll(original));
    }

    @Test
    void discard() {
        final int A = 10, D = 10;
        List<Object> added = new ArrayList<>();
        for (int i = 0; i < A; ++i) {
            Object obj = new Object();
            added.add(obj);
            deck.add(obj);
        }
        List<Object> discarded = new ArrayList<>();
        for (int i = 0; i < D; ++i) {
            Object obj = new Object();
            discarded.add(obj);
            deck.discard(obj);
        }

        List<Object> afterAdded = new ArrayList<>();
        for (int i = 0; i < A; i++) {
            afterAdded.add(deck.draw());
        }
        List<Object> afterDiscarded = new ArrayList<>();
        for (int i = 0; i < D; i++) {
            afterDiscarded.add(deck.draw());
        }

        assertTrue(afterAdded.containsAll(added));
        assertTrue(afterDiscarded.containsAll(discarded));
    }
}