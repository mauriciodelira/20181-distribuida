import React from "react";
import styled from "styled-components";

export default function WSOnlineUsers(props) {

  const H3 = styled.h3`
    color: #988F91;
    background-color: #211E2D;
    text-align: center;
    padding: 1em;
    margin-top: 0;
    margin-bottom: 0;
    z-index: 2;
  `;

  const Ul = styled.ul`
    background-color: #6B618B;
    margin: 0;
    padding: 1em;
    min-height: 10em;
    z-index: 1;
  `

  const Li = styled.li`
    list-style: none;
    color: #E8DDD5;
    font-family: 'Roboto Mono', monospace;
    font-weight: 500;
    text-align: left;
  `

  const listItems = props.users.map(usr => (
    <Li key={usr}>{usr}</Li>
  ));

  return (
    <aside>
      <H3>online users</H3>
      <Ul>{listItems}</Ul>
    </aside>
  );
}
