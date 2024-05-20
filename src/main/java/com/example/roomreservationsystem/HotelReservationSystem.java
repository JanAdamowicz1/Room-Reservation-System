package com.example.roomreservationsystem;

import com.example.roomreservationsystem.Room.Room;
import com.example.roomreservationsystem.State.Booked;
import com.example.roomreservationsystem.State.Free;
import com.example.roomreservationsystem.Memento.*;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;


public class HotelReservationSystem extends Application {
    private RoomInitiator roomInitiator;
    private List<Reservation> reservations;
    private ListView<String> reservationListView;
    private DatePicker startDatePicker;
    private DatePicker endDatePicker;
    private GridPane roomGridPane;
    private ValidationUtility validationUtility;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Hotel Reservation System");

        this.roomInitiator = new RoomInitiator();
        roomInitiator.initRooms();
        this.reservations = new ArrayList<>();
        this.startDatePicker = new DatePicker();
        this.endDatePicker = new DatePicker();
        this.validationUtility = ValidationUtility.getInstance();

        VBox root = new VBox();
        root.getChildren().addAll(createMainPane());
        Scene scene = new Scene(root, 1450, 700);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private HBox createMainPane() {
        HBox mainPane = new HBox(20);
        mainPane.setAlignment(Pos.TOP_LEFT);

        VBox leftPanel = new VBox(10);
        leftPanel.setAlignment(Pos.TOP_LEFT);
        leftPanel.getChildren().addAll(createFilterOptions());

        // Tworzenie GridPane i ustawienie właściwości hgrow
        GridPane roomGridPane = createGridPane();
        HBox.setHgrow(roomGridPane, Priority.ALWAYS);

        mainPane.getChildren().addAll(leftPanel, roomGridPane, createReservationListView());

        return mainPane;
    }

    private GridPane createGridPane() {
        if (roomGridPane == null) {
            roomGridPane = new GridPane();
            roomGridPane.setAlignment(Pos.TOP_LEFT);
            roomGridPane.setHgap(10);
            roomGridPane.setVgap(10);
            // Ustawienie minimalnej szerokości GridPane
            roomGridPane.setMinWidth(Region.USE_PREF_SIZE);
        }
        updateRoomGridPane();
        return roomGridPane;
    }

    private VBox createFilterOptions() {
        VBox filterOptions = new VBox(10);
        CheckBox standardCheckBox = new CheckBox("Standard");
        CheckBox luxuryCheckBox = new CheckBox("Luxury");
        CheckBox twoPersonsCheckBox = new CheckBox("2 person");
        CheckBox threePersonsCheckBox = new CheckBox("3 person");
        CheckBox fourPersonsCheckBox = new CheckBox("4 person");
        Button applyFilterButton = new Button("Apply Filter");
        applyFilterButton.setOnAction(e -> applyRoomFilter(
                standardCheckBox.isSelected(), luxuryCheckBox.isSelected(),
                twoPersonsCheckBox.isSelected(), threePersonsCheckBox.isSelected(),
                fourPersonsCheckBox.isSelected()));
        filterOptions.getChildren().addAll(standardCheckBox, luxuryCheckBox, twoPersonsCheckBox, threePersonsCheckBox, fourPersonsCheckBox, applyFilterButton);

        return filterOptions;
    }

    private HBox createSaveLoadButtons() {
        Button saveButton = new Button("Save Reservations");
        saveButton.setOnAction(e -> saveReservationsToFile("reservations.dat"));
        Button loadButton = new Button("Load Reservations");
        loadButton.setOnAction(e -> loadReservationsFromFile("reservations.dat"));
        HBox buttonsBox = new HBox(10, saveButton, loadButton);
        buttonsBox.setAlignment(Pos.CENTER);

        return buttonsBox;
    }

