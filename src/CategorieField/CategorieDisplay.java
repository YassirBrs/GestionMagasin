package CategorieField;

import java.io.File;
import java.util.Collection;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class CategorieDisplay extends Application{

	public static int prdF = 0;
	private static TextField txtintitu = new TextField();
	Button btnAjouter = new Button("Ajouter");
	Button btnModifier = new Button("Modifier");
	Button btnSupprimer = new Button("Supprimer");
	Button btnAnnuler = new Button("Annuler");
	public static Text message = new Text("");
	private static Text messageSelect = new Text("");
	private static Text messageAction = new Text("");
	public static CategorieDao cdao = new CategorieDaoImpl();
	public static Categorie categorieSelected = null;
	public static TableView<Categorie> table = new TableView<>();
	public static ObservableList<Categorie> categories = FXCollections.observableArrayList();

	public static void remplire(){
		txtintitu.setText(categorieSelected.getIntitule());
	}
	
	public static void vider(){
		txtintitu.setText("");
		categorieSelected = null;
	}
	
	private static void getCategories(){
		categories.clear();
		Collection<Categorie> cats = cdao.getAll();
		for(Categorie c:cats){
			categories.add(c);
		}
	}

	public static void addCategorie(Categorie c){
		cdao.insert(c);
		categories.clear();
		getCategories();
		table.setItems(categories);
		categorieSelected = null;
	}
	
	public static void updateCategorie(Categorie c){
		cdao.update(c);
		categories.clear();
		getCategories();
		table.setItems(categories);
		categorieSelected = null;
	}
	
	public static void deleteCategorie(long codecateg){
		cdao.delete(codecateg);
		categories.clear();
		getCategories();
		table.setItems(categories);
		categorieSelected = null;
	}
	
	@Override
	public void start(Stage window) throws Exception {
//		window.initModality(Modality.APPLICATION_MODAL);
		window.setWidth(500);
		window.setHeight(712);
		window.setTitle("Gestion des categories");
		BorderPane brd = new BorderPane();
		brd.setTop(createContentTop());
		brd.setCenter(createContentCenter());
		Scene scene = new Scene(brd, 200, 300, Color.WHITE);
		File f = new File("css/style.css");
		scene.getStylesheets().clear();
		scene.getStylesheets().add("file:css/style.css");
		window.setScene(scene);
		window.show();
	}

	private Pane createContentCenter(){
		Pane pane = new VBox();
		((VBox)pane).setSpacing(12);
		pane.setId("CatCenter");
		pane.setPrefWidth(500);
		messageSelect.setFill(Color.RED);
		pane.getChildren().add(messageSelect);
		Text labelCodeCat=new Text("Code:");
		Text labelIntitule=new Text("Intitule:");
		pane.getChildren().add(labelIntitule);
		pane.getChildren().add(txtintitu);
		messageAction.setFill(Color.RED);
		pane.getChildren().add(messageAction);
		pane.getChildren().add(createContentBottom());
		return pane;
	}

	private Pane createContentBottom(){
		Pane pane = new HBox();
		((HBox) pane).setSpacing(5);
		pane.getChildren().add(btnAjouter);
		btnAjouter.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
            	try {
					String intitule = txtintitu.getText();
	                System.out.println("Categorie ajoute avec succes");
	                Categorie nc = new Categorie(intitule);
	                addCategorie(nc);
	                vider();
	                messageSelect.setText("");
                	messageAction.setFill(Color.LIMEGREEN);
            		messageAction.setText("Categorie bien ajouter.");
				} catch (Exception e) {
					messageSelect.setText("");
					messageAction.setFill(Color.RED);
            		messageAction.setText("Erreur !");
				}
            }
        });
		
		pane.getChildren().add(btnModifier);
		btnModifier.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
            	try {
					if(categorieSelected != null){
	                	Categorie c = new Categorie();
	                	c.setCodecat(categorieSelected.getCodecat());
	                	c.setIntitule(txtintitu.getText());
	                	updateCategorie(c);
	                	vider();
	                	messageSelect.setText("");
	                	messageAction.setFill(Color.LIMEGREEN);
	            		messageAction.setText("Categorie bien modifier.");
	            	}else{
	            		messageSelect.setFill(Color.RED);
	            		messageSelect.setText("Categorie n'est pas selectionné !");
	            		messageAction.setFill(Color.RED);
	            		messageAction.setText("Veuillez selectionner une categorie !");
	            	}
				} catch (Exception e) {
					messageSelect.setText("");
					messageAction.setFill(Color.RED);
            		messageAction.setText("Erreur !");
				}
            	
            }
        });
		pane.getChildren().add(btnSupprimer);
		btnSupprimer.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
            	try {
					if(categorieSelected != null){
	        			deleteCategorie(categorieSelected.getCodecat());
	        			vider();
	                	messageSelect.setText("");
	                	messageAction.setFill(Color.LIMEGREEN);
	            		messageAction.setText("Categorie bien supprimer.");
	            	}else{
	            		messageSelect.setFill(Color.RED);
	            		messageSelect.setText("Categorie n'est pas selectionné !");
	            		messageAction.setFill(Color.RED);
	            		messageAction.setText("Veuillez selectionner une categorie !");
	            	}
				} catch (Exception e) {
					messageSelect.setText("");
					messageAction.setFill(Color.RED);
            		messageAction.setText("Erreur !");
				}
            }
        });
		
		pane.getChildren().add(btnAnnuler);
		btnAnnuler.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
            	try {
            		messageSelect.setText("");
            		messageAction.setText("");
            		Stage stage = (Stage) btnAnnuler.getScene().getWindow();
            	    stage.close();
				} catch (Exception e) {
					messageSelect.setText("");
					messageAction.setFill(Color.RED);
            		messageAction.setText("Erreur !");
				}
            }
        });
		return pane;
	}
	
	private  Pane createContentTop(){
		Pane pane = new VBox();
		getCategories();
		table.getColumns().clear();
		TableColumn<Categorie, Long> codecatCol = new TableColumn<>("Code");
		codecatCol.setCellValueFactory(new PropertyValueFactory<>("codecat"));
		TableColumn<Categorie, String> intituleCol = new TableColumn<>("Intitule");
		intituleCol.setCellValueFactory(new  PropertyValueFactory<>("intitule"));
		table.getColumns().addAll(codecatCol, intituleCol);
		table.setItems(categories);
		if(prdF == 1){
			table.setRowFactory(obj->{
				TableRow<Categorie> row = new TableRow<>();
				row.setOnMouseClicked(event->{
					try {
						if (event.getClickCount() == 2 && (!row.isEmpty())) {
							categorieSelected = row.getItem();
							remplire();
							messageAction.setText("");
							messageSelect.setFill(Color.ORANGE);
							messageSelect.setText("Categorie bien selectionne pour ajouter le nouveau produit.");
							Stage stage = (Stage)table.getScene().getWindow();
			        	    stage.close();
						}
					} catch (Exception e) {
						messageSelect.setText("");
						messageAction.setFill(Color.RED);
	            		messageAction.setText("Erreur !");
					}
				});
				return row;
			});
		}else{
			table.setRowFactory(obj->{
				TableRow<Categorie> row = new TableRow<>();
				row.setOnMouseClicked(event->{
					try {
						if (event.getClickCount() == 1 && (!row.isEmpty())) {
							categorieSelected = row.getItem();
							remplire();
							messageAction.setText("");
							messageSelect.setFill(Color.ORANGE);
		            		messageSelect.setText("Categorie bien selectionn�. ");
						}
					} catch (Exception e) {
						messageSelect.setText("");
						messageAction.setFill(Color.RED);
	            		messageAction.setText("Erreur !");
					}
				});
				return row;
			});
		}
		pane.setPrefHeight(300);
		pane.getChildren().addAll(createContentTopTitle(), table);
		return pane;
	}
	
	private Pane createContentTopTitle(){
		Pane pane = new HBox();
		pane.setId("headTitle");
		pane.setMinHeight(50);
		((HBox)pane).setAlignment(Pos.CENTER_LEFT);
		Text titre = new Text(" Liste des categories :");
		titre.setFont(Font.font("Calibri", FontWeight.BOLD, 20));
		titre.setFill(Color.WHITE);
		pane.getChildren().add(titre);
		return pane;
	}
	
	/*public static void main(String[] args) {
		Application.launch(args);
	}*/
	
}
