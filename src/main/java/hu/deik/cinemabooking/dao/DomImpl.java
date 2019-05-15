package hu.deik.cinemabooking.dao;

import hu.deik.cinemabooking.model.dto.Foglalas;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * A {@link Dom} interfész implementációja.
 */
public class DomImpl implements Dom {

    /**
     * A logger.
     */
    private static Logger logger = LoggerFactory.getLogger(DomImpl.class);

    /**
     * Dokumentum builder factory.
     */
    private DocumentBuilderFactory dbFactory;

    /**
     * Document builder.
     */
    private DocumentBuilder dBuilder;

    /**
     * Documentum.
     */
    private Document doc;

    /**
     * A foglalásokat tartalmazó {@link ArrayList}.
     */
    private List<Foglalas> foglalasok;

    /**
     * A {@link DomImpl} osztály konstruktora.
     */
    public DomImpl() {
        dbFactory = DocumentBuilderFactory.newInstance();
        try {
            dBuilder = dbFactory.newDocumentBuilder();
            doc = dBuilder.parse(this.getClass().getClassLoader().getResourceAsStream("database/foglalasok.xml"));
        } catch (SAXException | IOException | ParserConfigurationException e) {
            logger.error("Hiba történt: " + e.getMessage());
        }
    }

    @Override
    public void saveFoglalas(Foglalas foglalas) {

        logger.info("Saving foglalas");

        NodeList nodes = doc.getElementsByTagName("foglalasok");
        Element rootElement = (Element) nodes.item(0);


        NodeList foglalasList = rootElement.getElementsByTagName("foglalas");

        Long id = 0L;

        if (foglalasList.getLength() > 0) {
            Element foglalasElement = (Element) foglalasList.item(foglalasList.getLength() - 1);
            id = Long.valueOf(foglalasElement.getElementsByTagName("id").item(0).getTextContent());
            id++;
        }

        Element foglalasNode = doc.createElement("foglalas");

        Element idNode = doc.createElement("id");
        idNode.appendChild(doc.createTextNode(id.toString()));

        Element nevNode = doc.createElement("nev");
        nevNode.appendChild(doc.createTextNode(foglalas.getNev()));

        Element emailNode = doc.createElement("email");
        emailNode.appendChild(doc.createTextNode(foglalas.getEmail()));

        Element telefonNode = doc.createElement("telefon");
        telefonNode.appendChild(doc.createTextNode(foglalas.getTelefon()));

        Element arNode = doc.createElement("ar");
        arNode.appendChild(doc.createTextNode(String.valueOf(foglalas.getAr())));

        Element eloadasNode = doc.createElement("eloadas");
        eloadasNode.appendChild(doc.createTextNode(foglalas.getEloadasCime()));

        Element datumNode = doc.createElement("datum");
        datumNode.appendChild(doc.createTextNode(foglalas.getNap().toString()));

        Element oraNode = doc.createElement("ora");
        oraNode.appendChild(doc.createTextNode(foglalas.getEloadasOra()));

        foglalasNode.appendChild(idNode);
        foglalasNode.appendChild(nevNode);
        foglalasNode.appendChild(emailNode);
        foglalasNode.appendChild(telefonNode);
        foglalasNode.appendChild(arNode);
        foglalasNode.appendChild(eloadasNode);
        foglalasNode.appendChild(datumNode);
        foglalasNode.appendChild(oraNode);

        rootElement.appendChild(foglalasNode);

        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer;
            transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "3");
            DOMSource source = new DOMSource(doc);

            URL url = this.getClass().getClassLoader().getResource("database/foglalasok.xml");
            File file;
            file = new File(URLDecoder.decode(url.getPath(), "UTF-8"));
            StreamResult result = new StreamResult(file);
            transformer.transform(source, result);
        } catch (UnsupportedEncodingException | TransformerException ex) {
            logger.info("Hiba történt: " + ex.getMessage());
        }

    }

    @Override
    public void listFoglalasok() {

        logger.info("Listing foglalasok");

        NodeList nodeList = doc.getChildNodes();

        Element root = (Element) nodeList.item(0);
        NodeList foglalasList = root.getElementsByTagName("foglalas");

        List<Foglalas> foglalasok = getFoglalasok();
        if (foglalasok == null) {
            foglalasok = new ArrayList<>();
        }

        for (int i = 0; i < foglalasList.getLength(); i++) {
            Element foglalasElement = (Element) foglalasList.item(i);
            Foglalas foglalas = new Foglalas();
            foglalas.setId(Long.valueOf(foglalasElement.getElementsByTagName("id").item(0).getTextContent()));
            foglalas.setNev(foglalasElement.getElementsByTagName("nev").item(0).getTextContent());
            foglalas.setEmail(foglalasElement.getElementsByTagName("email").item(0).getTextContent());
            foglalas.setTelefon(foglalasElement.getElementsByTagName("telefon").item(0).getTextContent());
            foglalas.setAr(Integer.valueOf(foglalasElement.getElementsByTagName("ar").item(0).getTextContent()));
            foglalas.setEloadasCime(foglalasElement.getElementsByTagName("eloadas").item(0).getTextContent());
            foglalas.setNap(LocalDate.parse(foglalasElement.getElementsByTagName("datum").item(0).getTextContent()));
            foglalas.setEloadasOra(foglalasElement.getElementsByTagName("ora").item(0).getTextContent());

            foglalasok.add(foglalas);
            setFoglalasok(foglalasok);
        }
    }

    /**
     * Visszaadja a foglalásokat.
     *
     * @return a foglalások
     */
    public List<Foglalas> getFoglalasok() {
        return foglalasok;
    }

    /**
     * Beállítja a foglalásokat.
     *
     * @param foglalasok a foglalás
     */
    public void setFoglalasok(List<Foglalas> foglalasok) {
        this.foglalasok = foglalasok;
    }
}

