package com.mauriciodelira;

import com.mauriciodelira.client.LocalClient;

import java.util.concurrent.ThreadLocalRandom;

public class App {
  public static void main(String[] args) {
    String baseUrl = args[0].isEmpty() ? "127.0.0.1:54321/test.test" : args[0];

    baseUrl = baseUrl.split("\\/")[0];

    String[] files = {"hello.html", "hello.txt", "notfound.txt"};

    //      Thread t = new Thread(new LocalClient(baseUrl));  // apenas quando em "produção"

    for (int i = 0; i < 5; i++) {

      //      Enquanto rodar localmente, os arquivos serão aleatórios
      int random = ThreadLocalRandom.current().nextInt(0, files.length);
      String newUrl = baseUrl.concat("/" + files[random]);
      Thread t = new Thread(new LocalClient(newUrl));
      t.start();
    }
  }
}
