package mx.uv.fei.logic;

public class ProjectRequest {
    private int projectPetitionID;
    private int projectID;
    private String projectName;
    private String studentId;
    private String status;
    private String description;

    public int getProjectPetitionID() {
        return projectPetitionID;
    }

    public void setProjectPetitionID(int projectPetitionID) {
        this.projectPetitionID = projectPetitionID;
    }

    public int getProjectID() {
        return projectID;
    }

    public void setProjectID(int projectID) {
        this.projectID = projectID;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }
}
