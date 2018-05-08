package com.mauriciodelira.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;

public class LocalServer {
    final private String baseDir = System.getProperty("user.dir").concat("/public/");

    private String file2String(File file, String charset) throws IOException {
        String message = "";
        LineIterator it = FileUtils.lineIterator(file, charset);
        while(it.hasNext()) {
            message = message.concat(it.next());
        }

        return message;
    }

    private String getFileStatus(String filename) throws IOException {
        File file = new File(baseDir.concat(filename));

        if(!file.exists()) return "400";
        else return "200";

    }

    private String readFile(String filename) throws IOException {
        File file = new File(baseDir.concat(filename));

        String content;
        String[] parts = filename.split("\\.");
        if(file.exists() && parts.length > 1) {
            switch (parts[1]) {
                case "html":
//                    type = "text/html";
                    content = file2String(file, "UTF-8");
                    break;
                case "txt":
//                    type = "text/plain";
                    content = file2String(file, "UTF-8");
                    break;
                default:
//                    type = "unknown";
                    content = "Arquivo não suportado";
                    break;
            }
        } else {
//            type = filename.split(".")[1];
            content = "Arquivo [" + filename + "] não encontrado.";
        }

        return (
            String.format("\nHTTP/1.1 %s\n" +
//                "Content-type: %s\n" +
                "// Body content of the file\n" +
                "%s", getFileStatus(filename), content)
        );
    }

    /**
     * First parameter should be the default port
     * @param args
     */
    public static void main(String[] args) {

        int port = 54054;
        if(!args[0].isEmpty())
            port = Integer.parseInt(args[0]);

        try {
            ServerSocket server = new ServerSocket(port);
            System.out.println("SERVER -- Servidor ouvindo em: " + server.getInetAddress() + ":" +server.getLocalPort());

            /*  O que deve ser feito após iniciar o servidor?
            *
            * 1. conectar com o cliente
            * 2. ler o nome do recurso
            * 3. buscar o recurso em /public
            * 4. transformar em String
            * 5. escrever 200 se achou ou 400 se não
            * 6. escrever o arquivo para o cliente, caso tenha achado
            *
            * */
            do {
//                Aceita uma conexão com o cliente
                Socket client = server.accept();
                System.out.println("SERVER -- Aceitou uma conexão. Cliente em: "+client.getInetAddress()+":"+client.getPort());

                DataInputStream reader = new DataInputStream(client.getInputStream());
                DataOutputStream writer = new DataOutputStream(client.getOutputStream());

                String fileName = reader.readUTF();

                LocalServer local = new LocalServer();

                writer.writeUTF(local.getFileStatus(fileName));
                writer.writeUTF(local.readFile(fileName));

                if(!client.isClosed())
                    client.close();

                System.out.println("SERVER -- Conexão encerrada com o cliente em: "+
                        client.getInetAddress()+":"+client.getPort());

            } while (!server.isClosed());

        } catch (IOException e) {

        }


    }
}
