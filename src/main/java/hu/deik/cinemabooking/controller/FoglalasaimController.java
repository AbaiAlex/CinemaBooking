package hu.deik.cinemabooking.controller;


import hu.deik.cinemabooking.dao.DomImpl;
import hu.deik.cinemabooking.model.dto.Foglalas;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.print.*;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.function.Predicate;

public class FoglalasaimController implements Initializable {

    /**
     * A logger.
     */
    private static Logger logger = LoggerFactory.getLogger(FoglalasaimController.class);

    @FXML
    private TableView<Foglalas> foglalasListTableView;

    @FXML
    private TableColumn<Foglalas, String> nevColumn;

    @FXML
    private TableColumn<Foglalas, String> emailColumn;

    @FXML
    private TableColumn<Foglalas, String> telefonColumn;

    @FXML
    private TableColumn<Foglalas, Integer> arColumn;

    @FXML
    private TableColumn<Foglalas, String> napColumn;

    @FXML
    private TableColumn<Foglalas, String> eloadasCimeColumn;

    @FXML
    private TableColumn<Foglalas, String> eloadasOraColumn;

    @FXML
    private TextField search;

    private ObservableList<Foglalas> data = FXCollections.observableArrayList();
    private FilteredList<Foglalas> filteredData = new FilteredList<>(data, e -> true);

    /**
     * Nyomtatás esemény kezelése.
     *
     * @param event az esemény
     */
    @FXML
    void handlePrint(ActionEvent event) {
        logger.info("Starting printing");
        Printer printer = Printer.getDefaultPrinter();
        PrinterJob job = PrinterJob.createPrinterJob();
        PageLayout pageLayout = printer.createPageLayout(Paper.A4, PageOrientation.LANDSCAPE, Printer.MarginType.DEFAULT);
        job.getJobSettings().setPageLayout(pageLayout);

        double rescaleX = pageLayout.getPrintableWidth() / foglalasListTableView.getBoundsInParent().getWidth();
        double rescaleY = pageLayout.getPrintableHeight() / foglalasListTableView.getBoundsInParent().getHeight();

        foglalasListTableView.getStyleClass().remove("orderList");

        Scale rescale = new Scale(rescaleX, rescaleY);

        foglalasListTableView.getTransforms().add(rescale);
        logger.info("TableView rescaled");

        if (job != null) {
            boolean success = job.printPage(foglalasListTableView);
            foglalasListTableView.getTransforms().remove(rescale);
            foglalasListTableView.getStyleClass().add("orderList");
            if (success) {
                job.endJob();
                logger.info("Done printing");
                foglalasListTableView.getTransforms().remove(rescale);
                foglalasListTableView.getStyleClass().add("orderList");
            }
        }
    }

    /**
     * A főképernyőre való visszalépés.
     *
     * @param event az esemény
     */
    @FXML
    void doVisszaToMain(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/Main.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Cinema Booking");
            ((Node) (event.getSource())).getScene().getWindow().hide();
            stage.show();
            logger.info("Opening the Main.fxml");
        } catch (IOException ex) {
            logger.error(ex.getMessage());
        }
    }

    /**
     * Keresés kezelése.
     *
     * @param event az esemény
     */
    @FXML
    void handleSearch(ActionEvent event) {
        search.textProperty().addListener((observableValue, oldValue, newValue) -> {
            filteredData.setPredicate((Predicate<? super Foglalas>) foglalas -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                if (foglalas.getNev().toLowerCase().contains(lowerCaseFilter) || foglalas.getEmail().toLowerCase().contains(lowerCaseFilter) ||
                        foglalas.getTelefon().contains(lowerCaseFilter) || String.valueOf(foglalas.getAr()).contains(lowerCaseFilter) || foglalas.getEloadasCime().contains(lowerCaseFilter)
                        || foglalas.getEloadasOra().contains(lowerCaseFilter)) {
                    return true;
                }
                return false;
            });
        });
        SortedList<Foglalas> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(foglalasListTableView.comparatorProperty());
        foglalasListTableView.setItems(sortedData);
    }

    /**
     * Foglalásaim controller inicializáló metódusa.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        logger.info("FoglalasaimController opened");
        setCellTable();
        loadDateFromXml();
    }

    private void setCellTable() {
        nevColumn.setCellValueFactory(new PropertyValueFactory<>("nev"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        telefonColumn.setCellValueFactory(new PropertyValueFactory<>("telefon"));
        arColumn.setCellValueFactory(new PropertyValueFactory<>("ar"));
        napColumn.setCellValueFactory(new PropertyValueFactory<>("eloadas"));
        eloadasCimeColumn.setCellValueFactory(new PropertyValueFactory<>("datum"));
        eloadasOraColumn.setCellValueFactory(new PropertyValueFactory<>("ora"));
    }

    private void loadDateFromXml() {
        DomImpl dom = new DomImpl();
        dom.listFoglalasok();
        for (int i = 0; i < DomImpl.foglalasok.size(); i++) {
            data.add(
                    new Foglalas(
                            DomImpl.foglalasok.get(i).getNev(),
                            DomImpl.foglalasok.get(i).getEmail(),
                            DomImpl.foglalasok.get(i).getTelefon(),
                            DomImpl.foglalasok.get(i).getAr(),
                            DomImpl.foglalasok.get(i).getNap(),
                            DomImpl.foglalasok.get(i).getEloadasCime(),
                            DomImpl.foglalasok.get(i).getEloadasOra()
                    ));
        }
        foglalasListTableView.setItems(data);
    }

}
