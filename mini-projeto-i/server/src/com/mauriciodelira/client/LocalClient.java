package com.mauriciodelira.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LocalClient implements Runnable {
    // FOR EXAMPLE ONLY - Right arg: "127.0.0.1:54321/hello.html";
    private String baseUrl;

    public LocalClient(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public void run() {
        try {
            if (!baseUrl.isEmpty()) {
                String response = getFileFromServer(decomposeURL(baseUrl));
                System.out.println("* RESPONSE\n" + response + "\n ------ \n");
            } else System.out.println("Não foi possível conectar-se. Verifique sua URL de conexão e tente novamente.");

            System.out.println(" -- Encerrando a aplicação cliente --");
        } catch (IOException e) {
            System.out.println(" -- Oops, algo não deu certo. Erro:\n\n" + e.getMessage());
        }
    }

    private static List<String> decomposeURL(String url) {
//        Extrai a URL, Porta e Recurso e guarda num Matcher
        Matcher m = Pattern.compile("^([\\d\\.?]+):(\\d{1,6})\\/(\\w+.\\w+)$").matcher(url);

//        Puxa do Matcher os 3 grupos e coloca numa lista
        ArrayList<String> params = new ArrayList<>();
        while(m.find()) {
            params.add(m.group(1));
            params.add(m.group(2));
            params.add(m.group(3));
        }

        return params;
    }

    private static String getFileFromServer(List<String> params) throws IOException {
        String url = params.get(0);
        int port = Integer.parseInt(params.get(1));
        String filename = params.get(2);

//        Inicia a conexão com o servidor no endereço e porta passados
        Socket server = new Socket(url, port);

//        Pega a escrita e leitura do servidor para futuramente enviar o nome do arquivo e ler a resposta
        DataOutputStream out = new DataOutputStream(server.getOutputStream());
        DataInputStream in = new DataInputStream(server.getInputStream());

//        Verifica se a conexão falhou
        if(server.isClosed() || server.isOutputShutdown())
            return "Não foi possível conectar-se ao servidor em: "+ url + ":" + port +". Tente novamente mais tarde.";
        else if(server.isConnected())
            System.out.println("Conectado com o servidor em: " + url + ":" + port + ". Aguardando resposta para o arquivo ["+ filename + "] abaixo...");


//        Envia a mensagem de que quer o arquivo especificado
        out.writeUTF(filename);

//        A primeira leitura é o Status
        String message = in.readUTF();

//        E a segunda leitura é o conteúdo
        message = message.concat(in.readUTF());

        server.close();

        return message;
    }


    public static void main(String[] args) {
        String baseUrl = args[0].isEmpty() ? "" : args[0];

        baseUrl = baseUrl.split("\\/")[0];

        String[] files = {"hello.html", "hello.txt", "notfound.txt"};

        for(int i = 0; i < 5; i ++){
            int random = ThreadLocalRandom.current().nextInt(0, 3);
            String newUrl = baseUrl.concat("/" + files[random]);

            Thread t = new Thread(new LocalClient(newUrl));
            t.start();
            try {
                t.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


    }
}
