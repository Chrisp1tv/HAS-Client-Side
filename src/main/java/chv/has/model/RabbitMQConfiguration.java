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
    private StringProperty host;

    private String identificationName;

    private BooleanProperty registered;

    private String subscribedQueueName;

    public RabbitMQConfiguration(String identificationName) {
        this.initialize();
        this.setIdentificationName(identificationName);
        this.setHost(RabbitMQManager.DEFAULT_SERVER_HOST);
    }

    public String getHost() {
        return host.get();
    }

    public StringProperty hostProperty() {
        return host;
    }

    public void setHost(String host) {
        this.host.set(host);
    }

    public String getIdentificationName() {
        return identificationName;
    }

    public void setIdentificationName(String identificationName) {
        this.identificationName = identificationName;
    }

    public boolean isRegistered() {
        return registered.get();
    }

    public BooleanProperty registeredProperty() {
        return registered;
    }

    public void setRegistered(boolean registered) {
        this.registered.set(registered);
    }

    public String getSubscribedQueueName() {
        return subscribedQueueName;
    }

    public void setSubscribedQueueName(String subscribedQueueName) {
        this.subscribedQueueName = subscribedQueueName;
    }

    private void initialize() {
        this.host = new SimpleStringProperty();
        this.registered = new SimpleBooleanProperty();
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.writeObject(this.getHost());
        objectOutputStream.writeObject(this.getIdentificationName());
        objectOutputStream.writeBoolean(this.isRegistered());
        objectOutputStream.writeObject(this.getSubscribedQueueName());
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        this.initialize();
        this.setHost((String) objectInputStream.readObject());
        this.setIdentificationName((String) objectInputStream.readObject());
        this.setRegistered(objectInputStream.readBoolean());
        this.setSubscribedQueueName((String) objectInputStream.readObject());
    }
}
