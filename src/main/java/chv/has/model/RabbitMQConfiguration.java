package chv.has.model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * @author Christopher Anciaux
 */
public class RabbitMQConfiguration implements Serializable {
    private static final long serialVersionUID = -8121657064880120033L;

    protected int id;

    protected String identificationName;

    protected BooleanProperty registered;

    protected String subscribedQueueName;

    public RabbitMQConfiguration(String identificationName) {
        this.initialize();
        this.setIdentificationName(identificationName);
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIdentificationName() {
        return this.identificationName;
    }

    public void setIdentificationName(String identificationName) {
        this.identificationName = identificationName;
    }

    public boolean isRegistered() {
        return this.registered.get();
    }

    public void setRegistered(boolean registered) {
        this.registered.set(registered);
    }

    public BooleanProperty registeredProperty() {
        return this.registered;
    }

    public String getSubscribedQueueName() {
        return this.subscribedQueueName;
    }

    public void setSubscribedQueueName(String subscribedQueueName) {
        this.subscribedQueueName = subscribedQueueName;
    }

    protected void initialize() {
        this.registered = new SimpleBooleanProperty();
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.writeInt(this.getId());
        objectOutputStream.writeObject(this.getIdentificationName());
        objectOutputStream.writeBoolean(this.isRegistered());
        objectOutputStream.writeObject(this.getSubscribedQueueName());
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        this.initialize();
        this.setId(objectInputStream.readInt());
        this.setIdentificationName((String) objectInputStream.readObject());
        this.setRegistered(objectInputStream.readBoolean());
        this.setSubscribedQueueName((String) objectInputStream.readObject());
    }
}
