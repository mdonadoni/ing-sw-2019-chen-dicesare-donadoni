package it.polimi.ingsw.model.weapons;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.model.AmmoColor;
import it.polimi.ingsw.model.Json;
import it.polimi.ingsw.model.ResourceException;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class Weapon {
    private String name;
    private Boolean charged;
    private List<AmmoColor> pickupColor = new ArrayList<>();
    private AmmoColor additionalRechargeColor;
    private ArrayList<Attack> attacks = new ArrayList<>();

    @JsonCreator
    public Weapon(String name){
        this.name = name;
    }
    public Weapon(WeaponType name) throws ResourceException {
        this(Weapon.class.getResourceAsStream("/weapons/"+name.toString().toLowerCase()+".json"));
    }
    public Weapon(InputStream stream) throws ResourceException {
        try {
            ObjectMapper mapper = Json.getMapper();
            JsonNode json = mapper.readTree(stream);

            setName(json.get("name").asText());
            setCharged(json.get("charged").asBoolean());

            for(JsonNode color : json.get("pickupColor")){
                addPickupColor(AmmoColor.valueOf(color.asText().toUpperCase()));
            }
            setAdditionalRechargeColor(AmmoColor.valueOf(json.get("additionalRechargeColor").asText().toUpperCase()));

            for(JsonNode attack : json.get("attacks")){
                addAttack(new Attack(attack));
            }
        } catch (Exception e){
            throw new ResourceException("Cannot load weapon resource", e);
        }
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
    public Boolean getCharged() {
        return charged;
    }
    public void setCharged(Boolean charged) {
        this.charged = charged;
    }
    public List<AmmoColor> getPickupColor() {
        return pickupColor;
    }
    public void addPickupColor(AmmoColor ammo) {
        pickupColor.add(ammo);
    }
    public AmmoColor getAdditionalRechargeColor() {
        return additionalRechargeColor;
    }
    public void setAdditionalRechargeColor(AmmoColor additionalRechargeColor) {
        this.additionalRechargeColor = additionalRechargeColor;
    }
    public List<Attack> getAttacks(){
        return attacks;
    }
    public void addAttack(Attack attack){
        attacks.add(attack);
    }
    public void setAttack(int ndx, Attack attack){
        if (attacks.size() > ndx){
            attacks.set(ndx, attack);
        }
    }
    public void removeAttack(int ndx){
        attacks.remove(ndx);
    }

}