    private void applyRoomFilter(boolean standard, boolean luxury, boolean twoPersons, boolean threePersons, boolean fourPersons) {
        roomGridPane.getChildren().clear();
        int col = 0;
        int row = 0;

        boolean typeFilterActive = standard || luxury;
        boolean capacityFilterActive = twoPersons || threePersons || fourPersons;

        for (Room room : roomInitiator.getRooms()) {
            boolean matchesType = (!typeFilterActive) || (standard && room.getType().equals("Standard Room")) || (luxury && room.getType().equals("Luxury Room"));
            boolean matchesCapacity = (!capacityFilterActive) || (twoPersons && room.getCapacity() == 2) || (threePersons && room.getCapacity() == 3) || (fourPersons && room.getCapacity() == 4);

            if (matchesType && matchesCapacity) {
                VBox roomRectangle = createRoomRectangle(room);
                Button reserveButton = createReserveButton(room);

                GridPane.setMargin(roomRectangle, new Insets(10, 10, 0, 10));
                GridPane.setMargin(reserveButton, new Insets(0, 10, 10, 10));

                roomGridPane.add(roomRectangle, col, row);
                roomGridPane.add(reserveButton, col, row + 1);

                col++;
                if (col == 5) {
                    col = 0;
                    row += 2;
                }
            }
        }
    }

    private void updateRoomGridPane() {
        roomGridPane.getChildren().clear();
        int col = 0;
        int row = 0;

        for (Room room : roomInitiator.getRooms()) {
            VBox roomRectangle = createRoomRectangle(room);
            Button reserveButton = createReserveButton(room);

            GridPane.setMargin(roomRectangle, new Insets(10, 10, 0, 10));
            GridPane.setMargin(reserveButton, new Insets(0, 10, 10, 10));

            roomGridPane.add(roomRectangle, col, row);
            roomGridPane.add(reserveButton, col, row + 1);

            col++;
            if (col == 5) {
                col = 0;
                row += 2;
            }
        }
    }

    private VBox createRoomRectangle(Room room) {
        Rectangle rectangle = new Rectangle(100, 50, room.getState().getColor());
        room.setRectangle(rectangle);
        rectangle.setOnMouseClicked(event -> {
            showRoomReservations(room);
        });

        Label typeLabel = new Label(room.getType() + " number " + room.getNumber());
        Label capacityLabel = new Label(room.getCapacity() + " person");

        VBox vbox = new VBox(rectangle, typeLabel, capacityLabel);
        vbox.setSpacing(5);

        return vbox;
    }

    private Button createReserveButton(Room room) {
        Button reserveButton = new Button("Reserve");
        reserveButton.setOnAction(event -> {
            LocalDate startDate = startDatePicker.getValue();
            LocalDate endDate = endDatePicker.getValue();

            if (startDate != null && endDate != null) {
                if (room.getState().checkDateValidity(startDate, endDate)) {
                    if (room.checkDatesAvailability(startDate, endDate)) {
                        room.bookRoom(startDate, endDate);
                        Reservation reservation = new Reservation(room, startDate, endDate, null, null);
                        showReservationDialog(room, startDate, endDate, reservation);
                        if(reservation.getCustomerName() != null && reservation.getPhoneNumber() != null) {
                            reservations.add(reservation);
                            updateReservationListView();
                            room.getRectangle().setFill(room.getState().getColor());
                            showAlert(Alert.AlertType.INFORMATION, "Reservation Successful", "Room " + room.getNumber() + " has been reserved.");
                        }
                        else {
                            room.freeRoom(startDate, endDate);
                            showAlert(Alert.AlertType.WARNING, "Reservation Cancelled", "Room " + room.getNumber() + " reservation has ben cancelled.");
                        }
                    } else {
                        showAlert(Alert.AlertType.WARNING, "Reservation Error", "Room " + room.getNumber() + " is not available in the selected date range.");
                    }
                } else {
                    showAlert(Alert.AlertType.WARNING, "Invalid Dates", "Selected dates are not valid.");
                }
            } else {
                showAlert(Alert.AlertType.WARNING, "Missing Dates", "Please select both start and end dates.");
            }
        });
        return reserveButton;
    }

