package it.polimi.ingsw.model;

import java.util.*;

/**
 * This class represents a deck of generic cards.
 * @param <T> Class of the cards.
 */
public class Deck<T> {
    /**
     * Stack that maintains the available cards.
     */
    private LinkedList<T> availableCards = new LinkedList<>();

    /**
     * Stack that maintains the discarded cards.
     */
    private LinkedList<T> discardedCards = new LinkedList<>();

    /**
     * Constructor that makes an empty deck.
     */
    public Deck() {
    }

    /**
     * Constructor that makes a shuffled deck with the given cards.
     * @param cards Cards used to initialize the deck.
     */
    public Deck(Collection<T> cards) {
        availableCards.addAll(cards);
        shuffle();
    }

    /**
     * Shuffle the deck of cards. The discarded cards are put in the stack of the
     * available cards and it gets shuffled.
     */
    public void shuffle() {
        availableCards.addAll(discardedCards);
        discardedCards.clear();
        Collections.shuffle(availableCards);
    }

    /**
     * Draw a card from the deck. If the stack of available cards is empty, the
     * deck gets shuffled so that the discarded cards become available.
     * @return Card drawn from the deck.
     */
    public T draw() {
        if (availableCards.isEmpty()) {
            shuffle();
        }
        return availableCards.pop();
    }

    public T peek(){
        return availableCards.get(0);
    }

    /**
     * Discard a card. This card is added to the stack of discarded cards, and
     * will become available when there are no available cards left (or when the
     * deck is shuffled)
     * @param card Card to be discarded.
     */
    public void discard(T card) {
        discardedCards.add(card);
    }

    /**
     * Add a card on top of the stack of available cards.
     * @param card Card to be added.
     */
    public void add(T card) {
        availableCards.add(card);
    }

    public List<T> getDiscarded(){
        return new LinkedList<>(discardedCards);
    }

    public List<T> getAvaible(){
        return new LinkedList<>(availableCards);
    }

    public boolean canDraw(){
        return !(availableCards.isEmpty() && discardedCards.isEmpty());
    }
}
