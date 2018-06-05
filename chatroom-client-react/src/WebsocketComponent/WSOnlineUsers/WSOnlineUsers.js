import React from "react";
import styled from "styled-components";

export default function WSOnlineUsers(props) {

  const H3 = styled.h3`
    color: #988F91;
    background-color: #211E2D;
    text-align: center;
    font-size: 1.2rem;
    padding: 1rem;
    margin-top: 1rem;
    margin-bottom: 0;
    border-top-left-radius: 10px;
    border-top-right-radius: 10px;
  `;

  const Ul = styled.ul`
    background-color: #6B618B;
    margin: 0;
    padding: 1em;
    min-height: 280px;
    z-index: 1;
    border-bottom-left-radius: 10px;
    border-bottom-right-radius: 10px;
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
      <H3>Usuários na sala</H3>
      <Ul>{listItems}</Ul>
    </aside>
  );
}
