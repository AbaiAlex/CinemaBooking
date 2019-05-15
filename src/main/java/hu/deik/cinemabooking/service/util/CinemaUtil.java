package hu.deik.cinemabooking.service.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Segédmetódusokat tartalmazó osztály.
 */
public class CinemaUtil {

    /**
     * Logger.
     */
    private static Logger logger = LoggerFactory.getLogger(CinemaUtil.class);


    /**
     * Egy jegy fix ára.
     */
    private static final int EGY_JEGY_ARA = 1500;

    /**
     * Ár kalkulátor segédmetódus. Egy jegy árát a {@link #EGY_JEGY_ARA} határozza meg.
     *
     * @param jegyekSzama a felületen felvett jegyek száma
     * @return az ár
     */
    public static int calculateAr(int jegyekSzama) {
        logger.info("Calculating price");
        return EGY_JEGY_ARA * jegyekSzama;
    }
}
