package br.pd;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

public class Main {

    public static void main(String[] args) {
        try {
            Socket s = new Socket("10.0.154.243", 8000);
            System.out.println("Passou no servidor");


            DataInputThread rt = new DataInputThread(s);

            new Thread(rt).start();

            DataOutputStream outputStream = new DataOutputStream(s.getOutputStream());

            String sendMsg = JOptionPane.showInputDialog("Enviar msg:");

            while(!s.isClosed()) {

                outputStream.writeUTF(sendMsg);

                sendMsg = JOptionPane.showInputDialog("Enviar msg:");
            }

        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }

//        try {
//
//            Socket s = new Socket(InetAddress.getByName("10.0.154.243"), 8000);
//
//
//
//            DataOutputStream dos = new DataOutputStream(s.getOutputStream());
//            DataInputStream dis = new DataInputStream(s.getInputStream());
//
//            dos.writeUTF("testando");
//
//            String resp = dis.readUTF();
//
//            s.close();
//
//        } catch (IOException e) {
//            Logger.getLogger(e.getMessage());
//        }
    }
}
