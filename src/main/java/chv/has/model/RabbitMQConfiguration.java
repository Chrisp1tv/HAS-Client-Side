package chv.has.model;

import chv.has.utils.RabbitMQManager;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * @author Christopher Anciaux
 */
public class RabbitMQConfiguration implements Serializable {
    protected static final long serialVersionUID = 4172745486348076636L;

    protected StringProperty host;

    protected int id;

    protected String identificationName;

    protected BooleanProperty registered;

    protected String subscribedQueueName;

    public RabbitMQConfiguration(String identificationName) {
        this.initialize();
        this.setIdentificationName(identificationName);
        this.setHost(RabbitMQManager.DEFAULT_SERVER_HOST);
    }

    public String getHost() {
        return this.host.get();
    }

    public void setHost(String host) {
        this.host.set(host);
    }

    public StringProperty hostProperty() {
        return this.host;
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
        this.host = new SimpleStringProperty();
        this.registered = new SimpleBooleanProperty();
    }

    protected void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.writeObject(this.getHost());
        objectOutputStream.writeInt(this.getId());
        objectOutputStream.writeObject(this.getIdentificationName());
        objectOutputStream.writeBoolean(this.isRegistered());
        objectOutputStream.writeObject(this.getSubscribedQueueName());
    }

    protected void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        this.initialize();
        this.setHost((String) objectInputStream.readObject());
        this.setId(objectInputStream.readInt());
        this.setIdentificationName((String) objectInputStream.readObject());
        this.setRegistered(objectInputStream.readBoolean());
        this.setSubscribedQueueName((String) objectInputStream.readObject());
    }
}
