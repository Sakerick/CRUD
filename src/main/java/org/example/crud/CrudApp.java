package org.example.crud;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javax.swing.*;
import java.util.List;

public class CrudApp extends Application {

    private TableView<Persona> tabla;
    private TextField txtNombre, txtDireccion, txtTelefonos;
    private Button btnGuardar, btnActualizar, btnEliminar;
    private Persona personaSeleccionada;
    private TableView<Vehiculo> tablaVehiculos;
    private Button btnAgregarVehiculo;
    private Button btnEliminarVehiculo;

    @Override
    public void start(Stage primaryStage) {
        // Inicializar tabla de vehículos antes de usarla
        configurarTablaVehiculos();

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

        btnEliminar = new Button("Eliminar");
        btnEliminar.setDisable(true);
        btnEliminar.setOnAction(e -> {
            if (personaSeleccionada != null) {
                PersonaDAO.eliminarPersona(personaSeleccionada.getId());
                cargarPersonas();
                cargarVehiculos();
                limpiarCampos();
            }
        });

        btnEliminarVehiculo = new Button("Eliminar Vehículo");
        btnEliminarVehiculo.setDisable(true);
        btnEliminarVehiculo.setOnAction(e -> {
            Vehiculo vehiculoSeleccionado = tablaVehiculos.getSelectionModel().getSelectedItem();
            if (vehiculoSeleccionado != null) {
                VehiculoDAO.eliminarVehiculo(vehiculoSeleccionado.getId());
                cargarVehiculos(); // Recargar la tabla después de eliminar
            }
        });

        tablaVehiculos.setOnMouseClicked(e -> {
            Vehiculo vehiculoSeleccionado = tablaVehiculos.getSelectionModel().getSelectedItem();
            btnEliminarVehiculo.setDisable(vehiculoSeleccionado == null);
        });


        ComboBox<String> comboBoxVehiculos = new ComboBox<>();
        comboBoxVehiculos.getItems().addAll("Automovil", "Bicicleta", "Motocicleta", "Vehiculo Maritimo", "Vehiculo de Carga");
        comboBoxVehiculos.setValue("Automovil");
        btnAgregarVehiculo = new Button("Agregar Vehiculo");
        btnAgregarVehiculo.setOnAction(e -> agregarVehiculo(comboBoxVehiculos.getValue()));

        // Crear la tabla de personas
        tabla = new TableView<>();
        TableColumn<Persona, String> colNombre = new TableColumn<>("Nombre");
        TableColumn<Persona, String> colDireccion = new TableColumn<>("Dirección");
        TableColumn<Persona, String> colTelefonos = new TableColumn<>("Teléfonos");

        colNombre.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getNombre()));
        colDireccion.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getDireccion()));
        colTelefonos.setCellValueFactory(p -> new SimpleStringProperty(String.join(", ", p.getValue().getTelefonos())));

        tabla.getColumns().addAll(colNombre, colDireccion, colTelefonos);
        tabla.setOnMouseClicked(e -> seleccionarPersona());
        tabla.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tablaVehiculos.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);


        cargarPersonas();

        // Agregar todo al diseño
        VBox layout = new VBox(10,
                lblNombre, txtNombre,
                lblDireccion, txtDireccion,
                lblTelefonos, txtTelefonos,
                btnGuardar, btnActualizar, btnEliminar,
                tabla,
                comboBoxVehiculos,
                btnAgregarVehiculo,
                btnEliminarVehiculo,// Agregamos el botón
                tablaVehiculos      // Agregamos la tabla de vehículos
        );
        layout.setPadding(new Insets(10,10,10,10));
        Scene scene = new Scene(layout, 600, 600);
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
            btnAgregarVehiculo.setDisable(false);  // Habilitar botón de agregar vehículo

            cargarVehiculos();  // Cargar vehículos de la persona seleccionada
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

    private void configurarTablaVehiculos() {
        tablaVehiculos = new TableView<>();

        TableColumn<Vehiculo, String> colTipo = new TableColumn<>("Tipo");
        TableColumn<Vehiculo, String> colMarca = new TableColumn<>("Marca");
        TableColumn<Vehiculo, String> colModelo = new TableColumn<>("Modelo");
        TableColumn<Vehiculo, String> colAño = new TableColumn<>("Año");

        colTipo.setCellValueFactory(v -> new SimpleStringProperty(v.getValue().getTipo()));
        colMarca.setCellValueFactory(v -> new SimpleStringProperty(v.getValue().getMarca()));
        colModelo.setCellValueFactory(v -> new SimpleStringProperty(v.getValue().getModelo()));
        colAño.setCellValueFactory(v -> new SimpleStringProperty(String.valueOf(v.getValue().getAño())));

        tablaVehiculos.getColumns().addAll(colTipo);
    }


    private void agregarVehiculo(String tipo) {
        if (personaSeleccionada != null) {
            VehiculoDAO.insertarVehiculo(personaSeleccionada.getId(), tipo, "Chevrolet", "Sonic", 2013);
            cargarVehiculos(); // Refrescar la tabla de vehículos
        }
    }

    private void cargarVehiculos() {
        if (personaSeleccionada != null) {
            List<Vehiculo> vehiculos = VehiculoDAO.obtenerVehiculosPorPersona(personaSeleccionada.getId());
            ObservableList<Vehiculo> datosVehiculos = FXCollections.observableArrayList(vehiculos);
            tablaVehiculos.setItems(datosVehiculos);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
