package mx.uv.fei.logic;

public enum AlertStatus {
    SUCCESS("100"),
    WARNING("200"),
    ERROR("300"),
    FATAL("400");
    
    private final String code;
    
    private AlertStatus(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return code;
    }
}
