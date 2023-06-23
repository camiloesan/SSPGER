package mx.uv.fei.logic;

import java.util.Objects;

public class Advancement {

    private int advancementID;
    private String advancementName;
    private String advancementDescription;
    private String advancementStartDate;
    private String advancementDeadline;
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
    
    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public int getAdvancementID() {
        return advancementID;
    }

    public void setAdvancementID(int advancementID) {
        this.advancementID = advancementID;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()){
            return false;
        }
        Advancement other = (Advancement) object;
        return advancementID == other.advancementID
                && Objects.equals(advancementName, other.advancementName)
                && Objects.equals(advancementDescription, other.advancementDescription)
                && Objects.equals(advancementStartDate, other.advancementStartDate)
                && Objects.equals(advancementDeadline, other.advancementDeadline)
                && projectId == other.projectId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(advancementID, advancementName, advancementDescription, advancementStartDate,
                advancementDeadline, projectId);
    }

}
