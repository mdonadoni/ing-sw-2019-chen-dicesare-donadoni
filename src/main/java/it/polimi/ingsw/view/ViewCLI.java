package it.polimi.ingsw.view;

import it.polimi.ingsw.network.ConnectionType;
import it.polimi.ingsw.network.LocalView;
import it.polimi.ingsw.controller.NicknameAlreadyUsedException;

import java.rmi.RemoteException;
import java.util.Scanner;

public class ViewCLI extends LocalView {
    private Scanner scanner;

    public ViewCLI() {
        scanner = new Scanner(System.in);
    }

    @Override
    public void start(ConnectionType connection, String address, int port) {
        boolean connected = false;
        try {
            while (!connected) {
                connected = true;
                System.out.println("Choose username: ");
                String nickname = scanner.next();
                try {
                    if (connection == ConnectionType.RMI) {
                        connectServerRMI(nickname, address, port);
                    } else {
                        throw new UnsupportedOperationException();
                    }
                } catch (NicknameAlreadyUsedException e) {
                    System.out.println("Username already used!");
                    connected = false;
                }
            }
        } catch (RemoteException e) {
            System.out.println("Cannot connect to server");
            e.printStackTrace();
        }
    }

    @Override
    public void showMessage(String message) {
        System.out.println(message);
    }

    @Override
    public void disconnect() {
        try {
            super.disconnect();
        } catch(RemoteException e) {
            System.out.println("Cannot disconnect");
            e.printStackTrace();
        }
        System.out.println("Disconnected from server");
    }
}
