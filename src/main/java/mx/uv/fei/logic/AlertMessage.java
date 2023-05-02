package mx.uv.fei.logic;

public class AlertMessage {
    private String content;
    private AlertStatus alertStatus = AlertStatus.SUCCESS;
    
    public AlertMessage( String content, AlertStatus alertStatus) {
        this.content = content;
        this.alertStatus = alertStatus;
    }
    
    public String getContent() {
        return content;
    }
    
    public void setContent(String content) {
        this.content = content;
    }
    
    public AlertStatus getAlertStatus() {
        return alertStatus;
    }
    
    public void setAlertStatus(AlertStatus alertStatus) {
        this.alertStatus = alertStatus;
    }
}
