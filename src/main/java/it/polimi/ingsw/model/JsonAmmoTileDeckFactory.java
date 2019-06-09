package it.polimi.ingsw.model;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.util.Json;
import it.polimi.ingsw.util.ResourceException;
import it.polimi.ingsw.util.ResourceManager;

import java.io.InputStream;
import java.util.ArrayList;

public class JsonAmmoTileDeckFactory implements AmmoTileDeckFactory {
    @Override
    public Deck<AmmoTile> createAmmoTileDeck() {
        Deck<AmmoTile> deck = new Deck<>();
        ObjectMapper mapper = Json.getMapper();
        InputStream stream = ResourceManager.get("/decks/ammoTileDeck.json");
        try{
            JsonNode json = mapper.readTree(stream);

            for(JsonNode ammoTile : json){
                ArrayList<AmmoColor> ammo = new ArrayList<>();
                for(JsonNode color : ammoTile.get("ammos")){
                    ammo.add(AmmoColor.valueOf(color.asText().toUpperCase()));
                }
                int occurrences = ammoTile.get("number").asInt();
                for(int i = 0; i < occurrences; i++){
                    deck.add(new AmmoTile(ammo.toArray(new AmmoColor[0])));
                }
            }

            deck.shuffle();
        } catch(Exception e){
            throw new ResourceException("Cannot read ammoTileDeck resource", e);
        }
        return deck;
    }
}
