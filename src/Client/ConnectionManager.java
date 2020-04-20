package Client;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.Arrays;
import Protobuf.*;
import java.util.ArrayList;

public class ConnectionManager {
    String simAddress, visionAddress;
    int commandListenPort, visionPort, blueStatusPort, yellowStatusPort, botCount;
    ArrayList<Connection> connections;

    ConnectionManager(String simAddress, String visionAddress, int commandListenPort, int visionPort,
            int blueStatusPort, int yellowStatusPort, int botCount) {
        this.simAddress = simAddress;
        this.visionAddress = visionAddress;
        this.commandListenPort = commandListenPort;
        this.visionPort = visionPort;
        this.blueStatusPort = blueStatusPort;
        this.yellowStatusPort = yellowStatusPort;
        this.botCount = botCount;

        connections = new ArrayList<>(botCount * 2);
        for (int i = 0; i < botCount; i++)
            connections.add(new Connection(simAddress, commandListenPort, false, i));
        for (int i = 0; i < botCount; i++)
            connections.add(new Connection(simAddress, commandListenPort, true, i));
    }

    public Connection getConnection(boolean isYellow, int id) {
        if (isYellow)
            return connections.get(id + botCount);
        else
            return connections.get(id);
    }

    public void setCommand(boolean isYellow, int id, float wheel1, float wheel2, float wheel3, float wheel4,
            float kickspeedx, float kickspeedz, float velX, float velY, float velZ, boolean spinner, boolean useWheelSpeed) {
        Connection connection = getConnection(isYellow, id);
        connection.setWheel1(wheel1);
        connection.setWheel2(wheel2);
        connection.setWheel3(wheel3);
        connection.setWheel4(wheel4);
        connection.setKickspeedX(kickspeedx);
        connection.setKickspeedZ(kickspeedz);
        connection.setVelX(velX);
        connection.setVelY(velY);
        connection.setVelZ(velZ);
        connection.setSpinner(spinner);
        connection.setUseWheelSpeed(useWheelSpeed);
    }

    /*
     * Source: https://bit.ly/3cjWx9U
     */
    public static byte[] trim(byte[] bytes) {
        int i = bytes.length - 1;
        while (i >= 0 && bytes[i] == 0) {
            --i;
        }
        return Arrays.copyOf(bytes, i + 1);
    }

    /*
     * Author: Zihao Zhou
     */
    public void receive() {
        byte[] buffer = new byte[65536];

        try {
            MulticastSocket ds = new MulticastSocket(visionPort);
            InetAddress group = InetAddress.getByName(visionAddress);
            ds.joinGroup(group);

            DatagramPacket dp = new DatagramPacket(buffer, buffer.length);
            ds.receive(dp);
            MessagesRobocupSslWrapper.SSL_WrapperPacket packet = MessagesRobocupSslWrapper.SSL_WrapperPacket
                    .getDefaultInstance().parseFrom(trim(dp.getData()));
            System.out.println(packet.toString());
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void send() {
        for (Connection connection : connections)
            connection.send();
    }
}
