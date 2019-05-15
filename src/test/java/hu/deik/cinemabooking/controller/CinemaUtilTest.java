package hu.deik.cinemabooking.controller;

import hu.deik.cinemabooking.service.util.CinemaUtil;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.assertEquals;

/**
 * {@link CinemaUtil} osztály tesztesetei.
 */
public class CinemaUtilTest {

    /**
     * Logger.
     */
    private static Logger logger = LoggerFactory.getLogger(CinemaUtilTest.class);

    /**
     * Üres constructor.
     */
    public CinemaUtilTest() {
    }

    /**
     * Test of calculateAr method, of class MainController.
     */
    @Test
    public void testCalculateAr() {
        logger.info("testCalculateAr");
        int jegyekSzama = 0;
        int expResult = 0;
        int result = CinemaUtil.calculateAr(jegyekSzama);
        assertEquals(expResult, result);
        jegyekSzama = 1;
        expResult = 1500;
        result = CinemaUtil.calculateAr(jegyekSzama);
        assertEquals(expResult, result);
        jegyekSzama = 2;
        expResult = 3000;
        result = CinemaUtil.calculateAr(jegyekSzama);
        assertEquals(expResult, result);
    }
}
