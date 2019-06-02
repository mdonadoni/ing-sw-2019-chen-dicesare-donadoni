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

public class SelectionManager {
    List<Selectable> selectables;
    int min, max;
    Button button;
    Set<String> selected = new HashSet<>();
    CompletableFuture<List<String>> finalSelection = new CompletableFuture<>();

    public SelectionManager(List<Selectable> selectables, Button confirmButton, int min, int max) {
        this.selectables = new ArrayList<>(selectables);
        this.min = min;
        this.max = max;
        this.button = confirmButton;
    }

    public void start() {
        Platform.runLater(() -> {
            selectables.forEach(s -> s.enable(() -> changeState(s)));
            button.setOnAction(e -> stop());
            refreshStateButton();
        });
    }

    private void stop() {
        button.setDisable(true);
        selectables.forEach(Selectable::disable);
        finalSelection.complete(new ArrayList<>(selected));
    }

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

    private void refreshStateButton() {
        if (selected.size() >= min && selected.size() <= max) {
            button.setDisable(false);
        } else {
            button.setDisable(true);
        }
    }

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
