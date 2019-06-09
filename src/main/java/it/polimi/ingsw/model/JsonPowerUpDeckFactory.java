package it.polimi.ingsw.model;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.util.Json;
import it.polimi.ingsw.util.ResourceException;
import it.polimi.ingsw.util.ResourceManager;

import java.io.InputStream;

public class JsonPowerUpDeckFactory implements PowerUpDeckFactory {

    @Override
    public Deck<PowerUp> createPowerUpDeck() {
        Deck<PowerUp> deck = new Deck<>();
        ObjectMapper mapper = Json.getMapper();
        InputStream stream = ResourceManager.get("/decks/powerUpDeck.json");
        try {
            JsonNode json = mapper.readTree(stream);

            for (JsonNode pwu : json){

                int occurrences = pwu.get("number").asInt();
                PowerUpType type = PowerUpType.valueOf(pwu.get("type").asText().toUpperCase());
                AmmoColor ammo = AmmoColor.valueOf(pwu.get("color").asText().toUpperCase());

                for (int i = 0; i < occurrences; i++)
                    deck.add(new PowerUp(type, ammo));
            }

            deck.shuffle();
        } catch (Exception e){
            throw new ResourceException("Cannot read powerUpDeck resource", e);
        }
        return deck;
    }
}
