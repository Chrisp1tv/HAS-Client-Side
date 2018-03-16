package chv.has.utils;

import chv.has.HAS;
import chv.has.exceptions.DisconnectedException;
import chv.has.exceptions.RegistrationFailedException;
import chv.has.model.Message;
import chv.has.model.RabbitMQConfiguration;
import chv.has.model.communications.MessageStatus;
import chv.has.model.communications.Registration;
import chv.has.model.communications.RegistrationResponse;
import chv.has.model.interfaces.MessageInterface;
import chv.has.model.interfaces.OnMessageInterface;
import com.google.gson.Gson;
import com.rabbitmq.client.*;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeoutException;

/**
 * @author Christopher Anciaux
 */
public class RabbitMQManager {
    public static final String MESSAGE_STATUS_RECEIVED = "received";

    public static final String MESSAGE_STATUS_READ = "seen";

    protected static final String REGISTRATION_EXCHANGE_NAME = "has_registration_exchange";

    protected static final String CAMPAIGNS_STATUS_EXCHANGE = "has_campaigns_status_exchange";

    protected RabbitMQConfiguration configuration;

    protected Gson gson;

    protected Connection RabbitMQConnection;

    protected Channel RabbitMQChannel;

    protected BooleanProperty connected;

    public RabbitMQManager(RabbitMQConfiguration configuration) {
        this.configuration = configuration;
        this.gson = new Gson();
        this.connected = new SimpleBooleanProperty();
        this.initialize();
    }

    public RabbitMQConfiguration getConfiguration() {
        return this.configuration;
    }

    public boolean isConnected() {
        return this.connected.get();
    }

    public BooleanProperty connectedProperty() {
        return this.connected;
    }

    protected void setConnected(boolean connected) {
        this.connected.set(connected);
    }

    protected void register() throws Exception {
        String replyQueueName = this.RabbitMQChannel.queueDeclare().getQueue();
        String correlationID = UUID.randomUUID().toString();
        BlockingQueue<String> responseContainer = new ArrayBlockingQueue<>(1);

        AMQP.BasicProperties properties = new AMQP.BasicProperties.Builder().correlationId(correlationID).replyTo(replyQueueName).build();

        this.RabbitMQChannel.basicPublish(RabbitMQManager.REGISTRATION_EXCHANGE_NAME, "", properties, this.getRegistrationJSON(this.configuration.getIdentificationName()).getBytes());
        this.RabbitMQChannel.basicConsume(replyQueueName, true, new DefaultConsumer(this.RabbitMQChannel) {
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                responseContainer.add(new String(body, StandardCharsets.UTF_8));
            }
        });

        RegistrationResponse registrationResponse = this.getRegistrationResponseFromJSON(responseContainer.take());

        if (null == registrationResponse.getQueueName()) {
            throw new RegistrationFailedException();
        }

        this.configuration.setSubscribedQueueName(registrationResponse.getQueueName());
        this.configuration.setId(registrationResponse.getId());
        this.configuration.setRegistered(true);
    }

    public void disconnect() {
        this.disconnect(false);
    }

    protected void disconnect(boolean changeConnectedStatus) {
        if (!this.isConnected()) {
            return;
        }

        try {
            this.RabbitMQChannel.close();
            this.RabbitMQConnection.close();
        } catch (IOException | TimeoutException exception) {
            Logger.logException(exception);
        }

        if (changeConnectedStatus) {
            this.setConnected(false);
        }
    }

    public void onMessage(OnMessageInterface onMessage) throws DisconnectedException {
        if (!this.isConnected()) {
            throw new DisconnectedException();
        }

        try {
            this.RabbitMQChannel.basicConsume(this.configuration.getSubscribedQueueName(), true, new DefaultConsumer(this.RabbitMQChannel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    try {
                        MessageInterface message = RabbitMQManager.this.getMessageFromJSON(new String(body, StandardCharsets.UTF_8));

                        onMessage.onMessage(message);
                        RabbitMQManager.this.sendMessageStatus(RabbitMQManager.MESSAGE_STATUS_RECEIVED, message);
                    } catch (Exception e) {
                        Logger.logException(e);
                    }
                }
            });
        } catch (IOException e) {
            throw new DisconnectedException();
        }
    }

    public void sendMessageStatus(String status, MessageInterface message) {
        try {
            this.RabbitMQChannel.basicPublish(RabbitMQManager.CAMPAIGNS_STATUS_EXCHANGE, "", null, this.getMessageStatusJSON(status, message).getBytes());
        } catch (Exception exception) {
            Logger.logException(exception);
        }
    }

    protected void initialize() {
        try {
            if (null != this.RabbitMQConnection && null != this.RabbitMQChannel) {
                this.disconnect(false);
            }

            InputStream inputStream = HAS.class.getResourceAsStream("/rabbitmq_config/rabbitmq_config.properties");
            Properties p = new Properties();
            p.load(inputStream);

            ConnectionFactory connectionFactory = new ConnectionFactory();
            connectionFactory.setUri(p.getProperty("rabbitmq_uri"));

            this.RabbitMQConnection = connectionFactory.newConnection();
            this.RabbitMQChannel = this.RabbitMQConnection.createChannel();

            if (!this.configuration.isRegistered()) {
                this.register();
            }

            RabbitMQConfigurationManager.saveRabbitMQConfiguration(this.configuration);
            this.setConnected(true);
        } catch (Exception exception) {
            Logger.logException(exception);
            this.setConnected(false);
        }
    }

    protected String getRegistrationJSON(String identificationName) {
        return this.gson.toJson(new Registration(identificationName));
    }

    protected String getMessageStatusJSON(String status, MessageInterface message) {
        return this.gson.toJson(new MessageStatus(this.configuration.getId(), message.getId(), status));
    }

    protected MessageInterface getMessageFromJSON(String JSON) {
        return this.gson.fromJson(JSON, Message.class);
    }

    protected RegistrationResponse getRegistrationResponseFromJSON(String JSON) {
        return this.gson.fromJson(JSON, RegistrationResponse.class);
    }
}