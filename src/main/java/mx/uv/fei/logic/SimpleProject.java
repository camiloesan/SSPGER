package mx.uv.fei.logic;


public class SimpleProject {
    private int projectID;
    private String projectName;
    private String professorFullName;

    public SimpleProject() {
    }

    public int getProjectID(){
        return projectID;
    }

    public void setProjectID(int projectID) {
        this.projectID = projectID;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProfessorFullName() {
        return professorFullName;
    }

    public void setProfessorFullName(String professorFullName) {
        this.professorFullName = professorFullName;
    }

}
