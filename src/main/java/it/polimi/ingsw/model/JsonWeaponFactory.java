package it.polimi.ingsw.model;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.model.weapons.*;
import it.polimi.ingsw.util.Json;
import it.polimi.ingsw.util.ResourceException;
import it.polimi.ingsw.util.ResourceManager;

import java.io.InputStream;
/**
 * This class represent the implementation of the WeaponFactory.
 */
public class JsonWeaponFactory implements WeaponFactory {
    /**
     * Create a weapon based on its type. This method load the weapon from the file where it is contained.
     * @param type The type of weapon.
     * @return The weapon created.
     */
    public Weapon createWeapon(WeaponType type){
        Weapon weapon = new Weapon();

        InputStream stream = ResourceManager.get("/weapons/"+type.toString().toLowerCase()+".json");

        try {
            ObjectMapper mapper = Json.getMapper();
            JsonNode json = mapper.readTree(stream);

            weapon.setName(json.get("name").asText());
            weapon.setCharged(json.get("charged").asBoolean());

            for(JsonNode color : json.get("pickupColor")){
                weapon.addPickupColor(AmmoColor.valueOf(color.asText().toUpperCase()));
            }

            JsonNode additionalRecharge = json.get("additionalRechargeColor");
            if(additionalRecharge != null)
                weapon.setAdditionalRechargeColor(AmmoColor.valueOf(additionalRecharge.asText().toUpperCase()));

            for(JsonNode attack : json.get("attacks")){
                weapon.addAttack(createAttack(attack));
            }
        } catch (Exception e){
            throw new ResourceException("Cannot load weapon resource", e);
        }

        return weapon;
    }

    /**
     * Create an attack of the weapon from the file of the weapon.
     * @param json The JsonNode of the weapon, that maps its file.
     * @return The attack created.
     */
    private Attack createAttack(JsonNode json){
        Attack attack = new Attack();

        attack.setDescriptionId(json.get("id").asText());
        for(JsonNode target : json.get("baseFire")){
            if(target.get("type").asText().equals("player"))
                attack.addBaseFire(createPlayerTarget(target));
            else if(target.get("type").asText().equals("square"))
                attack.addBaseFire(createSquareTarget(target));
        }
        for(JsonNode addAtt : json.get("additionalAttacks")){
            attack.addAdditionalAttack(createAttack(addAtt));
        }
        for(JsonNode bonusMov : json.get("bonusMovement")){
            attack.addBonusMovement(createMovementEffect(bonusMov));
        }
        JsonNode bonusCost = json.get("bonusMovementCost");
        if(bonusCost!=null){
            for(JsonNode ammoCost : bonusCost)
                attack.addBonusMovementCost(AmmoColor.valueOf(ammoCost.asText().toUpperCase()));
        }
        for(JsonNode ammoCost : json.get("cost")){
            attack.addCost(AmmoColor.valueOf(ammoCost.asText().toUpperCase()));
        }

        return attack;
    }
    /**
     * Create an target of the attack.
     * @param json The JsonNode of the attack, that maps its field.
     * @return The target created.
     */
    private Target createTarget(JsonNode json){
        Target target = new Target();

        target.setNumberOfTargets(json.get("numberOfTargets").asInt());
        target.setVisibility(Visibility.valueOf(json.get("visibility").asText().toUpperCase()));
        target.setMinDistance(json.get("minDistance").asInt());
        target.setMaxDistance(json.get("maxDistance").asInt());
        target.setExclusive(json.get("exclusive").asBoolean());
        target.setInherited(json.get("inherited").asBoolean());
        for(JsonNode special : json.get("special"))
        {
            target.addSpecial(SpecialArea.valueOf(special.asText()));
        }
        for(JsonNode effect : json.get("effects")){
            if(effect.get("type").asText().equals("harmful"))
                target.addEffect(createHarmfulEffect(effect));
            else if(effect.get("type").asText().equals("movement"))
                target.addEffect(createMovementEffect(effect));
        }

        return target;
    }

    /**
     * Create a target player.
     * @param json The JsonNode of the target player.
     * @return The target player created.
     */
    private PlayerTarget createPlayerTarget(JsonNode json){
        return new PlayerTarget(createTarget(json));
    }

    /**
     * Create a target square.
     * @param json he JsonNode of the target square.
     * @return The target square created.
     */
    private SquareTarget createSquareTarget(JsonNode json){
        SquareTarget squareTarget = new SquareTarget(createTarget(json));

        squareTarget.setMaxPlayerDistance(json.get("maxPlayerDistance").asInt());
        squareTarget.setNumberOfPlayers(json.get("numberOfPlayers").asInt());
        squareTarget.setVortex(json.get("vortex").asBoolean());

        return squareTarget;
    }

    /**
     * Create an harmful effect.
     * @param json The JsonNode of the harmful effect.
     * @return The hermful effect created.
     */
    private HarmfulEffect createHarmfulEffect(JsonNode json){
        HarmfulEffect harmfulEffect = new HarmfulEffect();

        harmfulEffect.setValue(json.get("value").asInt());
        harmfulEffect.setType(HarmType.valueOf(json.get("harm").asText().toUpperCase()));

        return harmfulEffect;
    }

    /**
     * Create a movement effect.
     * @param json The JsonNode of the movement effect.
     * @return The movement effect.
     */
    private MovementEffect createMovementEffect(JsonNode json){
        MovementEffect movementEffect = new MovementEffect();

        movementEffect.setValue(json.get("value").asInt());
        movementEffect.setVisibleDest(Visibility.valueOf(json.get("visibleDest").asText().toUpperCase()));
        movementEffect.setLine(json.get("line").asBoolean());
        movementEffect.setFixed(json.get("fixed").asBoolean());
        movementEffect.setSelf(json.get("self").asBoolean());

        return movementEffect;
    }
}
