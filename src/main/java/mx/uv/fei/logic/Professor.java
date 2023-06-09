package mx.uv.fei.logic;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Professor {
    private String professorDegree;
    private String professorName;
    private String professorLastName;
    private String professorFullName;
    private String username;
    private String email;

    public Professor() {}

    public boolean isEmailValid(String professorEmail) {
        String regex =
                "^[\\w!#$%&amp;'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&amp;'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(professorEmail);

        return matcher.matches() && professorEmail.contains("uv.mx");
    }

    public boolean isUsernameValid(String username) {
        String regex = "^(\\p{Alnum}+(\\/{1}\\p{Alnum}+)*)+(?!([\\/]{2}))$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(username);

        return matcher.matches();
    }

    public String getUsername() {
        return username;
    }
    
    public String getProfessorDegree() {
        return professorDegree;
    }

    public String getProfessorName() {
        return professorName;
    }

    public String getProfessorLastName() {
        return professorLastName;
    }
    
    public String getProfessorFullName() {
        return professorFullName;
    }

    public void setProfessorDegree(String professorDegree) {
        this.professorDegree = professorDegree;
    }
    
    public void setProfessorName(String professorName) {
        this.professorName = professorName;
    }

    public void setProfessorLastName(String professorLastName) {
        this.professorLastName = professorLastName;
    }
    
    public void setProfessorFullName(String professorFullName) {
        this.professorFullName = professorFullName;
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
}
