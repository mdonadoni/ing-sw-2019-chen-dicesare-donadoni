package it.polimi.ingsw.view.gui.component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.model.PlayerToken;
import it.polimi.ingsw.model.minified.MiniGameBoard;
import it.polimi.ingsw.util.Json;
import it.polimi.ingsw.util.ResourceException;
import it.polimi.ingsw.util.ResourceManager;
import it.polimi.ingsw.view.gui.util.*;
import javafx.scene.effect.DropShadow;

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
            InputStream in = ResourceManager.get(jsonPath);
            if (in == null) {
                throw new ResourceException("Cannot find board gui");
            }
            ObjectMapper mapper = Json.getMapper();
            JsonNode json = mapper.readTree(in);

            double boardWidth = json.get("gameBoardWidth").asDouble();
            double boardHeight = json.get("gameBoardHeight").asDouble();

            int numberRows = json.get("numberRows").asInt();
            int numberColumns = json.get("numberColumns").asInt();

            Position boardPosition = Position.fromJson(json.get("board"));
            boardGUI = new BoardGUI(gameBoard.getBoard(), numberColumns, numberRows);

            Composition overlay = new Composition();
            overlay.setCompositionWidth(boardWidth);
            overlay.setCompositionHeight(boardHeight);
            overlay.add(boardGUI,boardPosition);

            // Add weapon
            gameBoard.getBoard().getSpawnPoints().forEach(spawn -> {
                for (int i = 0; i < spawn.getWeapons().size(); i++) {
                    String color = spawn.getColor().toString().toLowerCase();
                    Position position = Position.fromJson(json.get("weapon").get(color).get(i));
                    WeaponGUI weapon = new WeaponGUI(spawn.getWeapons().get(i), false);
                    overlay.add(weapon, position);
                    weaponsGUI.add(weapon);
                }
            });

            // Add killshots
            Position killshot = Position.fromJson(json.get("killshot").get("position"));
            int initialSkulls = gameBoard.getInitialSkullNumber();
            int maxSkulls = json.get("killshot").get("maxSkulls").asInt();
            int emptySkulls = maxSkulls - initialSkulls;
            double tokenWidth = killshot.getWidth()/maxSkulls;
            double tokenHeight = killshot.getHeight();
            for (int i = 0; i < maxSkulls; i++) {
                if (i >= emptySkulls) {
                    if (i - emptySkulls < gameBoard.getKillShotTrack().size()) {
                        List<PlayerToken> tokens = gameBoard.getKillShotTrack().get(i - emptySkulls);
                        for (int t = 0; t < tokens.size(); t++) {
                            Position token = new Position();
                            token.setX(killshot.getX() + i * tokenWidth);
                            token.setY(killshot.getY() + t * tokenHeight / 4);
                            token.setWidth(tokenWidth);
                            token.setHeight(tokenHeight);
                            overlay.add(new TokenGUI(tokens.get(t), 1), token);
                        }
                    } else {
                        overlay.add(
                            new SkullGUI(),
                            killshot.getX() + i*tokenWidth,
                            killshot.getY(),
                            tokenWidth,
                            tokenHeight
                        );
                    }
                }
            }

            // skulls of final frenzy
            if (gameBoard.getKillShotTrack().size() == initialSkulls+1) {
                List<PlayerToken> tokens = gameBoard.getKillShotTrack().get(initialSkulls);
                for (int t = 0; t < tokens.size(); t++) {
                    Position token = new Position();
                    token.setX(killshot.getX() + killshot.getWidth() + t * tokenWidth / 4);
                    token.setY(killshot.getY());
                    token.setWidth(tokenWidth);
                    token.setHeight(tokenHeight);
                    overlay.add(new TokenGUI(tokens.get(t), 1), token);
                }
            }

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
