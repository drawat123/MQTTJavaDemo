import org.eclipse.paho.client.mqttv3.*;

import java.util.concurrent.CountDownLatch;

public class MQTTJavaDemo {
    public static void main(String[] args) {
        String broker = "tcp://localhost:1883", clientId = "demo_client";
        String subscribeTopic = "v1/devices/me/attributes", publishTopic = "v1/devices/me/telemetry";
        int subQos = 1, pubQos = 1;

        try {
            MqttClient client = new MqttClient(broker, clientId);
            MqttConnectOptions options = new MqttConnectOptions();

            options.setUserName("46C8CHXtCgYU9bdI6XSB");
            options.setCleanSession(true);
            /*options.setServerURIs(new String[]{broker});
            String pass = "12345678";
            options.setPassword(pass.toCharArray());*/

            client.connect(options);

            if (client.isConnected()) {
                CountDownLatch latch = new CountDownLatch(1); // Use CountDownLatch or sleep to keep the main thread alive

                client.setCallback(new MqttCallback() {
                    public void messageArrived(String topic, MqttMessage message) throws Exception {
                        String msg = new String(message.getPayload());
                        if (!msg.equals("Java Client Running!")) {
                            System.out.println("topic: " + topic);
                            System.out.println("qos: " + message.getQos());
                            System.out.println("message content: " + msg);

                            if (msg.equalsIgnoreCase("exitJavaClient")) {
                                String str = "Java Client Exit!";
                                MqttMessage pubMessage = new MqttMessage(str.getBytes());
                                pubMessage.setQos(pubQos);
                                client.publish(topic, pubMessage);

                                latch.countDown(); // Decrement the latch count
                            } else {
                                String str = "Java Client Running!";
                                MqttMessage pubMessage = new MqttMessage(str.getBytes());
                                pubMessage.setQos(pubQos);
                                client.publish(topic, pubMessage);
                            }
                        }
                    }

                    public void connectionLost(Throwable cause) {
                        System.out.println("connectionLost: " + cause.getMessage());
                    }

                    public void deliveryComplete(IMqttDeliveryToken token) {
                        System.out.println("Java Client message deliveryComplete: " + token.isComplete());
                    }
                });

                client.subscribe(subscribeTopic, subQos);

                String payload = "{temperature:10}";
                MqttMessage pubMessage = new MqttMessage(payload.getBytes());
                pubMessage.setQos(pubQos);
                client.publish(publishTopic, pubMessage);

                latch.await(); // Wait indefinitely
            }

            client.disconnect();
            client.close();

        } catch (MqttException | InterruptedException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
