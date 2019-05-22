package it.polimi.ingsw.network;

import java.rmi.RemoteException;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class decorates a View with a cumulative timeout.
 */
public class TimedView {
    /**
     * Logger
     */
    private static final Logger LOG = Logger.getLogger(TimedView.class.getName());
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

    /**
     * Constructs a TimedView with no time left.
     * @param view
     */
    public TimedView(View view) {
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
                    pingTimer.cancel();
                    executor.shutdownNow();
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
        if (timeout <= 0) {
            throw new RemoteException("Timed out");
        }

        if (executor.isShutdown()) {
            throw new RemoteException("View already disconnected");
        }

        Future<T> future = executor.submit(task);
        try {
            T res = future.get(timeout, TimeUnit.MILLISECONDS);

            return res;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RemoteException("Remote request interrupted", e);
        } catch (TimeoutException | ExecutionException e) {
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
            throw e;
        }
        timeLeft -= new Date().getTime() - initialTime;
        return res;
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
        try {
            makeTimedRequest(() -> {
                view.disconnect();
                return null;
            });
        } finally {
            executor.shutdown();
        }
    }
}
