/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client;

/**
 *
 * @author kevinzhou
 */
public class Runner {

    public static void main(String args[]) {
        String simAddress = "127.0.0.1";
        int commandListenPort = 20011;
        String visionAddress = "224.5.23.2";
        int visionPort = 10020;
        int blueStatusPort = 30011;
        int yellowStatusPort = 30012;
        int botCount = 8;
        ConnectionManager connectionManager = new ConnectionManager(simAddress, visionAddress, commandListenPort,
                visionPort, blueStatusPort, yellowStatusPort, botCount);

        boolean isYellow = true;
        int id = 7;
        float wheel1 = 10;
        float wheel2 = 0;
        float wheel3 = 0;
        float wheel4 = 0;
        float kickspeedx = 0;
        float kickspeedz = 0;
        float velX = 0;
        float velY = 0;
        float velZ = 0;
        boolean spinner = false;
        boolean useWheelSpeed = true;

        connectionManager.setCommand(isYellow, id, wheel1, wheel2, wheel3, wheel4, kickspeedx, kickspeedz, velX, velY,
                velZ, spinner, useWheelSpeed);
        connectionManager.send();

        connectionManager.receive();
    }
}
