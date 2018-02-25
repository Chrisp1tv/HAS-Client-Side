package chv.has.model.communications;

/**
 * @author Christopher Anciaux
 */
public class RegistrationResponse {
    private int id;

    private String queueName;

    public RegistrationResponse(int id, String queueName) {
        this.id = id;
        this.queueName = queueName;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQueueName() {
        return this.queueName;
    }

    public void setQueueName(String queueName) {
        this.queueName = queueName;
    }
}
