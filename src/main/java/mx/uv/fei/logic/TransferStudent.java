package mx.uv.fei.logic;

public class TransferStudent {
    private static String studentID;
    private static String studentName;
    
    public static String getStudentID() {
        return studentID;
    }
    
    public static void setStudentID(String studentID) {
        TransferStudent.studentID = studentID;
    }
    
    public static String getStudentName() {
        return studentName;
    }
    
    public static void setStudentName(String studentName) {
        TransferStudent.studentName = studentName;
    }
}
