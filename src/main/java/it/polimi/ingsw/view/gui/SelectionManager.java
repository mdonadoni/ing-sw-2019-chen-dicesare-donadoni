package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.view.gui.util.Selectable;
import javafx.application.Platform;
import javafx.scene.control.Button;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * Manager of the selection
 */
public class SelectionManager {
    /**
     * List of selectable objects
     */
    private List<Selectable> selectables;
    /**
     * Minimum size of the selection
     */
    private int min;
    /**
     * Maximum size of the selection
     */
    private int max;
    /**
     * The button for the confirm
     */
    private Button button;
    /**
     * Selected items
     */
    private Set<String> selected = new HashSet<>();
    /**
     * Confirmed selection
     */
    private CompletableFuture<List<String>> finalSelection = new CompletableFuture<>();

    /**
     * Constructor of the class
     * @param selectables List of selectable objects
     * @param confirmButton The button for the confirm
     * @param min Minimum size of the selection
     * @param max Maximum size of the selection
     */
    public SelectionManager(List<Selectable> selectables, Button confirmButton, int min, int max) {
        this.selectables = new ArrayList<>(selectables);
        this.min = min;
        this.max = max;
        this.button = confirmButton;
    }

    /**
     * Start the selection
     */
    public void start() {
        Platform.runLater(() -> {
            selectables.forEach(s -> s.enable(() -> changeState(s)));
            button.setOnAction(e -> stop());
            refreshStateButton();
        });
    }

    /**
     * Stop the selection
     */
    private void stop() {
        button.setDisable(true);
        selectables.forEach(Selectable::disable);
        finalSelection.complete(new ArrayList<>(selected));
    }

    /**
     * Change the state of a selectable object
     * @param s the selectable object
     */
    private void changeState(Selectable s) {
        if (selected.contains(s.getUuid())) {
            selected.remove(s.getUuid());
            s.setSelected(false);
        } else {
            selected.add(s.getUuid());
            s.setSelected(true);
        }

        refreshStateButton();
    }

    /**
     * Refresh the button state
     */
    private void refreshStateButton() {
        if (selected.size() >= min && selected.size() <= max) {
            button.setDisable(false);
        } else {
            button.setDisable(true);
        }
    }

    /**
     * Get the selected object.
     * @return List of selected object
     */
    public List<String> getSelected() {
        try {
            return finalSelection.get();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Thread interrupted", e);
        } catch (ExecutionException e) {
            throw new RuntimeException("Something went wrong while selecting", e);
        }
    }

}
