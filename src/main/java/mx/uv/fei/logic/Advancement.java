package mx.uv.fei.logic;

public class Advancement {
    private String advancementName;
    private String advancementDescription;
    private String advancementStartDate;
    private String advancementDeadline;
    private int professorId;
    private int projectId;

    public String getAdvancementName() {
        return advancementName;
    }

    public void setAdvancementName(String advancementName) {
        this.advancementName = advancementName;
    }

    public String getAdvancementDescription() {
        return advancementDescription;
    }

    public void setAdvancementDescription(String advancementDescription) {
        this.advancementDescription = advancementDescription;
    }

    public String getAdvancementStartDate() {
        return advancementStartDate;
    }

    public void setAdvancementStartDate(String advancementStartDate) {
        this.advancementStartDate = advancementStartDate;
    }

    public String getAdvancementDeadline() {
        return advancementDeadline;
    }

    public void setAdvancementDeadline(String advancementDeadline) {
        this.advancementDeadline = advancementDeadline;
    }

    public int getProfessorId() {
        return professorId;
    }

    public void setProfessorId(int professorId) {
        this.professorId = professorId;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }
}
