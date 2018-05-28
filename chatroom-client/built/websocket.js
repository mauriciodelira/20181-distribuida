const connect = document.getElementById("connect");
connect.addEventListener("click", function (e) {
    console.log('clicou para conectar');
    openWs;
});
let connectionStatus = "Não conectado";
function openWs(baseUrl, port, room, username) {
    const ws = new WebSocket(`ws://${baseUrl}:${port}/chatroom/chat/${room}/${username}`);
    ws.onopen = onOpen;
    ws.onclose = onClose;
    ws.onmessage = onMessage;
    ws.onerror = onError;
    connectionStatus = "Conectando...";
}
function onOpen() { }
function onClose() { }
function onMessage(event) {
    console.log("MENSAGEM DO SERVIDOR: ", event);
}
function onError(err) {
    console.log("ERRO VINDO DO SERV: ", err);
}
