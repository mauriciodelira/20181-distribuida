import React from 'react'

export default class WSSendInput extends React.Component {
  constructor(props) {
    super(props)

    this.state = {
      message: undefined
    }

    console.log("wssendmessage PROPS: ", props)
  }

  onSend = (ev) => {
    ev.preventDefault()

    this.props.sendAction(this.state.message)
    
    if(this.state.message)
      this.setState({
        message: undefined
      })
  }

  onChange = (ev) => {
    console.log(ev.target.value)
    this.setState({
      message: ev.target.value
    })
  }

  render() {
    return (
      <form onSubmit={this.onSend}>
        <input type="text"
          value={this.state.message}
          onChange={this.onChange}
          />

        <button onClick={this.onSend}>Enviar</button>
      </form>
    )
  }

}