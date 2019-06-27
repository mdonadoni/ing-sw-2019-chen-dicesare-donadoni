package it.polimi.ingsw.view.cli;


import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.minified.*;
import it.polimi.ingsw.view.cli.component.WeaponCLI;
import it.polimi.ingsw.view.cli.util.ColorCLI;


import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.WeakHashMap;

public class SelectionManagerCLI {

    private Scanner scanner;
    MiniModel model;

    public SelectionManagerCLI (Scanner scanner){
        this.scanner = scanner;
    }

    private void showTurn(MiniTurn miniTurn){
        if(model.getMyNickname().equals(miniTurn.getCurrentPlayer())){
            System.out.println("E' il tuo turno.");

        }
    }

    public MiniAction selectAction(ArrayList<MiniAction> actions){
        int act;
        MiniAction action;

        System.out.println("Scegli l'azione 0-"+actions.size()+" :");
        for( MiniAction a : actions ){
            System.out.print(""+actions.indexOf(a)+") ");
            for(BasicAction basicAction : a.getActions()){
                System.out.println(basicAction);
            }
        }

        act = selectCheck(actions.size());
        action = actions.get(act);
        return action;
    }

    private int selectCheck(int max){
        int input=0;
        String in;
        System.out.print("-");
        in =scanner.nextLine();
        try {
            input = Integer.parseInt(in);
        }catch (NumberFormatException e){
            System.out.println("Inserire un numero.");
        }
        while(input < 1 || input > max){
            System.out.println("Input non valido.");
            System.out.print("-");
            in =scanner.nextLine();
            try {
                input = Integer.parseInt(in);
            }catch (NumberFormatException e){
                System.out.println("Inserire un numero.");
            }

        }
        return input-1;
    }

    public MiniSquare selectSquare(List<MiniSquare> miniSquareList){
        int sq;
        MiniSquare ms;

        System.out.println("Scegli la dove muoverti 1-"+miniSquareList.size()+" :");
        for( MiniSquare s : miniSquareList ){
            int i =miniSquareList.indexOf(s)+1;
            System.out.print(i+") ");
            System.out.println("("+s.getCoordinates().getRow()+":"+s.getCoordinates().getColumn()+")");
        }

        sq = selectCheck(miniSquareList.size());
        ms = miniSquareList.get(sq);
        return ms;
    }

    private MiniWeapon selectWeapon(List<MiniWeapon> miniWeaponList){
        int in;
        MiniWeapon mw;
        for(MiniWeapon w : miniWeaponList){
            int i = miniWeaponList.indexOf(w)+1;
            System.out.println(i+")" + new WeaponCLI(w).viewWeapon().get(0));
        }
        in = selectCheck(miniWeaponList.size());
        mw = miniWeaponList.get(in);
        return mw;
    }

    public MiniWeapon selectGrabWeapon(List<MiniWeapon> miniWeaponList){
        MiniWeapon mw;
        System.out.println("Scegli arma da prendere 1-"+miniWeaponList.size());
        mw = selectWeapon(miniWeaponList);
        return mw;
    }

    public MiniWeapon selectDropWeapon(List<MiniWeapon> miniWeaponList){
        MiniWeapon mw;
        System.out.println("Scegli arma da scartare 1-"+miniWeaponList.size());
        mw = selectWeapon(miniWeaponList);
        return mw;
    }

    private PowerUp selectPowerUp(List<PowerUp> powerUpList){
        int pw;
        PowerUp powerUp;

        for( PowerUp p : powerUpList){
            int i = powerUpList.size()+1;
            System.out.println(i + ") "+ColorCLI.getAmmoColor(p.getAmmo(), p.getType().toString()));
        }
        pw = selectCheck(powerUpList.size());
        powerUp = powerUpList.get(pw);
        return powerUp;
    }

    public PowerUp selectUsePowerUp(List<PowerUp> powerUpList){
        int pw;
        PowerUp powerUp;

        System.out.println("Scegli il power-up da usare 1-"+powerUpList.size()+" :");
        powerUp = selectPowerUp(powerUpList);
        return powerUp;
    }

    public PowerUp selectDropPowerUp(List<PowerUp> powerUpList){
        int pw;
        PowerUp powerUp;

        System.out.println("Scegli il power-up da scartare 1-"+powerUpList.size()+" :");
        powerUp = selectPowerUp(powerUpList);
        return powerUp;
    }

    public MiniPlayer selectTargetPlayer(List<MiniPlayer> players){
        int nplayer;
        MiniPlayer miniPlayer;

        System.out.println("Scegli il bersaglio 1-"+players.size()+" :");
        for( MiniPlayer p : players ){
            int i = players.indexOf(p) + 1 ;
            System.out.print(i+") ");
            System.out.println(ColorCLI.getPlayerColor(p.getColor(), p.getNickname()));
        }
        nplayer = selectCheck(players.size());
        miniPlayer = players.get(nplayer);
        //System.out.println(miniPlayer.getNickname());
        return miniPlayer;
    }

    public static void main(String[] args){
        SelectionManagerCLI s = new SelectionManagerCLI(new Scanner(System.in));
        ArrayList<MiniPlayer> m = new ArrayList<>();
        m.add(new MiniPlayer(new Player("ed", PlayerToken.PURPLE)));
        m.add(new MiniPlayer(new Player("fa", PlayerToken.GREY)));

        s.selectTargetPlayer(m);
    }
}
