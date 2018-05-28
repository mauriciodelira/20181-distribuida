/* ----- Get elements from DOM ----- */
const btnConnect = $('#connect')
const btnSendMessage = $('#send-message')
const pConnectionStatus = $('#connection-status')
const ulMessages = $('#messages')

/* ----- Initialize some variables to be used ----- */
let connectionStatus = "Não conectado"
let ws

/* ----- Adds necessary events to DOM elements ----- */
btnConnect.bind("click", openConnection)
btnSendMessage.bind("click", sendMessage)

/* ----- Functions ----- */

function openConnection() {
  alterStatus("Conectando...", "transition")

  const ipAddr = $('#form-ip').val()
  const port = $('#form-port').val()
  const room = $('#form-room').val()
  const username = $('#form-username').val()
  openWs(ipAddr, port, room, username)
}

function closeConnection() {
  if(ws) {
    alterStatus("Fechando...")
    ws.close()
  }
}

function sendMessage() {
  const message = $('#message').val()

  if(ws) {
    ws.send(message)
  }
}

function addToChat(message, type) {
  switch(type) {
    case "message":
      ulMessages.append(`<li class="chat-message chat-bubble">${message}</li>`)
      break
      case "private":
      ulMessages.append(`<li class="chat-message chat-bubble-private">${message}</li>`)
      break
    case "info":
      ulMessages.append(`<li class="chat-message chat-info">${message}</li>`)
      break
    case "docs":
      ulMessages.append(`<li class="chat-message chat-documentation">${message}</li>`)
      break
      case "error":
      ulMessages.append(`<li class="chat-message chat-error">${message}</li>`)
      break
    default:
      console.log("Não foi possível interpretar a mensagem:", message)
      break  
  }
}

function jsonParseEscapedQuotes(v, fn) {
  return (JSON.parse(
    v.replace(/\\"/g, '"'),
    fn
  ))
}

function treatMessage(message) {
  const splitted = message.split("::")
  switch (splitted[0]) {
    case "users":
      console.log("usuários logados: ", jsonParseEscapedQuotes(splitted[1]))
      break
    case "rooms":
      console.log("salas disponíveis: ", jsonParseEscapedQuotes(splitted[1]))
      break
      
    case "err":
      addToChat(splitted[1], "error")
      break

    case "msg":
      addToChat(splitted[1], "message")
      break

    case "priv-msg":
      addToChat(splitted[1], "private")
      break
      
    case "info":
      addToChat(splitted[1], "info")
      break

    case "doc":
      const docs = jsonParseEscapedQuotes(splitted[1])
      console.log(docs)
      const docsStr = 


      addToChat(JSON.stringify(docs), "docs")
      break

    default:
      console.log("caiu em nada")
      break
  }
}

function openWs(ipAddress, port, room, username) {
  if(ipAddress && port && room && username) {
    ws = new WebSocket(
      `ws://${ipAddress}:${port}/chatroom/chat/${room}/${username}`
    );
    ws.onopen = onOpen;
    ws.onclose = onClose;
    ws.onmessage = onMessage;
    ws.onerror = onError;
  } else {
    alterStatus('Preencha os campos corretamente')
  }
}

function onOpen() {
  alterStatus("Conectado")
  btnConnect.unbind("click")
  btnConnect.bind("click", closeConnection)
  btnConnect.text("Desconectar")
  
}

function onClose() {
  alterStatus("Fechado")
  btnConnect.unbind("click")
  btnConnect.bind("click", openConnection);
  btnConnect.text("Conectar")
}

function onMessage(e) {
  treatMessage(e.data)
}

function onError(err) {
  console.log("ERRO VINDO DO SERV: ", err);
}

function alterStatus(status, classname) {
  pConnectionStatus.text(status)
  pConnectionStatus.class
}