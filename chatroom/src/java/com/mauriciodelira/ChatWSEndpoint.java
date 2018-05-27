/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mauriciodelira;

import com.mauriciodelira.entities.User;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.ws.rs.NotFoundException;

/**
 *
 * @author mauriciodelira
 */
@ServerEndpoint("/chat/{room}/{username}")
public class ChatWSEndpoint {

  // room-name: { username: { first-username, user-session } };
  private static Map<String, Map<String, User>> rooms = Collections.synchronizedMap(new HashMap<>());
  private static final String CHAT_DOCS = "doc::Você pode tentar:\n"
          + "send [mensagem]\t\t\tEnviar mensagem para todos no chat.\n"
          + "send -u [usuario] [mensagem]\t\t\tEnviar mensagem privada.\n"
          + "rename [novo-usuario]\t\t\tRenomear seu usuário na sala.";

  @OnOpen
  public void onOpen(Session sess, @PathParam("room") String room, @PathParam("username") String username) {
    User newUser = new User(username, sess);

    if (ChatWSEndpoint.rooms.containsKey(room) && ChatWSEndpoint.rooms.get(room) != null) {
      Map<String, User> selectedRoom = ChatWSEndpoint.rooms.get(room);

      if (selectedRoom.containsKey(username)) {
//        Takes a default username
        String newUsername = defaultUsername(selectedRoom, username);

//        Puts user in selected room
        selectedRoom.putIfAbsent(newUsername, newUser);

        sendTo(selectedRoom, newUsername, changedUsernameMessage(username, newUsername, true, true));

      } else {
        selectedRoom.putIfAbsent(username, newUser);
      }
    } else {
//      Creates a new room
      initEmptyRoom(room).putIfAbsent(username, newUser);
    }

    broadcastCurrentUsers(ChatWSEndpoint.rooms.get(room));
  }

  @OnMessage
  public void onMessage(String message, Session sess, @PathParam("room") String room) {
    Map<String, User> selectedRoom = ChatWSEndpoint.rooms.get(room);
    Entry<String, User> actualUser = findBySession(selectedRoom, sess);
    BiFunction<String, Integer, String[]> splitter = (xmsg, xmax) -> xmsg.split(" ", xmax);

    switch (splitter.apply(message, 2)[0]) {
      case "help":
        sendTo(sess, CHAT_DOCS);
        break;
      case "rename":
        renameUser(selectedRoom, actualUser, message.split(" ")[1]);
        break;
      case "send":
        String[] messageParams = splitter.apply(message, 4);
        if(messageParams[1].equals("-u") && messageParams.length > 3) {
          sendPrivatelyTo(selectedRoom, actualUser, messageParams[2], messageParams[3]);
        } else {
          sendToAll(selectedRoom, actualUser, splitter.apply(message, 2)[1], true);
        }
        break;
      default:
        sendTo(sess, "err::Operação não suportada.\n" + CHAT_DOCS);
        break;
    }
  }

  /**
   * Creates the default message to be sent from a user to another user
   */
  private String defaultMessage(String sentBy, String message, boolean isPrivate) {
    return "msg::" + sentBy + " às "
            + new SimpleDateFormat("HH:mm").format(Calendar.getInstance().getTimeInMillis())
            + (isPrivate ? " reservadamente: " : ": ")
            + message;
  }

  @OnClose
  public void onClose(Session sess, @PathParam("room") String room, @PathParam("username") String user) {
    Map<String, User> selectedRoom = ChatWSEndpoint.rooms.get(room);

    selectedRoom.entrySet().removeIf(e -> e.getValue().getSession() == sess);

    broadcastCurrentUsers(selectedRoom);
  }

  private void renameUser(Map<String, User> coll, Entry<String, User> oldUser, String renamedKey) {
    if (coll.containsKey(renamedKey)) {
      // User already exists.
      sendTo(oldUser.getValue().getSession(), changedUsernameMessage(oldUser.getKey(), renamedKey, true, false));
    } else {
      coll.put(renamedKey, oldUser.getValue());
      coll.remove(oldUser.getKey());
      sendTo(oldUser.getValue().getSession(), changedUsernameMessage(oldUser.getKey(), renamedKey, false, true));
      sendToAll(coll, findBySession(coll, oldUser.getValue().getSession()), "info::[ "+oldUser.getKey()+" ] renomeou para: [ "+renamedKey+" ]", false);
      broadcastCurrentUsers(coll);
    }
  }

