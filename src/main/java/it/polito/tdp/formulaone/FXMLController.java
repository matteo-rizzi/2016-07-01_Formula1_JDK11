package it.polito.tdp.formulaone;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.formulaone.model.Driver;
import it.polito.tdp.formulaone.model.Model;
import it.polito.tdp.formulaone.model.Season;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

//controller del turno A --> switchare al branch master_turnoB o master_turnoC per turno B o C

public class FXMLController {
	

	private Model model;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ComboBox<Season> boxAnno;

    @FXML
    private TextField textInputK;

    @FXML
    private TextArea txtResult;

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	this.txtResult.clear();
    	
    	if(this.boxAnno.getValue() == null) {
    		this.txtResult.appendText("Errore! Per creare il grafo devi prima selezionare una season dall'apposito menu a tendina!\n");
    		return;
    	}
    	Integer anno = this.boxAnno.getValue().getYear().getValue();
    	this.model.creaGrafo(anno);
    	this.model.migliorRisultato();
    	
    	this.txtResult.appendText("Grafo creato!\n");
    	this.txtResult.appendText("# VERTICI: " + this.model.nVertici() + "\n");
    	this.txtResult.appendText("# ARCHI: " + this.model.nArchi() + "\n\n");

    	this.txtResult.appendText(String.format("Il pilota migliore del %d è %s, con un risultato di %d.\n", anno, this.model.getMigliore().toString(), this.model.getMax()));;
    }

    @FXML
    void doTrovaDreamTeam(ActionEvent event) {
    	this.txtResult.clear();
    	
    	Integer K;
    	try {
    		K = Integer.parseInt(this.textInputK.getText());
    	} catch(NumberFormatException e) {
    		this.txtResult.appendText("Errore! Il valore di K deve essere un numero intero!\n");
    		return;
    	}
    	
    	List<Driver> best = this.model.trovaDreamTeam(K);
    	this.txtResult.appendText("Elenco dei piloti appartenenti al Dream Team:\n");
    	for(Driver d : best) {
    		this.txtResult.appendText(d.toString() + "\n");
    	}
    	
    	this.txtResult.appendText("\nIl tasso di sconfitta del Dream Team è: " + this.model.getMinimo() + "\n");
    }

    @FXML
    void initialize() {
        assert boxAnno != null : "fx:id=\"boxAnno\" was not injected: check your FXML file 'FormulaOne.fxml'.";
        assert textInputK != null : "fx:id=\"textInputK\" was not injected: check your FXML file 'FormulaOne.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'FormulaOne.fxml'.";

    }

	public void setModel(Model model) {
		this.model = model;
		this.boxAnno.getItems().addAll(this.model.getAllSeasons());
	}
}
