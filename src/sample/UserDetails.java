package sample;


import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;


public class UserDetails {
    private final StringProperty studentID;
    private final StringProperty firstName;
    private final StringProperty lastName;
    private final StringProperty password;
    private final StringProperty phoneNumber;
    private final StringProperty email;
    private final BooleanProperty permission;


    String url = "jdbc:mysql://localhost:3306/users";
    String user = "root";
    String serverpassword = "root";


    public UserDetails(String studentID, String password, String firstName, String lastName, String phoneNumber,
                       String email, boolean permission) {
        this.studentID = new SimpleStringProperty(studentID);
        this.password = new SimpleStringProperty(password);
        this.firstName = new SimpleStringProperty(firstName);
        this.lastName = new SimpleStringProperty(lastName);
        this.phoneNumber = new SimpleStringProperty(phoneNumber);
        this.email = new SimpleStringProperty(email);
        this.permission = new SimpleBooleanProperty(permission);

    }


    public String getStudentID() {
        return studentID.get();
    }


    public String getPassword() {
        return password.get();
    }

    public String getFirstName() {
        return firstName.get();
    }

    public String getLastName() {
        return lastName.get();
    }

    public String getPhoneNumber() {
        return phoneNumber.get();
    }

    public String getEmail() {
        return email.get();
    }

    public void setStudentID(String value) {
        studentID.set(value);
    }


    public void setPassword(String value) {
        password.set(value);
    }

    public void setFirstName(String value) {
        firstName.set(value);
    }

    public void setLastName(String value) {
        lastName.set(value);
    }

    public void setPhoneNumber(String value) {
        phoneNumber.set(value);
    }

    public void setEmail(String value) {
        email.set(value);
    }


    public StringProperty studentIDProperty() {
        return studentID;
    }

    public StringProperty passwordProperty() {
        return password;
    }

    public StringProperty firstNameProperty() {
        return firstName;
    }

    public StringProperty lastNameProperty() {
        return lastName;
    }


    public StringProperty phoneNumberProperty() {
        return phoneNumber;
    }

    public StringProperty emailProperty() {
        return email;
    }

    public BooleanProperty permissionProperty() {
        return permission;
    }


}



