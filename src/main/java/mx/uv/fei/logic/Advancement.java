package mx.uv.fei.logic;

public class Advancement {
    private int advancementId;
    private String advancementName;
    private String advancementDescription;
    private String advancementStartDate;
    private String advancementEndDate;
    private int professorId;
    private int projectId;

    public int getAdvancementId() {
        return advancementId;
    }

    public void setAdvancementId(int advancementId) {
        this.advancementId = advancementId;
    }

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

    public String getAdvancementEndDate() {
        return advancementEndDate;
    }

    public void setAdvancementEndDate(String advancementEndDate) {
        this.advancementEndDate = advancementEndDate;
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
