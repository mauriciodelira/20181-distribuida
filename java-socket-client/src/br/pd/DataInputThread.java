package br.pd;

import java.io.DataInputStream;
import java.net.Socket;

public class DataInputThread extends Thread {
    private Socket serverSocket;

    DataInputThread(Socket server) {
        this.serverSocket = server;
    }

    public void run() {
        try {
            DataInputStream in = new DataInputStream(serverSocket.getInputStream());

            String msg;

            do {
                msg = in.readUTF();
                System.out.println(formatIp(serverSocket) + " enviou: " + msg);

            } while(!msg.equalsIgnoreCase("fim"));

            serverSocket.close();

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    private String formatIp(Socket ss) {
        return ss.getInetAddress()+":"+ss.getPort();
    }
}
