import React, { Fragment } from "react";
import '../WebsocketComponent.css'
import styled from "styled-components";

function WSChat(props) {

  const listMessages = props.messages.map((m) => 
    <li className="chat-message">{m}</li>
  )

  const Ul = styled.ul`
    margin: 1rem;
    overflow-x: hidden;
    overflow-y: scroll;
    min-height: 280px;
    max-height: 280px;
    position: relative;
  `

  return (
    <Fragment>
      <Ul>{listMessages}</Ul>
    </Fragment>
  )
}

 export default WSChat