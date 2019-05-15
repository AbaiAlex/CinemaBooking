package hu.deik.cinemabooking.controller;

import hu.deik.cinemabooking.dao.DomImpl;
import hu.deik.cinemabooking.model.dto.Foglalas;
import hu.deik.cinemabooking.service.util.CinemaUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.ResourceBundle;

/**
 * A fő controller osztály.
 */
public class MainController implements Initializable {

    /**
     * A logger.
     */
    private static Logger logger = LoggerFactory.getLogger(FoglalasaimController.class);

    /**
     * Név beviteli mező.
     */
    @FXML
    private TextField nevInput;

    /**
     * Email beviteli mező.
     */
    @FXML
    private TextField emailInput;

    /**
     * Telefonszám beviteli mező.
     */
    @FXML
    private TextField telefonInput;

    /**
     * Jegyek száma beviteli mező.
     */
    @FXML
    private TextField jegyekSzamaInput;

    /**
     * Dátumválasztó beviteli mező.
     */
    @FXML
    private DatePicker selectDate;

    /**
     * Film címe érték.
     */
    @FXML
    private Text filmCimeText;

    /**
     * Időpont (óra) érték.
     */
    @FXML
    private Text idopontText;

    @FXML
    private Text validator;

    /**
     * Email címeket tartalmazó regex {@link String};
     */
    private static final String EMAIL_REGEX = "^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,6}$";
    /**
     * Magyarországi telefonszámokat tartalmazó regex {@link String}.
     */
    private static final String PHONE_NUMBER_REGEX = "((?:\\+?3|0)6)(?:-|\\()?(\\d{1,2})(?:-|\\))?(\\d{3})-?(\\d{3,4})";
    /**
     * Szám típusú regex {@link String}.
     */
    private static final String NUMBER_REGEX = "\\d";

    /**
     * A foglalásért felelős esemény.
     *
     * @param event az esemény
     */
    @FXML
    void doFoglalas(ActionEvent event) {
        logger.info("Creating Foglalas");
        Foglalas foglalas = new Foglalas();
        foglalas.setNev(nevInput.getText());
        foglalas.setEmail(emailInput.getText());
        foglalas.setTelefon(telefonInput.getText());
        String jegyekSzama;
        if (jegyekSzamaInput.getText() == null || jegyekSzamaInput.getText().isEmpty()) {
            jegyekSzama = "0";
        } else {
            jegyekSzama = jegyekSzamaInput.getText();
        }
        foglalas.setAr(CinemaUtil.calculateAr(Integer.valueOf(jegyekSzama)));
        foglalas.setNap(selectDate.getValue());
        foglalas.setEloadasOra(idopontText.getText());
        foglalas.setEloadasCime(filmCimeText.getText());
        logger.info("Foglalas created: " + foglalas.toString());

        if (validate(foglalas)) {
            logger.info("Validation success");
            DomImpl domImpl = new DomImpl();
            domImpl.saveFoglalas(foglalas);
            logger.info("Foglalas saved");

        }
    }

