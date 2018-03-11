package chv.has.utils;

import chv.has.model.RabbitMQConfiguration;

import java.io.*;

/**
 * @author Christopher Anciaux
 */
abstract public class RabbitMQConfigurationManager {
    protected static final File CONFIGURATION_FILE = new File(System.getProperty("user.home") + File.separator + ".has" + File.separator + "hasConfiguration");

    protected static final String DEFAULT_USER_NAME = System.getProperty("user.name");

    public static RabbitMQConfiguration loadRabbitMQConfiguration() {
        if (!RabbitMQConfigurationManager.CONFIGURATION_FILE.exists() || !RabbitMQConfigurationManager.CONFIGURATION_FILE.isFile()) {
            return RabbitMQConfigurationManager.getDefaultRabbitMQConfiguration();
        }

        RabbitMQConfiguration RabbitMQConfiguration;

        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(new BufferedInputStream(new FileInputStream(RabbitMQConfigurationManager.CONFIGURATION_FILE)));

            RabbitMQConfiguration = (RabbitMQConfiguration) objectInputStream.readObject();
            objectInputStream.close();
        } catch (IOException | ClassNotFoundException e) {
            Logger.logException(e);

            return RabbitMQConfigurationManager.getDefaultRabbitMQConfiguration();
        }

        return RabbitMQConfiguration;
    }

    public static void saveRabbitMQConfiguration(RabbitMQConfiguration RabbitMQConfiguration) throws IOException {
        if (!RabbitMQConfigurationManager.CONFIGURATION_FILE.getParentFile().exists()) {
            RabbitMQConfigurationManager.CONFIGURATION_FILE.getParentFile().mkdirs();
        }

        ObjectOutputStream objectOutputStream = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(RabbitMQConfigurationManager.CONFIGURATION_FILE)));

        objectOutputStream.writeObject(RabbitMQConfiguration);
        objectOutputStream.close();
    }

    protected static RabbitMQConfiguration getDefaultRabbitMQConfiguration() {
        return new RabbitMQConfiguration(RabbitMQConfigurationManager.DEFAULT_USER_NAME);
    }
}
