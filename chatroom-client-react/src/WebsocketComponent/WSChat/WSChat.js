import React, { Fragment } from "react";
import '../WebsocketComponent.css'
import styled from "styled-components";

function WSChat(props) {
  console.log("WSChat props:", props)

  const listMessages = props.messages.map((m) => 
    <li className="chat-message">{m}</li>
  )

  const Ul = styled.ul`
    overflow: hidden;
    overflow-y: scroll;
  `

  return (
    <Fragment>
      <ul>{listMessages}</ul>
    </Fragment>
  )
}

 export default WSChat