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
        user: 'usuario',
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
      <aside className="container" style={{marginTop: 16}}>
        <div className="row">
          <form onSubmit={this.handleSubmit}>
            <div className="form-row">
              <div className="col-2">
                <input type="text" name="ip" className="form-control"
                  value={this.state.formFields.ip} 
                  onChange={this.handleChange}
                  required
                  />
              </div>

              <div className="col-2">
                <input type="text" name="port" className="form-control"
                  value={this.state.formFields.port}
                  onChange={this.handleChange}
                  required
                  />
              </div>

              <div className="col-3">
                <input type="text" name="room" className="form-control"
                  value={this.state.formFields.room} 
                  onChange={this.handleChange}
                  required
                  />
              </div>

              <div className="col-3">
                <input type="text" name="user" className="form-control"
                  value={this.state.formFields.user} 
                  onChange={this.handleChange}
                  required
                  />
              </div>

              <div className="col-2">
                <button className="btn btn-primary" onClick={this.handleSubmit}>{this.props.connectButtonText}</button>
              </div>
            </div>

          </form>
          <code className="connection-status">{this.props.connectionStatus}</code>
        </div>
      </aside>
      </Fragment>
    )
  }

}

export default WSForm;