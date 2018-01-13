package chv.has.utils;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author Christopher Anciaux
 */
public class RabbitMQManager {
    private static final String DEFAULT_SERVER_HOST = "localhost";

    private Connection RMQConnection;

    private Channel RMQChannel;

    private String host = RabbitMQManager.DEFAULT_SERVER_HOST;

    private boolean connected;

    public RabbitMQManager() {
        this.initialize();
    }

    public RabbitMQManager(String host) {
        this.setHost(host);
    }

    public String getHost() {
        return this.host;
    }

    public void setHost(String host) {
        if (host.equals(this.getHost())) {
            return;
        }

        this.host = host;
        this.initialize();

    }

    public boolean isConnected() {
        return this.connected;
    }

    private void initialize() {
        try {
            if (null != this.RMQConnection && null != this.RMQChannel) {
                this.RMQChannel.close();
                this.RMQConnection.close();
            }

            ConnectionFactory connectionFactory = new ConnectionFactory();
            connectionFactory.setHost(host);

            this.RMQConnection = connectionFactory.newConnection();
            this.RMQChannel = this.RMQConnection.createChannel();

            this.connected = true;
        } catch (IOException | TimeoutException e) {
            this.connected = false;
        }
    }
}