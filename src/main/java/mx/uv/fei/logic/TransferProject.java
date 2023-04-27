package mx.uv.fei.logic;

public class TransferProject {
    private static String receptionWorkName;
    
    public static void setReceptionWorkName(String receptionWorkName) {
        TransferProject.receptionWorkName = receptionWorkName;
    }
    public static String getReceptionWorkName() {
        return receptionWorkName;
    }
}
