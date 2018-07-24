package com.mauriciodelira.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LocalClient implements Runnable {
  private String baseUrl;

  public LocalClient(String baseUrl) {
    this.baseUrl = baseUrl;
  }

  public void run() {
    try {
      if (!baseUrl.isEmpty()) {
        String response = getFileFromServer(decomposeURL(baseUrl));
        System.out.println("---------- - --------- -------\n* REQUEST: " + response + "\n");
      } else System.out.println("Não foi possível conectar-se. Verifique sua URL de conexão e tente novamente.\n ====== ====== ");

      System.out.println("---------- - --------- -------\n");
    } catch (IOException e) {
      System.out.println("-- Oops, algo não deu certo. Erro:\n\n" + e.getMessage());
    }
  }

  private static List<String> decomposeURL(String url) {
    //        Extrai a URL, Porta e Recurso e guarda num Matcher
    Matcher m = Pattern.compile("^([\\d\\.?]+):(\\d{1,6})\\/(\\w+.\\w+)$").matcher(url);

    //        Puxa do Matcher os 3 grupos e coloca numa lista
    ArrayList<String> params = new ArrayList<>();
    while (m.find()) {
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
    if (server.isClosed() || server.isOutputShutdown())
      return "Não foi possível conectar-se ao servidor em: " + url + ":" + port + ". Tente novamente mais tarde.";
    else if (server.isConnected())
      System.out.println("Conectado com o servidor em: " + url + ":" + port + ". Aguardando resposta para o arquivo [" + filename + "] abaixo...");


    //        Envia a mensagem de que quer o arquivo especificado
    out.writeUTF(filename);

    //        A primeira leitura é o Status
    String content = in.readUTF();

    //        E a segunda leitura é o conteúdo
    content = content.concat("\n" + in.readUTF());

    server.close();

    return content;
  }
}
