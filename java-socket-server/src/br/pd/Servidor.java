package br.pd;

import javax.swing.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Logger;

public class Servidor {

    public static void main(String[] args) {
        ServerSocket socketDoServidor;
        Socket socketDoCliente;

        try {
//            Inicia um servidor na porta 33221
            socketDoServidor = new ServerSocket(33221);
            System.out.println("Endereço do servidor: "+socketDoServidor.getInetAddress() + ":" + socketDoServidor.getLocalPort());

            socketDoCliente = socketDoServidor.accept();
            System.out.println("Servidor conectado ao cliente: " + socketDoCliente.getInetAddress() + ":" + socketDoCliente.getPort());

//            Inicia uma thread de leitura vinculada a um cliente
//              (o `accept()` retorna o socket do cliente)
            Thread t = new Thread(new LeitorThread(socketDoCliente));
            t.start();

//            Antes de tudo, vamos já capturar a mensagem que quero enviar para o cliente...
            String msg = JOptionPane.showInputDialog("SERVIDOR || enviar para cliente: ");

//            Enquanto o servidor estiver aberto...
            while (!socketDoServidor.isClosed()) {


//                A partir daqui, o processo de leitura está tôdo na thread acima,
//                  deixando essa livre para escrita

//                Envia dados para o cliente
                DataOutputStream dos = new DataOutputStream(socketDoCliente.getOutputStream());
                dos.writeUTF(socketDoServidor.getInetAddress() + ":" + socketDoServidor.getLocalPort() + ": " + msg);
                msg = JOptionPane.showInputDialog("SERVIDOR || enviar para cliente: ");

                if(msg == null || msg.equalsIgnoreCase("fim") || socketDoCliente.isClosed()) {
                    socketDoCliente.close();
                    socketDoServidor.close();
                }

            }

        } catch (IOException e) {
            Logger.getLogger(e.getMessage());
        }
    }
}
