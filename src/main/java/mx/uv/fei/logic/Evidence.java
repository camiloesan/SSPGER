package mx.uv.fei.logic;

import java.util.Objects;

public class Evidence {
    private int evidenceId;
    private String evidenceTitle;
    private String evidenceStatus;
    private int evidenceGrade;
    private String evidenceDescription;
    private String advancementName;
    private int advancementId;
    private String studentName;
    private String studentId;
    private String deliverDate;

    public int getEvidenceId() {
        return evidenceId;
    }

    public void setEvidenceId(int evidenceId) {
        this.evidenceId = evidenceId;
    }

    public String getEvidenceTitle() {
        return evidenceTitle;
    }

    public void setEvidenceTitle(String evidenceTitle) {
        this.evidenceTitle = evidenceTitle;
    }

    public String getEvidenceStatus() {
        return evidenceStatus;
    }

    public void setEvidenceStatus(String evidenceStatus) {
        this.evidenceStatus = evidenceStatus;
    }

    public int getEvidenceGrade() {
        return evidenceGrade;
    }

    public void setEvidenceGrade(int evidenceGrade) {
        this.evidenceGrade = evidenceGrade;
    }

    public String getEvidenceDescription() {
        return evidenceDescription;
    }

    public void setEvidenceDescription(String evidenceDescription) {
        this.evidenceDescription = evidenceDescription;
    }

    public int getAdvancementId() {
        return advancementId;
    }

    public void setAdvancementId(int advancementId) {
        this.advancementId = advancementId;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }
    
    public String getDeliverDate() {
        return deliverDate;
    }
    
    public void setDeliverDate(String deliverDate) {
        this.deliverDate = deliverDate;
    }

    public String getAdvancementName() {
        return advancementName;
    }

    public void setAdvancementName(String advancementName) {
        this.advancementName = advancementName;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        Evidence evidence = (Evidence) object;

        return evidenceId == evidence.evidenceId
                && Objects.equals(evidenceTitle, evidence.evidenceTitle)
                && Objects.equals(evidenceDescription, evidence.evidenceDescription)
                && Objects.equals(evidenceStatus, evidence.evidenceStatus)
                && evidenceGrade == evidence.evidenceGrade
                && advancementId == evidence.advancementId
                && Objects.equals(studentId, evidence.studentId)
                && Objects.equals(deliverDate, evidence.deliverDate)
                && Objects.equals(advancementName, evidence.advancementName)
                && Objects.equals(studentName, evidence.studentName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(evidenceId, evidenceTitle, evidenceDescription, evidenceStatus, evidenceGrade,
                advancementId, studentId, deliverDate, advancementName, studentName);
    }
}
