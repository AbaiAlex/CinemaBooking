package hu.deik.cinemabooking.model.dto;

import java.time.LocalDate;

/**
 * A foglalásért felelős dto osztály.
 */
public class Foglalas {

    /**
     * A név.
     */
    private String nev;
    /**
     * Az email.
     */
    private String email;
    /**
     * A telefon.
     */
    private String telefon;
    /**
     * Az ár.
     */
    private int ar;
    /**
     * Az előadás napja.
     */
    private LocalDate nap;
    /**
     * Az előadás órája.
     */
    private String eloadasOra;
    /**
     * Az előadás címe.
     */
    private String eloadasCime;

    /**
     * Üres konstruktor.
     */
    public Foglalas() {
    }

    /**
     * Teljes konstruktor.
     *
     * @param nev         a név
     * @param email       az email
     * @param telefon     a telefonszám
     * @param ar          az ár
     * @param nap         a nap
     * @param eloadasOra  az előadás órája
     * @param eloadasCime az előadás címe
     */
    public Foglalas(String nev, String email, String telefon, int ar, LocalDate nap, String eloadasOra, String eloadasCime) {
        this.nev = nev;
        this.email = email;
        this.telefon = telefon;
        this.ar = ar;
        this.nap = nap;
        this.eloadasOra = eloadasOra;
        this.eloadasCime = eloadasCime;
    }

    /**
     * Visszaadja a nevet.
     *
     * @return a név
     */
    public String getNev() {
        return nev;
    }

    /**
     * Beállítja a nevet.
     *
     * @param nev a név
     */
    public void setNev(String nev) {
        this.nev = nev;
    }

    /**
     * Visszaadja az emailt.
     *
     * @return az email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Beállítja az emailt.
     *
     * @param email az email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Visszaadja a telefonszámot.
     *
     * @return a telefonszám
     */
    public String getTelefon() {
        return telefon;
    }

    /**
     * Beállítja a telefonszámot.
     *
     * @param telefon a telefonszám
     */
    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }

    /**
     * Visszaadja az árat.
     *
     * @return az ár
     */
    public int getAr() {
        return ar;
    }

    /**
     * Beállítja az árat.
     *
     * @param ar az ár
     */
    public void setAr(int ar) {
        this.ar = ar;
    }

    /**
     * Visszaadja az előadás napját.
     *
     * @return az előadás napja
     */
    public LocalDate getNap() {
        return nap;
    }

    /**
     * Beállítja az előadás napját.
     *
     * @param nap az előadás napja
     */
    public void setNap(LocalDate nap) {
        this.nap = nap;
    }

    /**
     * Visszaadja az előadás óráját.
     *
     * @return az előadás órája
     */
    public String getEloadasOra() {
        return eloadasOra;
    }

    /**
     * Beállítja az előadás óráját.
     *
     * @param eloadasOra az előadás órája
     */
    public void setEloadasOra(String eloadasOra) {
        this.eloadasOra = eloadasOra;
    }

    /**
     * Visszaadja az előadás címét.
     *
     * @return az előadás címe
     */
    public String getEloadasCime() {
        return eloadasCime;
    }

    /**
     * Beállítja az előadás címét.
     *
     * @param eloadasCime az előadás címe
     */
    public void setEloadasCime(String eloadasCime) {
        this.eloadasCime = eloadasCime;
    }

}
