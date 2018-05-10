package com.mauriciodelira.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;

public class LocalServer implements Runnable {
  final private String baseDir = System.getProperty("user.dir").concat("/public/");
  private Socket client;

  public LocalServer(Socket clientConn) {
    this.client = clientConn;
  }

  public void run() {
    try {
      DataInputStream reader = new DataInputStream(client.getInputStream());
      DataOutputStream writer = new DataOutputStream(client.getOutputStream());

      String fileName = reader.readUTF();

      System.out.println("SERVER #" + Thread.currentThread().getName() + " -- Aceitou uma conexão. Cliente em: " +
        client.getInetAddress() + ":" + client.getPort() + " busca por: " + fileName);

      writer.writeUTF(getFileStatus(fileName));
      writer.writeUTF(readFile(fileName));

      if (!client.isClosed())
        client.close();

      System.out.println("SERVER #" + Thread.currentThread().getName() + " -- Conexão encerrada com o cliente em: " +
        client.getInetAddress() + ":" + client.getPort());

    } catch (IOException e) {

    }

  }

  private String file2String(File file, String charset) throws IOException {
    String message = "";

    if (charset.isEmpty())
      charset = "UTF-8";

    LineIterator it = FileUtils.lineIterator(file, charset);
    while (it.hasNext()) {
      message = message.concat(it.next() + "\n");
    }

    return message;
  }

  private String getFileStatus(String filename) {
    File file = new File(baseDir.concat(filename));

    if (!file.exists()) return "400";
    else return "200";

  }

  private String readFile(String filename) throws IOException {
    File file = new File(baseDir.concat(filename));

    String content;
    String[] parts = filename.split("\\.");
    if (file.exists() && parts.length > 1) {
      if (parts[1].equalsIgnoreCase("html") || parts[1].equalsIgnoreCase("txt")) {
        content = file2String(file, "UTF-8");
      } else {
        content = "Arquivo não suportado";
      }
    } else {
      content = "Arquivo [" + filename + "] não encontrado.";
    }

    return (
      String.format("\nHTTP/1.1 %s\n" +
        "\n// Body content of the file\n" +
        "%s", getFileStatus(filename), content)
    );
  }

}
