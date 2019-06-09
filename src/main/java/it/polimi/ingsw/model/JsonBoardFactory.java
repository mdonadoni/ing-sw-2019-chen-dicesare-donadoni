package it.polimi.ingsw.model;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.util.Json;
import it.polimi.ingsw.util.ResourceException;
import it.polimi.ingsw.util.ResourceManager;

import java.io.InputStream;
import java.util.function.Function;

public class JsonBoardFactory implements BoardFactory {

    private BoardType type;

    public JsonBoardFactory(BoardType type) {
        this.type = type;
    }

    @Override
    public Board createBoard() {
        Board board = new Board();
        board.setType(type);
        try {
            String path = "/boards/" + type.name().toLowerCase() + ".json";
            InputStream in = ResourceManager.get(path);
            ObjectMapper mapper = Json.getMapper();
            JsonNode json = mapper.readTree(in);

            Function<JsonNode, Coordinate> jsonToCoord = obj ->
                    new Coordinate(
                            obj.get("row").asInt(),
                            obj.get("column").asInt()
                    );

            for (JsonNode standard : json.get("standard")) {
                board.addStandardSquare(jsonToCoord.apply(standard));
            }

            for (JsonNode spawn : json.get("spawnpoint")) {
                AmmoColor color = AmmoColor.valueOf(spawn.get("color").asText().toUpperCase());
                board.addSpawnPoint(jsonToCoord.apply(spawn), color);
            }

            for (JsonNode link : json.get("links")) {
                Coordinate first = jsonToCoord.apply(link.get("first"));
                Coordinate second = jsonToCoord.apply(link.get("second"));
                LinkType linkType = LinkType.valueOf(link.get("type").asText().toUpperCase());
                board.addLink(first, second, linkType);
            }
        } catch (Exception e) {
            throw new ResourceException("Cannot load board json resource", e);
        }
        return board;
    }
}
