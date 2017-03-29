package be.ehb.dt;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class InternshipLoggerGUIController implements Initializable {

    ObservableList hours = FXCollections.observableArrayList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23);

    @FXML
    private TextField tokenInput;

    @FXML
    private TextField workspaceInput;

    @FXML
    private TextField projectInput;

    @FXML
    private DatePicker startDateInput;

    @FXML
    private ChoiceBox<Integer> startTimeInput;

    @FXML
    private DatePicker stopDateInput;

    @FXML
    private ChoiceBox<Integer> stopTimeInput;

    @FXML
    private TextField cookieInput;

    @FXML
    private Label output;

    @FXML
    private void handleSendAction(ActionEvent event) throws IOException {

        String startDateString = startDateInput.getValue() + " " + startTimeInput.getValue();
        String stopDateString = stopDateInput.getValue() + " " + stopTimeInput.getValue();
        output.setText(startDateString + " - " + stopDateString);

        ConnectionController cc = new ConnectionController(tokenInput.getText(), workspaceInput.getText(), projectInput.getText(), startDateString, stopDateString, cookieInput.getText());
        cc.togglToEhb();
    }

    public void initialize(URL location, ResourceBundle resources) {
        startTimeInput.setValue(8);
        stopTimeInput.setValue(18);
        startTimeInput.setItems(hours);
        stopTimeInput.setItems(hours);
    }
}
