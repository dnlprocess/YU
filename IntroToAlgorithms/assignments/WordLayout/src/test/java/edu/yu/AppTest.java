package edu.yu;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.yu.introtoalgs.WordLayout;
import edu.yu.introtoalgs.WordLayoutBase;
import edu.yu.introtoalgs.WordLayoutBase.Grid;
import edu.yu.introtoalgs.WordLayoutBase.LocationBase;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    private static final Logger logger = LogManager.getLogger(AppTest.class);
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue()
    {
        final int nRows =3;
        final int nColumns =3;
        final List<String> words = List.of("CAT", "DOG", "BOB");
        logger.info("Using this list of words: {}" , words);

        final WordLayoutBase layout = new WordLayout(nRows, nColumns, words);
        for (String word: words) {
            final List<LocationBase> locations = layout.locations(word);
            logger.info("Locations for word {}: {}", word, locations);
        }

        final Grid grid = layout . getGrid ();
        logger.info("The filled in grid: {}" , grid );
        logger.info("succes");

    }
}
