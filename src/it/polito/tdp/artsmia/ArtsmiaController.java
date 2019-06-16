/**
 * Sample Skeleton for 'Artsmia.fxml' Controller Class
 */

package it.polito.tdp.artsmia;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.artsmia.model.ArtObject;
import it.polito.tdp.artsmia.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class ArtsmiaController {
	
	Model model;
	int dimensioneComponente = 0;
	Integer idInserito= null;

	@FXML // ResourceBundle that was given to the FXMLLoader
	private ResourceBundle resources;

	@FXML // URL location of the FXML file that was given to the FXMLLoader
	private URL location;

	@FXML // fx:id="boxLUN"
	private ChoiceBox<Integer> boxLUN; // Value injected by FXMLLoader

	@FXML // fx:id="btnCalcolaComponenteConnessa"
	private Button btnCalcolaComponenteConnessa; // Value injected by FXMLLoader

	@FXML // fx:id="btnCercaOggetti"
	private Button btnCercaOggetti; // Value injected by FXMLLoader

	@FXML // fx:id="btnAnalizzaOggetti"
	private Button btnAnalizzaOggetti; // Value injected by FXMLLoader

	@FXML // fx:id="txtObjectId"
	private TextField txtObjectId; // Value injected by FXMLLoader

	@FXML // fx:id="txtResult"
	private TextArea txtResult; // Value injected by FXMLLoader

	@FXML
	void doAnalizzaOggetti(ActionEvent event) {
		model.creaGrafo();
		txtResult.setText("Grafo creato");
	}

	@FXML
	void doCalcolaComponenteConnessa(ActionEvent event) {
		txtResult.clear();
		
		//Scegliere id = 63, la cui componente connessa ha dimensione 6
		try {
			idInserito = Integer.parseInt(txtObjectId.getText());
		} catch(NumberFormatException e) {
			txtResult.setText("Inserire numero");
			return;
		}
		
		
		if(model.isCorrect(idInserito) == false) {
			txtResult.setText("Id inserito non è presente!");
			return;
		}
		
		dimensioneComponente = model.getComponenteConnessa(idInserito).size() ;
		txtResult.appendText("La componente connessa di "+ idInserito + " ha "+dimensioneComponente+" vertici.");
		
		for(int i = 2; i<=dimensioneComponente; i++) {
			boxLUN.getItems().add(i);
		}
		
	}

	@FXML
	void doCercaOggetti(ActionEvent event) {
		
		int lunghezza = boxLUN.getValue();
		
		List<ArtObject> opereCammino = model.camminoPesoMassimo(lunghezza, idInserito);
		txtResult.setText("Cammino di lunghezza "+lunghezza+" a partire da " + idInserito +"\n");
		
		for(ArtObject a : opereCammino) {
			txtResult.appendText("Opera "+a.toString()+" \n");
		}
		txtResult.appendText("Peso totale "+ model.peso(opereCammino));
	}

	@FXML // This method is called by the FXMLLoader when initialization is complete
	void initialize() {
		assert boxLUN != null : "fx:id=\"boxLUN\" was not injected: check your FXML file 'Artsmia.fxml'.";
		assert btnCalcolaComponenteConnessa != null : "fx:id=\"btnCalcolaComponenteConnessa\" was not injected: check your FXML file 'Artsmia.fxml'.";
		assert btnCercaOggetti != null : "fx:id=\"btnCercaOggetti\" was not injected: check your FXML file 'Artsmia.fxml'.";
		assert btnAnalizzaOggetti != null : "fx:id=\"btnAnalizzaOggetti\" was not injected: check your FXML file 'Artsmia.fxml'.";
		assert txtObjectId != null : "fx:id=\"txtObjectId\" was not injected: check your FXML file 'Artsmia.fxml'.";
		assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Artsmia.fxml'.";

	}
	
	public void setModel(Model model) {
		this.model = model;
	}
}
