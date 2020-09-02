package it.polito.tdp.seriea;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

import it.polito.tdp.seriea.model.Model;
import it.polito.tdp.seriea.model.Season;
import it.polito.tdp.seriea.model.Team;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;

public class FXMLController {
	
	private Model model;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ChoiceBox<Team> boxSquadra;

    @FXML
    private Button btnSelezionaSquadra;

    @FXML
    private Button btnTrovaAnnataOro;

    @FXML
    private Button btnTrovaCamminoVirtuoso;

    @FXML
    private TextArea txtResult;

    @FXML
    void doSelezionaSquadra(ActionEvent event) {
    	Team t=this.boxSquadra.getValue();
    	if(t==null) {
    		txtResult.appendText("Errore, seleziona una squadra");
    		return;
    	}
    	Map<Season, Integer>mappa=this.model.puntiSquadra(t);
    	for(Season s: mappa.keySet()) {
    		txtResult.appendText(String.format("%s: %d \n", s.getDescription(),mappa.get(s)));
    	}

    }

    @FXML
    void doTrovaAnnataOro(ActionEvent event) {
    	this.model.creaGrafo();
    	txtResult.appendText("Annata d'oro");
    	txtResult.appendText(this.model.getBest().getDescription());
    	txtResult.appendText(" "+this.model.getDifferenzaPesiBest());
    }

    @FXML
    void doTrovaCamminoVirtuoso(ActionEvent event) {

    }

    @FXML
    void initialize() {
        assert boxSquadra != null : "fx:id=\"boxSquadra\" was not injected: check your FXML file 'SerieA.fxml'.";
        assert btnSelezionaSquadra != null : "fx:id=\"btnSelezionaSquadra\" was not injected: check your FXML file 'SerieA.fxml'.";
        assert btnTrovaAnnataOro != null : "fx:id=\"btnTrovaAnnataOro\" was not injected: check your FXML file 'SerieA.fxml'.";
        assert btnTrovaCamminoVirtuoso != null : "fx:id=\"btnTrovaCamminoVirtuoso\" was not injected: check your FXML file 'SerieA.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'SerieA.fxml'.";

    }

	public void setModel(Model model) {
		this.model = model;
		this.boxSquadra.getItems().clear();
		this.boxSquadra.getItems().addAll(this.model.getElencoSquadre());
	}
}
