package mx.uv.fei.logic;

public class Professor {
    private int professorId;
    private String professorDegree;
    private String professorName;
    private String professorLastName;
    private String professorFullName;
    private String professorEmail;
    private int userId;

    public Professor() {}
    
    public int getProfessorId() {
        return professorId;
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

    public int getUserId() {
        return userId;
    }

    public void setProfessorId(int professorId) {
        this.professorId = professorId;
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

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
