package it.polimi.ingsw;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Hello world!
 *
 */
public class App 
{
    private static final Logger LOGGER = Logger.getLogger( App.class.getName() );

    public static void main( String[] args )
    {
        LOGGER.log(Level.FINE, "Hello Word!");
    }
}