    private void showReservationDialog(Room room, LocalDate startDate, LocalDate endDate, Reservation reservation) {
        // Utworzenie okna dialogowego
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Reservation Details");
        dialog.setHeaderText("Enter customer details for Room " + room.getNumber() + "\n" +
                "From: " + startDate.toString() + " To: " + endDate.toString());

        // Dodanie przycisków
        ButtonType reserveButtonType = new ButtonType("Reserve", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(reserveButtonType, ButtonType.CANCEL);

        // Utworzenie formularza
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField nameField = new TextField();
        nameField.setPromptText("Name");
        TextField phoneField = new TextField();
        phoneField.setPromptText("Phone");

        grid.add(new Label("Name:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Phone:"), 0, 1);
        grid.add(phoneField, 1, 1);

        dialog.getDialogPane().setContent(grid);

        // Ustawienie akcji dla przycisku 'Reserve'
        final Button btReserve = (Button) dialog.getDialogPane().lookupButton(reserveButtonType);
        btReserve.addEventFilter(ActionEvent.ACTION, event -> {
            String customerName = nameField.getText();
            String phoneNumber = phoneField.getText();

            boolean isNameValid = validationUtility.isNameValid(customerName);
            boolean isPhoneNumberValid = validationUtility.isPhoneNumberValid(phoneNumber);

            if (customerName.isEmpty() || phoneNumber.isEmpty() || !isPhoneNumberValid || !isNameValid) {
                showAlert(Alert.AlertType.WARNING, "Incorrect Information", "Name or phone number is incorrect.");
                event.consume(); // Zatrzymaj zamknięcie okna
            } else {
                reservation.setCustomerName(customerName);
                reservation.setPhoneNumber(phoneNumber);
            }
        });

        dialog.showAndWait();
    }

    private HBox createDateCheckContainer() {
        Button applyButton = new Button("Apply");
        applyButton.setOnAction(event -> {
            LocalDate startDate = startDatePicker.getValue();
            LocalDate endDate = endDatePicker.getValue();
            if (startDate != null && endDate != null) {
                for (Room room : roomInitiator.getRooms()) {
                    for (LocalDate date : room.getOccupiedDates()){
                        System.out.println("data " + date + " zajeta dla " + room.getNumber());
                    }
                    if (room.checkDatesAvailability(startDate, endDate)) {
                        room.changeState(new Free(room));
                        room.getRectangle().setFill(room.getState().getColor());
                    } else {
                        room.changeState(new Booked(room));
                        room.getRectangle().setFill(room.getState().getColor());
                    }
                }
            }
            updateReservationListView();
        });

        HBox hbox = new HBox(startDatePicker, endDatePicker, applyButton);
        hbox.setSpacing(10);
        hbox.setAlignment(Pos.CENTER);

        return hbox;
    }

    private VBox createReservationListView() {
        VBox reservationListContainer = new VBox();
        reservationListContainer.setSpacing(10);

        TextField searchField = new TextField();
        searchField.setPromptText("Search by name or phone");

        Button searchButton = new Button("Search");
        searchButton.setOnAction(event -> filterReservationList(searchField.getText()));

        reservationListView = new ListView<>();
        reservationListView.setPrefWidth(500);

        Button cancelReservationButton = new Button("Cancel Reservation");
        cancelReservationButton.setOnAction(event -> cancelSelectedReservation());

        HBox searchBox = new HBox(searchField, searchButton);
        searchBox.setSpacing(10);

        HBox dateCheckContainer = createDateCheckContainer();

        HBox saveLoadButtons = createSaveLoadButtons();

        reservationListContainer.getChildren().addAll(searchBox, new Label("Room Reservations"), reservationListView, cancelReservationButton, dateCheckContainer, saveLoadButtons);

        return reservationListContainer;
    }

    private void filterReservationList(String searchText) {
        reservationListView.getItems().clear();
        for (Reservation reservation : reservations) {
            if (reservation.getCustomerName().toLowerCase().contains(searchText.toLowerCase())
                    || reservation.getPhoneNumber().contains(searchText)) {
                String displayText = "Room " + reservation.getRoom().getNumber() +
                        " | " + reservation.getStartDate() +
                        " to " + reservation.getEndDate() +
                        " | name: " + reservation.getCustomerName() +
                        ", phone: " + reservation.getPhoneNumber();
                reservationListView.getItems().add(displayText);
            }
        }
    }

    private void cancelSelectedReservation() {
        int selectedIndex = reservationListView.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            Reservation selectedReservation = reservations.get(selectedIndex);
            Room room = selectedReservation.getRoom();

            room.freeRoom(selectedReservation.getStartDate(), selectedReservation.getEndDate()); // Usuwa daty z listy zajętych dat
            reservations.remove(selectedIndex);

            if (room.getOccupiedDates().isEmpty()) {
                room.changeState(new Free(room)); // Zmienia stan pokoju na wolny, jeśli nie ma zajętych dat
            }
            updateReservationListView(); // Aktualizuje listę rezerwacji

            showAlert(Alert.AlertType.INFORMATION, "Reservation Canceled", "Reservation canceled successfully.");
        } else {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a reservation to cancel.");
        }
    }

