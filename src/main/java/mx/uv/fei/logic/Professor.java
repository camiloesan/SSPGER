package mx.uv.fei.logic;

public class Professor {
    private String professorDegree;
    private String professorName;
    private String professorLastName;
    private String professorFullName;
    private String professorEmail;
    private String username;

    public Professor() {}

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
    
    public String getProfessorEmail() {
        return professorEmail;
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
    
    public void setProfessorEmail(String professorEmail) {
        this.professorEmail = professorEmail;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
