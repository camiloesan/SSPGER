package mx.uv.fei.logic;

public class Professor {
    private int professorId;
    private String professorName;
    private String professorFirsLastName;
    private String professorSecondLastName;
    private String professorEmail;
    private int userId;

    public Professor() {}

    public Professor(String professorName, String professorFirsLastName, String professorSecondLastName, String professorEmail, int userId) {
        this.professorName = professorName;
        this.professorFirsLastName = professorFirsLastName;
        this.professorSecondLastName = professorSecondLastName;
        this.professorEmail = professorEmail;
        this.userId = userId;
    }

    public int getProfessorId() {
        return professorId;
    }

    public String getProfessorName() {
        return professorName;
    }

    public String getProfessorFirsLastName() {
        return professorFirsLastName;
    }

    public String getProfessorSecondLastName() {
        return professorSecondLastName;
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

    public void setProfessorName(String professorName) {
        this.professorName = professorName;
    }

    public void setProfessorFirsLastName(String professorFirsLastName) {
        this.professorFirsLastName = professorFirsLastName;
    }

    public void setProfessorSecondLastName(String professorSecondLastName) {
        this.professorSecondLastName = professorSecondLastName;
    }

    public void setProfessorEmail(String professorEmail) {
        this.professorEmail = professorEmail;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
