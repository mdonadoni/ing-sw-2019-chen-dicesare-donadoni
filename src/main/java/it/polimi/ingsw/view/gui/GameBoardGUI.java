package it.polimi.ingsw.view.gui;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.model.ResourceException;
import it.polimi.ingsw.model.minified.MiniGameBoard;
import it.polimi.ingsw.util.Json;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

import java.io.InputStream;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;

public class GameBoardGUI extends GridPane {
    MiniGameBoard gameBoard;

    public GameBoardGUI(MiniGameBoard gameBoard) {
        this.gameBoard = gameBoard;

        ColumnConstraints colMain = new ColumnConstraints();
        colMain.setPercentWidth(100);

        RowConstraints rowMain = new RowConstraints();
        rowMain.setPercentHeight(100);

        getColumnConstraints().add(colMain);
        getRowConstraints().add(rowMain);

        String cssBoardClass = gameBoard
                .getBoard()
                .getType()
                .toString()
                .toLowerCase()
                .replace("_", "") + "-board";

        getStyleClass().add(cssBoardClass);

        try {
            String jsonPath = MessageFormat.format(
                    "/gui/boards/{0}.json",
                    gameBoard.getBoard().getType().toString().toLowerCase());
            InputStream in = getClass().getResourceAsStream(jsonPath);
            ObjectMapper mapper = Json.getMapper();
            JsonNode json = mapper.readTree(in);

            double boardWidth = json.get("boardWidth").asDouble();
            double boardHeight = json.get("boardHeight").asDouble();

            int numberRows = json.get("numberRows").asInt();
            int numberColumns = json.get("numberColumns").asInt();

            double x1 = json.get("x1").asDouble();
            double y1 = json.get("y1").asDouble();

            double x2 = json.get("x2").asDouble();
            double y2 = json.get("y2").asDouble();

            GridPane boardPane = new GridPane();

            List<Double> x = Arrays.asList(0.0, x1, x2, boardWidth);
            for (int i = 1; i < x.size(); i++) {
                ColumnConstraints col = new ColumnConstraints();
                col.setPercentWidth((x.get(i) - x.get(i-1)) / boardWidth * 100.0);
                boardPane.getColumnConstraints().add(col);
            }

            List<Double> y = Arrays.asList(0.0, y1 ,y2, boardHeight);
            for (int i = 1; i < y.size(); i++) {
                RowConstraints row = new RowConstraints();
                row.setPercentHeight((y.get(i) - y.get(i-1)) / boardHeight * 100.0);
                boardPane.getRowConstraints().add(row);
            }

            BoardGUI board = new BoardGUI(gameBoard.getBoard(), numberColumns, numberRows);
            boardPane.add(board, 1, 1);
            getChildren().add(boardPane);
        } catch (Exception e) {
            throw new ResourceException("Error while parsing GUI board json", e);
        }
    }
}
