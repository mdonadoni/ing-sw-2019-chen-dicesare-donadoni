package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Identifiable;
import it.polimi.ingsw.model.minified.MiniModel;
import it.polimi.ingsw.network.View;
import it.polimi.ingsw.view.dialogs.DialogType;

import java.rmi.RemoteException;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.*;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class RemotePlayer {
    /**
     * Logger
     */
    private static final Logger LOG = Logger.getLogger(RemotePlayer.class.getName());
    /**
     * Period between one ping and the successive one.
     */
    private static final long PING_PERIOD = 5000;
    /**
     * Maximum waiting time for a ping response.
     */
    private static final long PING_TIMEOUT = 3000;
    /**
     * Reference to view to be decorated.
     */
    private View view;
    /**
     * Nickname of the player.
     */
    private String nickname;
    /**
     * Executor for async requests.
     */
    private ExecutorService executor;
    /**
     * Milliseconds left for method invocation.
     */
    private long timeLeft;
    /**
     * Timer for ping messages.
     */
    private Timer pingTimer;

    private Consumer<RemotePlayer> disconnectionCallback;

    /**
     * Constructs a RemotePlayer with no time left.
     * @param view
     */
    public RemotePlayer(String nickname, View view) {
        this.nickname = nickname;
        this.view = view;
        this.executor = Executors.newCachedThreadPool();
        this.timeLeft = 0;

        this.pingTimer = new Timer();
        TimerTask pingTask = new TimerTask() {
            @Override
            public void run() {
                try {
                    throwOnTimeout(() -> {
                        view.ping();
                        return null;
                    }, PING_TIMEOUT);
                } catch (Exception e) {
                    LOG.log(Level.WARNING, "Error while pinging");
                    stop();
                }
            }
        };
        pingTimer.schedule(pingTask, 0, PING_PERIOD);
    }

    /**
     * Set the time left for requests.
     * @param timeLeft Time left in milliseconds.
     */
    public void setTimeLeft(long timeLeft) {
        this.timeLeft = timeLeft;
    }

    /**
     * Execute a task asynchronously and throw if time limit is exceeded.
     * @param task Task to be performed.
     * @param <T> Return type of task.
     * @return Result of task
     * @throws RemoteException If an error occurs or the timeout is reached.
     */
    private <T> T throwOnTimeout(Callable<T> task, long timeout) throws RemoteException {
        if (executor.isShutdown()) {
            throw new RemoteException("View already disconnected");
        }

        if (timeout <= 0) {
            stop();
            throw new RemoteException("Timed out");
        }

        Future<T> future = executor.submit(task);
        try {
            return future.get(timeout, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            stop();
            throw new RemoteException("Remote request interrupted", e);
        } catch (TimeoutException | ExecutionException e) {
            stop();
            throw new RemoteException("Remote request error", e);
        }
    }

    /**
     * Executes a task asynchronously keeping how much time has passed.
     * @param task Task to be run.
     * @param <T> Return type of task.
     * @return Result of task.
     * @throws RemoteException If there is a network error or a timeout is reached.
     */
    private <T> T makeTimedRequest(Callable<T> task) throws RemoteException {
        double initialTime = new Date().getTime();
        T res;
        try {
            res = throwOnTimeout(task, timeLeft);
        } catch (RemoteException e) {
            timeLeft = 0;
            stop();
            throw e;
        }
        timeLeft -= new Date().getTime() - initialTime;
        return res;
    }

    private void stop() {
        if (isConnected()) {
            pingTimer.cancel();
            executor.shutdownNow();
            new Thread(() -> {
                try {
                    view.disconnect();
                } catch (Exception e) {
                    LOG.log(Level.WARNING, "Couldn't disconnect view");
                }
            }).start();

            if (disconnectionCallback != null) {
                disconnectionCallback.accept(this);
            }
        }
    }

    /**
     * Select between list of objects.
     * @param objUuid Coordinates of the squares.
     * @param min Minimum number of squares to be chosen.
     * @param max Maximum number of squares to be chosen.
     * @return List of chosen objects.
     * @throws RemoteException If there is an error while making the request.
     */
    public List<String> selectObject(List<String> objUuid, int min, int max) throws RemoteException {
        return makeTimedRequest(() -> view.selectObject(objUuid, min, max));
    }

    public <T extends Identifiable> List<T> selectIdentifiable(List<T> objects, int min, int max) throws RemoteException {
        List<String> uuids = objects
                .stream()
                .map(Identifiable::getUuid)
                .collect(Collectors.toList());
        List<String> selected = selectObject(uuids, min, max);
        List<T> result = objects
                .stream()
                .filter(obj -> selected.contains(obj.getUuid()))
                .collect(Collectors.toList());
        if (result.size() != selected.size() || result.size() < min || result.size() > max) {
            throw new RemoteException("Something went wrong while selecting");
        }
        return result;
    }

    /**
     * Show message.
     * @param message Massage to be shown.
     * @throws RemoteException If there is an error while making the request.
     */
    public void showMessage(String message) throws RemoteException {
        makeTimedRequest(() -> {
            view.showMessage(message);
            return null;
        });
    }

    /**
     * Show message.
     * @param dialogType Enum that describes the type of the dialog to be shown
     * @param params List of params to be filled in the message
     * @throws RemoteException In case something goes wrong
     */
    public void showMessage(DialogType dialogType, List<String> params) throws RemoteException{
        showMessage(UserDialog.getDialog(dialogType, params));
    }

    /**
     * Show message with timeout. The time elapsed is not counted towards the
     * global time limit.
     * @param message Message to be sent.
     * @param timeout Timeout of the request.
     * @throws RemoteException If there is an error while making the request.
     */
    public void showMessage(String message, long timeout) throws RemoteException {
        throwOnTimeout(() -> {
            view.showMessage(message);
            return null;
        }, timeout);
    }

    /**
     * Update model on the view.
     * @param model Model to be sent.
     * @throws RemoteException If there is an error while making the request.
     */
    public void updateModel(MiniModel model) throws RemoteException {
        makeTimedRequest(() -> {
            view.updateModel(model);
            return null;
        });
    }

    /**
     * Update model on the view with a given timeout. The time elapsed is not
     * counted as part of the global limit.
     * @param model Model to be sent.
     * @param timeout Timeout of the request.
     * @throws RemoteException If there is a network error.
     */
    public void updateModel(MiniModel model, long timeout) throws RemoteException {
        throwOnTimeout(() -> {
            view.updateModel(model);
            return null;
        }, timeout);
    }

    /**
     * Method to check if the connection is still up.
     * @throws RemoteException If there is a network error.
     */
    public void ping() throws RemoteException {
        makeTimedRequest(() -> {
            view.ping();
            return null;
        });
    }

    /**
     * Disconnect the view
     * @throws RemoteException If there is an error while disconnecting the view.
     */
    public void disconnect() throws RemoteException {
        stop();
    }

    public boolean isConnected() {
        return !executor.isShutdown();
    }

    public String getNickname() {
        return nickname;
    }

    public void safeShowMessage(String message) {
        final long showMessageTimeout = 5000;
        try {
            showMessage(message, showMessageTimeout);
        } catch (RemoteException e) {
            LOG.log(Level.WARNING, () -> "Couldn't send message to " + nickname);
        }
    }

    public void safeShowMessage(DialogType dialogType, List<String> params){
        safeShowMessage(UserDialog.getDialog(dialogType, params));
    }

    public void setDisconnectionCallback(Consumer<RemotePlayer> disconnectionCallback) {
        this.disconnectionCallback = disconnectionCallback;
    }

    public View getView(){
        return view;
    }
}
