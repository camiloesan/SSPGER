package mx.uv.fei.logic;

public class TransferEvidence {
    private static int evidenceId;
    private static String evidenceName;

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
}
