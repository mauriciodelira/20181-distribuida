import React, { Component, Fragment } from 'react';

class WSForm extends Component {
  constructor(props) {
    super(props)
    console.log("WSFORM PROPS ", props)
    this.state = {
      formFields: {
        ip: '127.0.0.1',
        port: '8080',
        room: 'geral',
        user: 'vanjie',
      },
    }

    this.handleSubmit = this.handleSubmit.bind(this);
    this.handleChange = this.handleChange.bind(this);


  }
  
  handleSubmit(ev) {
    ev.preventDefault()
    if(this.props.connectionStatus === 'Conectado')
      this.props.closeWebsocket()
    else this.props.openWebsocket(this.state.formFields)

  }
  
  handleChange(ev) {
    this.setState({
      formFields: {
        ...this.state.formFields,
        [ev.target.name]: ev.target.value,
      }
    })
  }

  render() {
    return (
      <Fragment>
        <aside style={{border: '1px solid blue', background: 'lightblue'}}>
          <form onSubmit={this.handleSubmit}>
            <input type="text" name="ip" 
              value={this.state.formFields.ip} 
              onChange={this.handleChange}
              />

            <input type="text" name="port" 
              value={this.state.formFields.port}
              onChange={this.handleChange}
              />

            <input type="text" name="room" 
              value={this.state.formFields.room} 
              onChange={this.handleChange}
              />

            <input type="text" name="user" 
              value={this.state.formFields.user} 
              onChange={this.handleChange}
              />

            <button onClick={this.handleSubmit}>{this.props.connectButtonText}</button>
          </form>
          <code className="connection-status">{this.props.connectionStatus}</code>
        </aside>
      </Fragment>
    )
  }

}

export default WSForm;