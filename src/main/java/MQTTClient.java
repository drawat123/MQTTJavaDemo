import org.eclipse.paho.client.mqttv3.*;

import java.util.concurrent.CountDownLatch;

public class MQTTClient {
    MqttClient client = null;

    public void connect(String broker, String clientId, String userName) throws MqttException {
        client = new MqttClient(broker, clientId);

        MqttConnectOptions options = new MqttConnectOptions();

        options.setUserName(userName);
        options.setCleanSession(true);
        /*options.setServerURIs(new String[]{broker});
        String pass = "12345678";
        options.setPassword(pass.toCharArray());*/

        client.connect(options);

        if (client.isConnected()) {
            int pubQos = 1;
            client.setCallback(new MqttCallback() {
                public void messageArrived(String topic, MqttMessage message) {
                    System.out.println("topic: " + topic);
                    System.out.println("qos: " + message.getQos());
                    System.out.println("message content: " + new String(message.getPayload()));
                }

                public void connectionLost(Throwable cause) {
                    System.out.println("connectionLost: " + cause.getMessage());
                }

                public void deliveryComplete(IMqttDeliveryToken token) {
                    System.out.println("Java Client message deliveryComplete: " + token.isComplete());
                }
            });
        }
    }

    public void subscribe(String subscribeTopic) throws MqttException {
        int subQos = 1;
        client.subscribe(subscribeTopic, subQos);
    }

    public void publish(String publishTopic, String payLoad) throws MqttException {
        int pubQos = 1;
        MqttMessage pubMessage = new MqttMessage(payLoad.getBytes());
        pubMessage.setQos(pubQos);
        client.publish(publishTopic, pubMessage);
    }

    public void disconnect() throws MqttException {
        client.disconnect();
        client.close();
    }
}