  private void sendToAll(Map<String, User> coll, Entry<String, User> actualUserEntry, String message, boolean useDefaultMessage) {
    coll.entrySet().stream()
            .forEach(u -> 
                    sendTo(u.getValue().getSession(), 
                            useDefaultMessage ? defaultMessage(actualUserEntry.getKey(), message, false) : message
            ));
  }

  private void sendPrivatelyTo(Map<String, User> coll, Entry<String, User> actualUserEntry, String receiverUsername, String message) {
    User userReceiver = coll.get(receiverUsername);
    User actualUser = actualUserEntry.getValue();
    
    if (userReceiver != null && userReceiver.getSession().isOpen()) {
      sendTo(userReceiver.getSession(), defaultMessage(actualUserEntry.getKey(), message, true));
      sendTo(actualUser.getSession(), defaultMessage(actualUserEntry.getKey(), message, true));
    } else {
      sendTo(actualUser.getSession(), "err::Usuário [ " + receiverUsername + " ] não encontrado.");
    }
  }

  private boolean sendTo(Session sess, String message) {
    try {
      sess.getBasicRemote().sendText(message);
      return true;
    } catch (IOException ex) {
      Logger.getLogger(ChatWSEndpoint.class.getName()).log(Level.SEVERE, null, ex);
      return false;
    }
  }

  /**
   * Searches in a room for an username, and sends through its socket a message *
   */
  private boolean sendTo(Map<String, User> coll, String username, String msg) throws NotFoundException {
    if (!coll.containsKey(username)) {
      throw new NotFoundException("err::Não foi possível encontrar o usuário [ " + username + " ].");
    }

    return sendTo(coll.get(username).getSession(), msg);
  }

  /**
   * Takes an user and concatenates a random number at the end of it
   */
  private String defaultUsername(Map<String, User> room, String originalUser) {
    String temp = originalUser + "-" + new java.util.Random().nextInt(9999);
    if (room.containsKey(temp)) {
      return defaultUsername(room, originalUser);
    } else {
      return temp;
    }
  }

  private Map<String, User> initEmptyRoom(String room) {
    ChatWSEndpoint.rooms.put(room, Collections.synchronizedMap(new HashMap<>()));
    return ChatWSEndpoint.rooms.get(room);
  }

  private String changedUsernameMessage(String oldUsername, String newUsername, boolean notAvailable, boolean hasChanged) {
    String finalMessage = "info::";

    if (notAvailable) {
      finalMessage = finalMessage.concat("O usuário [ " + oldUsername + " ] não está disponível.\n");
    }
    if (hasChanged) {
      finalMessage = finalMessage.concat("Seu novo usuário é: [ " + newUsername + " ].\n");
      finalMessage = finalMessage.concat("Para alterá-lo novamente, envie uma mensagem "
              + "no padrão \"rename [novo-usuário]\"\n");
    } else {
      finalMessage = finalMessage.concat("Seu usuário é: [ " + oldUsername + " ].");
    }

    return finalMessage;
  }

  private Entry<String, User> findBySession(Map<String, User> coll, Session session) {
    return coll.entrySet().stream()
            .filter(u -> u.getValue().getSession().equals(session))
            .findFirst()
            .get();
  }

  private String setAsString(Set<String> s) {
    return s.stream()
            .collect(Collectors.joining("'; '", "{ 'online-users': ['", "'] }"));
  }

  private void broadcastCurrentUsers(Map<String, User> room) {
    try {
      for (Entry<String, User> e : room.entrySet()) {
        e.getValue().getSession().getBasicRemote()
                .sendText("usrs::" + setAsString(room.keySet()));
      }
    } catch (IOException ex) {
      Logger.getLogger(ChatWSEndpoint.class.getName()).log(Level.SEVERE, null, ex);
    }
  }
}