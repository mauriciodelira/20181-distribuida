package br.pd;

import java.io.DataInputStream;
import java.net.Socket;

public class DadosEntradaThread extends Thread {
    private Socket serverSocket;

    public DadosEntradaThread(Socket server) {
        this.serverSocket = server;
    }

    /**
     * Unicamente responsável pela leitura dos dados que vem do Servidor para o Cliente
     */
    public void run() {
        try {
//            Só precisa do InputStream (read), visto que a escrita está sendo feita na Main
            DataInputStream in = new DataInputStream(serverSocket.getInputStream());

            String msg;

            /*
            * ENQUANTO...
            *   A mensagem que vem do servidor for diferente de "fim"
            *   ... e ...
            *   A conexão com o servidor estiver aberta,
            * fique lendo...
            * */
            do {
                msg = in.readUTF();
                System.out.println(formatIp(serverSocket) + " enviou: " + msg);

            } while(!msg.equalsIgnoreCase("fim") && !serverSocket.isClosed());

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    private String formatIp(Socket ss) {
        return ss.getInetAddress()+":"+ss.getPort();
    }
}
