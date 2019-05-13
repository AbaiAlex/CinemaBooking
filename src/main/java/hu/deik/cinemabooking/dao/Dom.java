package hu.deik.cinemabooking.dao;

import hu.deik.cinemabooking.model.dto.Foglalas;

/**
 * A Dom interfész.
 */
public interface Dom {

    /**
     * Foglalás mentését megvalósító metódus.
     *
     * @param foglalas a foglalás.
     */
    void saveFoglalas(Foglalas foglalas);

    /**
     * foglalások listázását megvalósító metódus.
     */
    void listFoglalasok();
}
