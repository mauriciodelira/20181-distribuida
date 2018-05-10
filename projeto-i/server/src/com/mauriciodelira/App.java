package com.mauriciodelira;

import com.mauriciodelira.server.LocalServer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class App {

    /**
     * First parameter should be the default port
     */
    public static void main(String[] args) {

        int port = 54054;
        if(!args[0].isEmpty())
            port = Integer.parseInt(args[0]);

        try {
            ServerSocket server = new ServerSocket(port);
            System.out.println("SERVER -- Servidor ouvindo em: " + server.getInetAddress() + ":" +server.getLocalPort());

            do {
//                Aceita uma conexão com o cliente e abre uma nova thread
                Thread t = new Thread(new LocalServer(server.accept()));
                t.start();

            } while (!server.isClosed());

        } catch (IOException e) {

        }


    }
}
