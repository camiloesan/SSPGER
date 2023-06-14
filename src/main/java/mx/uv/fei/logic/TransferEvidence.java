package mx.uv.fei.logic;

public class TransferEvidence {
    private static int evidenceId;
    private static String evidenceName;
    private static String studentID;

    public static void setEvidenceId(int evidenceId) {
        TransferEvidence.evidenceId = evidenceId;
    }

    public static int getEvidenceId() {
        return evidenceId;
    }

    public static String getEvidenceName() {
        return evidenceName;
    }

    public static void setEvidenceName(String evidenceName) {
        TransferEvidence.evidenceName = evidenceName;
    }

    public static String getStudentID() {
        return studentID;
    }

    public static void setStudentID(String studentID) {
        TransferEvidence.studentID = studentID;
    }
}
