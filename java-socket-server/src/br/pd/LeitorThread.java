package br.pd;

import java.io.DataInputStream;
import java.net.Socket;

public class LeitorThread implements Runnable {
    private Socket socketDoCliente;

    public LeitorThread(Socket cliente) {
        this.socketDoCliente = cliente;
    }

    public void run() {
        try {
//            Recebe dados daquele socket especificado
            DataInputStream dis = new DataInputStream(socketDoCliente.getInputStream());

            while(!socketDoCliente.isClosed()){
                System.out.println( dis.readUTF() );
            }

        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
