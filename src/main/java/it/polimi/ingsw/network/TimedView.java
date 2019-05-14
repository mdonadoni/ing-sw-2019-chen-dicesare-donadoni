package it.polimi.ingsw.network;

import java.rmi.RemoteException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.*;

/**
 * This class decorates a View with a cumulative timeout.
 */
public class TimedView implements View {
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
     * Constructs a TimedView with no time left.
     * @param view
     */
    public TimedView(View view) {
        this.view = view;
        this.executor = Executors.newSingleThreadExecutor();
        this.timeLeft = 0;
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
    private <T> T throwOnTimeout(Callable<T> task) throws RemoteException {
        if (timeLeft <= 0) {
            throw new RemoteException("Timed out");
        }
        long initialTime = new Date().getTime();
        Future<T> future = executor.submit(task);
        try {
            T res = future.get(timeLeft, TimeUnit.MILLISECONDS);
            timeLeft -= new Date().getTime() - initialTime;
            return res;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            timeLeft = 0;
            throw new RemoteException("Remote request interrupted", e);
        } catch (TimeoutException | ExecutionException e) {
            timeLeft = 0;
            throw new RemoteException("Remote request error", e);
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
    @Override
    public List<String> selectObject(List<String> objUuid, int min, int max) throws RemoteException {
        return throwOnTimeout(() -> view.selectObject(objUuid, min, max));
    }

    /**
     * Show message.
     * @param message Massage to be shown.
     * @throws RemoteException If there is an error while making the request.
     */
    @Override
    public void showMessage(String message) throws RemoteException {
        throwOnTimeout(() -> {
            view.showMessage(message);
            return null;
        });
    }

    /**
     * Disconnect the view
     * @throws RemoteException If there is an error while disconnecting the view.
     */
    @Override
    public void disconnect() throws RemoteException {
        throwOnTimeout(() -> {
            view.disconnect();
            return null;
        });
    }
}
