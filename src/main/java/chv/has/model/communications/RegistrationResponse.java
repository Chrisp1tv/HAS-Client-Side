package chv.has.model.communications;

/**
 * @author Christopher Anciaux
 */
public class RegistrationResponse {
    private String queueName;

    public RegistrationResponse(String queueName) {
        this.queueName = queueName;
    }

    public String getQueueName() {
        return this.queueName;
    }

    public void setQueueName(String queueName) {
        this.queueName = queueName;
    }
}
