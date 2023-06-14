package mx.uv.fei.logic;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Student {
    private String studentID;
    private String name;
    private String lastName;
    private String fullName;
    private String username;
    private String email;

    public boolean isEmailValid(String studentEmail) {
        String regex = "^[\\w!#$%&amp;'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&amp;'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(studentEmail);

        return matcher.matches() && studentEmail.contains("estudiantes.uv.mx");
    }

    public boolean isUsernameValid(String username) {
        String regex = "^(\\p{Alnum}+(\\/{1}\\p{Alnum}+)*)+(?!([\\/]{2}))$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(username);

        return matcher.matches();
    }

    public String getStudentID() {
        return studentID;
    }

    public void setStudentID(String studentID) {
        this.studentID = studentID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    public String getFullName() {
        return fullName;
    }
    
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Student)) {
            return false;
        }
        Student student = (Student) obj;
        return Objects.equals(getStudentID(), student.getStudentID())
                && Objects.equals(getFullName(), student.getFullName());
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(getStudentID(),getFullName());
    }
}
