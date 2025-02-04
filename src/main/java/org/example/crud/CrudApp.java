package org.example.crud;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.util.List;

public class CrudApp extends Application {

    private TableView<Persona> tabla;
    private TextField txtNombre, txtDireccion, txtTelefonos;
    private Button btnGuardar, btnActualizar, btnEliminar;
    private Persona personaSeleccionada;

    @Override
    public void start(Stage primaryStage) {
        // Campos de entrada
        Label lblNombre = new Label("Nombre:");
        txtNombre = new TextField();

        Label lblDireccion = new Label("Dirección:");
        txtDireccion = new TextField();

        Label lblTelefonos = new Label("Teléfonos (separados por comas):");
        txtTelefonos = new TextField();

        btnGuardar = new Button("Guardar");
        btnGuardar.setOnAction(e -> {
            String nombre = txtNombre.getText();
            String direccion = txtDireccion.getText();
            String[] telefonosArray = txtTelefonos.getText().split(",");
            PersonaDAO.insertarPersona(nombre, direccion, List.of(telefonosArray));

            cargarPersonas();
            limpiarCampos();
        });

        btnActualizar = new Button("Actualizar");
        btnActualizar.setDisable(true);
        btnActualizar.setOnAction(e -> {
            if (personaSeleccionada != null) {
                personaSeleccionada.setNombre(txtNombre.getText());
                personaSeleccionada.setDireccion(txtDireccion.getText());
                personaSeleccionada.setTelefonos(List.of(txtTelefonos.getText().split(",")));

                PersonaDAO.actualizarPersona(personaSeleccionada);
                cargarPersonas();
                limpiarCampos();
            }
        });

        // Botón para eliminar persona
        btnEliminar = new Button("Eliminar");
        btnEliminar.setDisable(true);
        btnEliminar.setOnAction(e -> {
            if (personaSeleccionada != null) {
                PersonaDAO.eliminarPersona(personaSeleccionada.getId());
                cargarPersonas();
                limpiarCampos();
            }
        });

        // Crear la tabla
        tabla = new TableView<>();
        TableColumn<Persona, String> colNombre = new TableColumn<>("Nombre");
        TableColumn<Persona, String> colDireccion = new TableColumn<>("Dirección");
        TableColumn<Persona, String> colTelefonos = new TableColumn<>("Teléfonos");

        colNombre.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getNombre()));
        colDireccion.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getDireccion()));
        colTelefonos.setCellValueFactory(p -> new SimpleStringProperty(String.join(", ", p.getValue().getTelefonos())));

        tabla.getColumns().addAll(colNombre, colDireccion, colTelefonos);
        tabla.setOnMouseClicked(e -> seleccionarPersona());

        cargarPersonas();

        // Diseño de la ventana
        VBox layout = new VBox(10, lblNombre, txtNombre, lblDireccion, txtDireccion, lblTelefonos, txtTelefonos, btnGuardar, btnActualizar, btnEliminar, tabla);
        Scene scene = new Scene(layout, 500, 500);

        primaryStage.setTitle("CRUD con JavaFX y SQL");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void cargarPersonas() {
        List<Persona> personas = PersonaDAO.obtenerPersonas();
        ObservableList<Persona> datos = FXCollections.observableArrayList(personas);
        tabla.setItems(datos);
    }

    private void seleccionarPersona() {
        personaSeleccionada = tabla.getSelectionModel().getSelectedItem();
        if (personaSeleccionada != null) {
            txtNombre.setText(personaSeleccionada.getNombre());
            txtDireccion.setText(personaSeleccionada.getDireccion());
            txtTelefonos.setText(String.join(",", personaSeleccionada.getTelefonos()));
            btnActualizar.setDisable(false);
            btnEliminar.setDisable(false);
        }
    }

    private void limpiarCampos() {
        txtNombre.clear();
        txtDireccion.clear();
        txtTelefonos.clear();
        btnActualizar.setDisable(true);
        btnEliminar.setDisable(true);
        personaSeleccionada = null;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
