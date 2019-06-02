package it.polimi.ingsw.view.gui.component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.model.minified.MiniGameBoard;
import it.polimi.ingsw.util.Json;
import it.polimi.ingsw.util.ResourceException;
import it.polimi.ingsw.view.gui.util.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.transform.Rotate;

import java.io.InputStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class GameBoardGUI extends FitObject implements SelectableContainer {
    StretchImage boardImage;
    BoardGUI boardGUI;
    List<WeaponGUI> weaponsGUI = new ArrayList<>();

    public GameBoardGUI(MiniGameBoard gameBoard) {
        String path = "/gui/boards/" +
                gameBoard.getBoard().getType().toString().toLowerCase() + ".png";
        boardImage = new StretchImage(path);
        getChildren().add(boardImage);

        setContentHeight(boardImage.getImageHeight());
        setContentWidth(boardImage.getImageWidth());

        setEffect(new DropShadow());

        try {
            String jsonPath = MessageFormat.format(
                    "/gui/boards/{0}.json",
                    gameBoard.getBoard().getType().toString().toLowerCase());
            InputStream in = getClass().getResourceAsStream(jsonPath);
            if (in == null) {
                throw new ResourceException("Cannot find board gui");
            }
            ObjectMapper mapper = Json.getMapper();
            JsonNode json = mapper.readTree(in);

            double boardWidth = json.get("boardWidth").asDouble();
            double boardHeight = json.get("boardHeight").asDouble();

            int numberRows = json.get("numberRows").asInt();
            int numberColumns = json.get("numberColumns").asInt();

            double x1 = json.get("board").get("x1").asDouble();
            double y1 = json.get("board").get("y1").asDouble();

            double x2 = json.get("board").get("x2").asDouble();
            double y2 = json.get("board").get("y2").asDouble();

            boardGUI = new BoardGUI(gameBoard.getBoard(), numberColumns, numberRows);
            Composition overlay = new Composition();
            overlay.setCompositionWidth(boardWidth);
            overlay.setCompositionHeight(boardHeight);
            overlay.add(boardGUI, x1, y1, x2-x1, y2-y1);

            gameBoard.getBoard().getSpawnPoints().forEach(spawn -> {
                for (int i = 0; i < spawn.getWeapons().size(); i++) {
                    String color = spawn.getColor().toString().toLowerCase();
                    JsonNode position = json.get("weapon").get(color).get(i);
                    double x = position.get("x").asDouble();
                    double y = position.get("y").asDouble();
                    double w = position.get("w").asDouble();
                    double h = position.get("h").asDouble();
                    double r = position.get("r").asDouble();
                    WeaponGUI weapon = new WeaponGUI(spawn.getWeapons().get(i), false);
                    weapon.getTransforms().add(new Rotate(r));
                    overlay.add(weapon, x, y, w, h);
                }
            });

            getChildren().add(overlay);
        } catch (Exception e) {
            throw new ResourceException("Error while parsing GUI board json", e);
        }
    }

    @Override
    public Selectable findSelectable(String uuid) {
        for (WeaponGUI w : weaponsGUI) {
            if (w.getUuid().equals(uuid)) {
                return w;
            }
        }
        return boardGUI.findSelectable(uuid);
    }
}
