import React, { Fragment } from 'react'

import { parseJsonWithQuotes } from '../Helpers/Utils'

import WSForm from './WSForm/WSForm'
import WSChatRooms from './WSChatRooms/WSChatRooms'
import WSOnlineUsers from './WSOnlineUsers/WSOnlineUsers'
import WSChat from './WSChat/WSChat'
import WSSendInput from './WSChat/WSSendInput'

import './WebsocketComponent.css'

class WebsocketComponent extends React.Component {
  constructor(props) {
    super(props)

    this.state = {
      isInputDisabled: true,
      websocket: null,
      users: [],
      rooms: [],
      messages: [],
      connectionStatus: 'Desconectado',
      connectButtonText: 'Entrar na sala',
    }
  }

  openWebsocket = (form) => {
    this.setState({
      connectionStatus: 'Conectando...',
      websocket: new WebSocket(`ws://${form.ip}:${form.port}/chatroom/chat/${form.room}/${form.user}`)
    }, () => this.setupWebsocket())
  }

  closeWebsocket = () => {
    this.setState({
      connectionStatus: 'Desconectando...',
    }, () => {
      this.state.websocket.close()
      this.setState({
        websocket: null,
      })
    })
  }

  setupWebsocket() {
    this.state.websocket.onopen = this.onWSOpen
    this.state.websocket.onclose = this.onWSClose
    this.state.websocket.onmessage = this.onWSMessage
    this.state.websocket.onerror = this.onWSError
  }

  onWSOpen = (openedWS) => {
    this.clearMessages()
    this.setState({
      isInputDisabled: false,
      connectionStatus: 'Conectado',
      connectButtonText: 'Desconectar',
    })
  }

  onWSClose = (closedWS) => {
    this.setState({
      isInputDisabled: true,
      connectionStatus: 'Desconectado',
      connectButtonText: 'Entrar na sala',
    })
  }

  onWSMessage = (message) => {
    this.treatMessage(message.data)
  }

  onWSError = (err) => {
    console.log("errou", err)

  }

  addMessage = (message) => {
    this.setState(previousState => ({
      messages: [...previousState.messages, message]
    }))
  }

  sendMessage = (message) => {
    console.log("MESSAGE TO BE SENT: ", message)
    if(message !== undefined && message !== "")
      this.state.websocket.send(message)
  }

  setRooms = (newRooms) => {
    this.setState({
      rooms: newRooms
    })
  }

  setUsers = (newUsers) => {
    this.setState({
      users: newUsers
    })
  }

  clearMessages = () => {
    this.setState({
      messages: []
    }, 
    () => this.forceUpdate())
  }

  treatMessage = (message) => {
    const splitted = message.split("::")
    switch (splitted[0]) {
      case "users":
        this.setUsers(parseJsonWithQuotes(splitted[1])["online-users"])
        break
      case "rooms":
        this.setRooms(parseJsonWithQuotes(splitted[1])["available-rooms"])
        break
        
      case "err":
        this.addMessage(splitted[1])
        console.error(splitted[1])
        break
  
      case "msg":
        this.addMessage(splitted[1])
        break
  
      case "priv-msg":
        this.addMessage(splitted[1])
        break
        
      case "info":
        this.addMessage(splitted[1])
        console.debug("INFORMAÇÕES: ", splitted[1])
        break
  
      case "doc":
        const docs = parseJsonWithQuotes(splitted[1])
  
        console.log(JSON.stringify(docs), "docs")
        break
  
      default:
        console.debug("caiu em nada")
        break
    }
  }

  render() {
    return (
    <Fragment>
      <WSForm openWebsocket={this.openWebsocket} 
        closeWebsocket={this.closeWebsocket}
        connectButtonText={this.state.connectButtonText}
        connectionStatus={this.state.connectionStatus}
        />

      <div className="container-fluid">
        <div className="row">
          <section className="sidebar left col-3">
            <WSChatRooms rooms={this.state.rooms} />
          </section>
          
          <main className="main-chat col-6">
            <WSChat messages={this.state.messages} />
            <WSSendInput messages={this.state.messages} 
              clearChat={this.clearMessages}
              isInputDisabled={this.state.isInputDisabled}
              sendAction={this.sendMessage} />
          </main>

          <section className="sidebar right col-3">
            <WSOnlineUsers users={this.state.users} />
          </section>
        </div>
      </div>
    </Fragment>
    )
  }
}

export default WebsocketComponent