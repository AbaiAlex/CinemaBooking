package hu.deik.cinemabooking.controller;

import hu.deik.cinemabooking.model.dto.Foglalas;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.Test;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.assertEquals;

/**
 * Main controller teszt osztály.
 */
public class MainControllerTest {

    public MainControllerTest() {
    }

    /**
     * Test of validate method, of class MainController.
     */
    @Test
    public void testValidate() {
        System.out.println("validate");
        Foglalas foglalas = new Foglalas();
        MainController instance = new MainController();
        boolean expResult = false;
        boolean result = instance.validate(foglalas);
        assertEquals(expResult, result);
        foglalas = new Foglalas();
        foglalas.setNev("Senki");
        result = instance.validate(foglalas);
        assertEquals(expResult, result);
        foglalas.setNev("Kiss Béla");
        foglalas.setEmail("kiss.bela@email.com");
        foglalas.setTelefon("+36201234567");
        foglalas.setEloadasCime("Avengers");
        foglalas.setAr(1500);
        LocalDate now = DateUtils.truncate(new Date(), Calendar.DATE).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate plusHours = DateUtils.addHours(java.sql.Date.valueOf(now), 2).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        foglalas.setEloadasOra(plusHours.toString());
        foglalas.setNap(now);
        expResult = true;
        result = instance.validate(foglalas);
        assertEquals(expResult, result);
    }

}
