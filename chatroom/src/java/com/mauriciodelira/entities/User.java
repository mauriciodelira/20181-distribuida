/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mauriciodelira.entities;

import javax.websocket.Session;

/**
 *
 * @author mauriciodelira
 */
public class User {
  private String username;
  private Session session;

  public User(String username, Session session) {
    this.username = username;
    this.session = session;
  }

  public String getUsername() {
    return username;
  }

//  public void setUsername(String username) {
//    this.username = username;
//  }

  public Session getSession() {
    return session;
  }

//  public void setSession(Session session) {
//    this.session = session;
//  }
  
}
