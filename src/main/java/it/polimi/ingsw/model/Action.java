package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.List;

public class Action {
    private List<BaseAction> actions = new ArrayList<>();

    public void addAction(BaseAction act){
        actions.add(act);
    }

    public void removeAction(BaseAction act){
        actions.remove(act);
    }

    public int countMovement(){
        return (int)actions
                .stream()
                .filter(e -> e.equals(BaseAction.MOVEMENT))
                .count();
    }

    public boolean canMove(){
        return actions.contains(BaseAction.MOVEMENT);
    }

    public boolean canGrab(){
        return actions.contains(BaseAction.GRAB);
    }

    public boolean canShoot(){
        return actions.contains(BaseAction.SHOOT);
    }

    public boolean canReload(){
        return actions.contains(BaseAction.RELOAD);
    }
}
