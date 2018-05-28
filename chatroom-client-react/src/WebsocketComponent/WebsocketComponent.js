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
    this.setState({
      connectionStatus: 'Conectado',
      connectButtonText: 'Desconectar',
    })
  }

  onWSClose = (closedWS) => {
    this.setState({
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
        console.log(splitted[1], "error")
        break
  
      case "msg":
        this.addMessage(splitted[1])
        console.log(splitted[1], "message")
        break
  
      case "priv-msg":
        this.addMessage(splitted[1])
        break
        
      case "info":
        console.log(splitted[1], "info")
        break
  
      case "doc":
        const docs = parseJsonWithQuotes(splitted[1])
        console.log(docs)
  
        console.log(JSON.stringify(docs), "docs")
        break
  
      default:
        console.log("caiu em nada")
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

      <section className="sidebar left">
        <WSChatRooms rooms={this.state.rooms} />
      </section>
      
      <main className="main-chat">
        <WSChat messages={this.state.messages} />
        <WSSendInput messages={this.state.messages} 
          sendAction={this.sendMessage} />
      </main>

      <section className="sidebar right">
        <WSOnlineUsers users={this.state.users} />
      </section>
    </Fragment>
    )
  }
}

export default WebsocketComponent