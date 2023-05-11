package mx.uv.fei.logic;

public class TransferAdvancement {
    private static int advancementID;
    private static String advancementName;
    public static void setAdvancementName(String advancementName) {
        TransferAdvancement.advancementName = advancementName;
    }
    
    public static String getAdvancementName() {
        return advancementName;
    }

    public static int getAdvancementID() {
        return advancementID;
    }

    public static void setAdvancementID(int advancementID) {
        TransferAdvancement.advancementID = advancementID;
    }
}
