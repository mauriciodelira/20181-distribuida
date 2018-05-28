import React, { Fragment } from 'react';
import styled from 'styled-components';

export default function WSChatRoom(props) {

  const H3 = styled.h3`
    color: #a7daaf;
    background-color: #2e403d;
    text-align: center;
    padding: 1em;
    margin-top: 0;
    margin-bottom: 0;
  `

  const Ul = styled.ul`
    background-color: #425e59;
    padding: 1em;
    margin-top: 0;
    margin-bottom: 0;
    min-height: 10em;
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