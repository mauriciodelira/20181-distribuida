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
            Socket socketDoServidor = new Socket("localhost", 33221);
            System.out.println("Passou no servidor");

//            Inicia uma nova thread de leitura dos dados
            DadosEntradaThread thread = new DadosEntradaThread(socketDoServidor);
            new Thread(thread).start();

//            Perceba que aqui só há o OutputStream, não há mais InputStream
//              nessa classe (foi movida para DadosEntradaThread)
            DataOutputStream outputStream = new DataOutputStream(socketDoServidor.getOutputStream());

//            Captura a mensagem que quero enviar para o servidor
            String sendMsg = JOptionPane.showInputDialog("CLIENTE || enviar para servidor:");

            /*
             * ENQUANTO...
             *   O que eu digitar for diferente de "fim"
             *   ... e ...
             *   A conexão com o servidor estiver aberta
             * fique lendo o que eu digitar...
             * */
            while(!socketDoServidor.isClosed())
            {
                outputStream.writeUTF("CLIENTE: " + sendMsg);

                sendMsg = JOptionPane.showInputDialog("CLIENTE || enviar para servidor:");

                if(sendMsg == null || sendMsg.equalsIgnoreCase("fim"))
                    socketDoServidor.close();

            }

        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
