import React from 'react'

export default class WSSendInput extends React.Component {
  constructor(props) {
    super(props)
    this.state = {
      message: undefined,
    }
  }

  onSend = (ev) => {
    ev.preventDefault()

    this.props.sendAction(this.state.message)
    
    if(this.state.message)
      this.setState({
        message: ""
      })
  }

  onChange = (ev) => {
    this.setState({
      message: ev.target.value
    })
  }

  render() {
    return (
        <form onSubmit={this.onSend}>
          <div className="form-row">
            <div className="col-8">
              <input type="text" className="form-control"
                value={this.state.message}
                onChange={this.onChange}
                disabled={this.props.isInputDisabled}
                />
            </div>

            <div className="col-3">
              <button className="btn btn-primary" 
              onClick={this.onSend} disabled={this.props.isInputDisabled} >Enviar</button>
            </div>

            <div className="col-1">
              <button className="btn btn-outline-warning" onClick={this.props.clearChat}
                disabled={this.props.isInputDisabled}>
                <span class="oi oi-trash"></span>
              </button>
            </div>
          </div>
        </form>
    )
  }

}