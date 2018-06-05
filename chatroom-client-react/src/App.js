import React, { Component } from 'react';
import WebsocketComponent from './WebsocketComponent/WebsocketComponent'
import './App.css';

class App extends Component {
  constructor(props) {
    super(props)

    this.state = {
      error: null,
    }
  }

  render() {
    return (
      <div>
        <header className="App-header">
          <h1 className="App-title">Websocket Chat</h1>
        </header>

        <WebsocketComponent />
      </div>
    );
  }
}

export default App;
