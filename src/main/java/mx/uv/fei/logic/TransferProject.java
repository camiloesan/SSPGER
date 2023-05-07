package mx.uv.fei.logic;

public class TransferProject {
    private static String receptionWorkName;
    private static int projectID;
    
    public static void setReceptionWorkName(String receptionWorkName) {
        TransferProject.receptionWorkName = receptionWorkName;
    }
    public static String getReceptionWorkName() {
        return receptionWorkName;
    }

    public static int getProjectID() {
        return projectID;
    }

    public static void setProjectID(int projectID) {
        TransferProject.projectID = projectID;
    }
}
