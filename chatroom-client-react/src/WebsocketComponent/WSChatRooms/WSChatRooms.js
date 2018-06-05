import React, { Fragment } from 'react';
import styled from 'styled-components';

export default function WSChatRoom(props) {

  const H3 = styled.h3`
    color: #a7daaf;
    background-color: #2e403d;
    text-align: center;
    font-size: 1.3rem;
    padding: 1em;
    margin-top: 1rem;
    margin-bottom: 0;
    border-top-left-radius: 10px;
    border-top-right-radius: 10px;
  `

  const Ul = styled.ul`
    background-color: #425e59;
    padding: 1em;
    margin-top: 0;
    margin-bottom: 0;
    border-bottom-left-radius: 10px;
    border-bottom-right-radius: 10px;
    min-height: 280px;
  `

  const Li = styled.li`
    list-style: none;
    color: #c3edca;
    font-family: 'Roboto Mono', monospace;
    font-weight: 500;
    text-align: left;
  `

  const listItems = props.rooms.map((room) => 
    <Li key={room}>{room}</Li>
  )

  return (
    <Fragment>
      <aside id="rooms-list">
        <H3>Salas</H3>
        <Ul>
          {listItems}
        </Ul>
      </aside>
    </Fragment>
  )
}