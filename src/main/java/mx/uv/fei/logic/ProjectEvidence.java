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
    private String studentId;

    public ProjectEvidence(String projectEvidenceTitle, String projectEvidenceDescription, int professorId, int progressId, int projectId, String studentId) {
        this.projectEvidenceTitle = projectEvidenceTitle;
        this.projectEvidenceDescription = projectEvidenceDescription;
        this.professorId = professorId;
        this.progressId = progressId;
        this.projectId = projectId;
        this.studentId = studentId;
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

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }
}
