package Presentation;

import Metier.IMetier;
import Metier.IMetierImpl;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;


public class LoginController implements Initializable {

    @FXML
    private JFXTextField login_field;

    @FXML
    private JFXPasswordField password_field;



    public void connection(ActionEvent event) {
        String log = login_field.getText();
        String password = password_field.getText();
        IMetier metier = new IMetierImpl();
        metier.checkInfoUser(log,password);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {


    }
}