    private void showRoomReservations(Room room) {
        StringBuilder reservationInfo = new StringBuilder();
        for (Reservation reservation : reservations) {
            if (reservation.getRoom().getNumber() == room.getNumber()) {
                reservationInfo.append("From ")
                        .append(reservation.getStartDate())
                        .append(" to ")
                        .append(reservation.getEndDate())
                        .append(" | Name: ")
                        .append(reservation.getCustomerName())
                        .append(", Phone: ")
                        .append(reservation.getPhoneNumber())
                        .append("\n"); // Nowa linia dla każdej rezerwacji
            }
        }

        showScrollableAlert("Room Reservations", "Reservations for Room " + room.getNumber(), reservationInfo.toString());
    }

    private void showScrollableAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);

        TextArea textArea = new TextArea(content);
        textArea.setEditable(false);
        textArea.setWrapText(true);
        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);
        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.ALWAYS);

        GridPane contentPane = new GridPane();
        contentPane.setMaxWidth(Double.MAX_VALUE);
        contentPane.add(textArea, 0, 0);

        alert.getDialogPane().setContent(contentPane);
        alert.showAndWait();
    }

    private void updateReservationListView() {
        reservationListView.getItems().clear();
        for (Reservation reservation : reservations) {
            String displayText = "Room " + reservation.getRoom().getNumber() +
                    " | " + reservation.getStartDate() +
                    " to " + reservation.getEndDate() +
                    " | name: " + reservation.getCustomerName() +
                    ", phone: " + reservation.getPhoneNumber();
            reservationListView.getItems().add(displayText);
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public void saveReservationsToFile(String filePath) {
        Memento memento = new Memento(reservations);
        Caretaker caretaker = new Caretaker();
        try {
            caretaker.saveReservations(filePath, memento);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadReservationsFromFile(String filePath) {
        Caretaker caretaker = new Caretaker();
        try {
            Memento memento = caretaker.loadReservations(filePath);
            List<Reservation> collectedReservations = memento.getReservations();

            for(Reservation reservation : this.reservations){
                reservation.getRoom().freeRoom(reservation.getStartDate(), reservation.getEndDate());
            }
            this.reservations = new ArrayList<>();

            //Aktualizacja stanu pokoi
            for (Reservation reservation : collectedReservations) {
                Room room = findRoomByNumber(reservation.getRoom().getNumber());
                room.bookRoom(reservation.getStartDate(), reservation.getEndDate());
                Reservation r = new Reservation(room, reservation.getStartDate(), reservation.getEndDate(), reservation.getCustomerName(), reservation.getPhoneNumber());
                this.reservations.add(r);
            }
            updateReservationListView(); // Aktualizacja listy rezerwacji
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private Room findRoomByNumber(int roomNumber) {
        for (Room room : roomInitiator.getRooms()) {
            if (room.getNumber() == roomNumber) {
                return room;
            }
        }
        return null;
    }
}