    /**
     * {@link Foglalas} létrehozása előtti validáló segédmetódus.
     *
     * @param foglalas a foglalás dto.
     * @return {@code true}, amennyiben minden adat helyesen van kitöltve, ellenkező esetben {@code false}.
     */
    public boolean validate(Foglalas foglalas) {
        logger.info("Validating");
        boolean valid = true;
        LocalDate now = LocalDate.now();
        LocalDate ma = DateUtils.truncate(Date.valueOf(now), Calendar.DATE).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate ezAzOra = DateUtils.truncate(Date.valueOf(now), Calendar.HOUR_OF_DAY).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate eloadasNapja = foglalas.getNap();
        LocalDate eloadasOraja = now;
        if (eloadasNapja != null) {
            eloadasOraja = DateUtils.setHours(Date.valueOf(eloadasNapja), Integer.valueOf(foglalas.getEloadasOra().substring(0, 2)))
                    .toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        }
        String jegyekSzamaInputText = "0";
        if (jegyekSzamaInput != null) {
            jegyekSzamaInputText = jegyekSzamaInput.getText();
        }
        if (foglalas.getNev() == null || foglalas.getNev().isEmpty()) {
            if (validator != null) {
                validator.setText("A név megadása kötelező!");
            }
            valid = false;
        } else if (foglalas.getEmail() == null || foglalas.getEmail().isEmpty()) {
            if (validator != null) {
                validator.setText("Az e-mail cím megadása kötelező!");
            }
            valid = false;
        } else if (!foglalas.getEmail().matches(EMAIL_REGEX)) {
            if (validator != null) {
                validator.setText("Az email cím nem valós!");
            }
            valid = false;
        } else if (foglalas.getTelefon() == null || foglalas.getTelefon().isEmpty()) {
            if (validator != null) {
                validator.setText("A telefonszám megadása kötelező!");
            }
            valid = false;
        } else if (!foglalas.getTelefon().matches(PHONE_NUMBER_REGEX)) {
            if (validator != null) {
                validator.setText("A telefonszám csak számokat tartalmazhat!\n" +
                                  "Formátuma tetszőleges,\n" +
                                  "+36 vagy 06-tal kezdődő magyarországi lehet!");
            }
            valid = false;
        } else if (foglalas.getNap() == null) {
            if (validator != null) {
                validator.setText("Kérem válasszon napot!");
            }
            valid = false;
        } else if (ma.isAfter(foglalas.getNap())) {
            if (validator != null) {
                validator.setText("A dátum nem lehet múltbéli!");
            }
            valid = false;
        } else if (foglalas.getEloadasOra() == null || foglalas.getEloadasOra().isEmpty()) {
            if (validator != null) {
                validator.setText("Kérem válasszon időpontot!");
            }
            valid = false;
        } else if (ezAzOra.isAfter(eloadasOraja)) {
            if (validator != null) {
                validator.setText("Az előadás órája nem lehet múltbéli!");
            }
            valid = false;
        } else if (foglalas.getEloadasCime() == null || foglalas.getEloadasCime().isEmpty()) {
            if (validator != null) {
                validator.setText("Kérem válasszon előadást!");
            }
            valid = false;
        } else if (jegyekSzamaInputText == null || jegyekSzamaInputText.isEmpty()) {
            if (validator != null) {
                validator.setText("Kérem adja meg a jegyek számát!");
            }
            valid = false;
        } else if (!jegyekSzamaInputText.matches(NUMBER_REGEX)) {
            if (validator != null) {
                validator.setText("Kérem számot adjon meg a jegyek számára!");
            }
            valid = false;
        }
        if (valid) {
            logger.info("Validation success");
            if (validator != null) {
                validator.setText(null);
            }
        } else {
            logger.info("Validation failed");
        }
        return valid;
    }

    /**
     * Bezárja az alkalmazást.
     *
     * @param event az esemény
     */
    @FXML
    void doKilepes(ActionEvent event) {
        logger.info("Exit");
        System.exit(0);
    }

    /**
     * Megnyitja a {@link FoglalasaimController} osztály által vezérelt fxml fájlt.
     *
     * @param event az esemény.
     */
    @FXML
    void showFoglalasaim(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/Foglalasaim.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Foglalásaim");
            ((Node) (event.getSource())).getScene().getWindow().hide();
            stage.show();
            logger.info("Opening the Foglalasaim.fxml");
        } catch (IOException ex) {
            logger.error(ex.getMessage());
        }
    }

    /**
     * Beállítja a kiválasztott filmet.
     *
     * @param event az esemény
     */
    @FXML
    void setFilm(ActionEvent event) {
        String filmCime = ((MenuItem) event.getSource()).getText();
        logger.info("Selected film: " + filmCime);
        filmCimeText.setText(filmCime);
    }

    /**
     * Beállítja a kiválasztott időpontot.
     *
     * @param event az esemény
     */
    @FXML
    void setIdopont(ActionEvent event) {
        String idopont = ((MenuItem) event.getSource()).getText();
        idopontText.setText(idopont);
        logger.info("Time is set to: " + idopont);
    }

    /**
     * Inicializálja a kontroller osztályt.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        logger.info("MainController opened");
    }

}
