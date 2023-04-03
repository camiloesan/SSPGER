package mx.uv.fei.logic;

public class ProjectEvidence {
    private int projectEvidenceId;
    private String projectEvidenceTitle;
    private String projectEvidenceStatus;
    private int projectEvidenceGrade;
    private String projectEvidenceDescription;
    private int professorId;
    private int progressId;
    private int projectId;
    private String studentMatricula;

    public ProjectEvidence(int projectEvidenceId, String projectEvidenceTitle, String projectEvidenceStatus, int projectEvidenceGrade, String projectEvidenceDescription, int professorId, int progressId, int projectId, String studentMatricula) {
        this.projectEvidenceId = projectEvidenceId;
        this.projectEvidenceTitle = projectEvidenceTitle;
        this.projectEvidenceStatus = projectEvidenceStatus;
        this.projectEvidenceGrade = projectEvidenceGrade;
        this.projectEvidenceDescription = projectEvidenceDescription;
        this.professorId = professorId;
        this.progressId = progressId;
        this.projectId = projectId;
        this.studentMatricula = studentMatricula;
    }

    public int getProjectEvidenceId() {
        return projectEvidenceId;
    }

    public void setProjectEvidenceId(int projectEvidenceId) {
        this.projectEvidenceId = projectEvidenceId;
    }

    public String getProjectEvidenceTitle() {
        return projectEvidenceTitle;
    }

    public void setProjectEvidenceTitle(String projectEvidenceTitle) {
        this.projectEvidenceTitle = projectEvidenceTitle;
    }

    public String getProjectEvidenceStatus() {
        return projectEvidenceStatus;
    }

    public void setProjectEvidenceStatus(String projectEvidenceStatus) {
        this.projectEvidenceStatus = projectEvidenceStatus;
    }

    public int getProjectEvidenceGrade() {
        return projectEvidenceGrade;
    }

    public void setProjectEvidenceGrade(int projectEvidenceGrade) {
        this.projectEvidenceGrade = projectEvidenceGrade;
    }

    public String getProjectEvidenceDescription() {
        return projectEvidenceDescription;
    }

    public void setProjectEvidenceDescription(String projectEvidenceDescription) {
        this.projectEvidenceDescription = projectEvidenceDescription;
    }

    public int getProfessorId() {
        return professorId;
    }

    public void setProfessorId(int professorId) {
        this.professorId = professorId;
    }

    public int getProgressId() {
        return progressId;
    }

    public void setProgressId(int progressId) {
        this.progressId = progressId;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public String getStudentMatricula() {
        return studentMatricula;
    }

    public void setStudentMatricula(String studentMatricula) {
        this.studentMatricula = studentMatricula;
    }
}
