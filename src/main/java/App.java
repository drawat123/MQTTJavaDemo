import org.eclipse.paho.client.mqttv3.MqttException;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class App {
    private JPanel panelMain;
    private JTextField brokerTextField;
    private JTextField clientIdtextField;
    private JTextField subscribeTopicTextField;
    private JTextField publishTopicTextField;
    private JTextField userNametextField;
    private JButton publishButton;
    private JButton subscribeButton;
    private JButton connectButton;
    private JTextField publiishMessageTextField;
    private JLabel subscribeMessageLabel;
    private MQTTClient mqttClient;

    public App() {
        mqttClient = new MQTTClient();
        connectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    mqttClient.connect(brokerTextField.getText(), clientIdtextField.getText(), userNametextField.getText());
                    subscribeMessageLabel.setText("YO!");
                } catch (MqttException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        publishButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    mqttClient.publish(publishTopicTextField.getText(), publiishMessageTextField.getText());
                } catch (MqttException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        subscribeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    mqttClient.subscribe(subscribeTopicTextField.getText());
                } catch (MqttException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("App");
        frame.setContentPane(new App().panelMain);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    /*
String broker = "tcp://localhost:1883", clientId = "demo_client", username = "46C8CHXtCgYU9bdI6XSB", "ltxxmXyP3Wi9gJpnSVyL";
String subscribeTopic = "v1/devices/me/attributes", publishTopic = "v1/devices/me/telemetry";
String payload = "{temperature:10}";
*/
}
