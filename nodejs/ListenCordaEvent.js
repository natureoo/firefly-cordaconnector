const WebSocket = require('ws')
const ws = new WebSocket(`ws://localhost:8080/ws`);
let lastAck = null;
let heartBeatTimeout = setTimeout(() => {
    console.error('Event stream ping timeout');
    ws.terminate();
}, 10000);
const heartBeat = () => {
    ws.ping();
    clearTimeout(heartBeatTimeout);
    heartBeatTimeout = setTimeout(() => {
        console.error('Event stream ping timeout');
        ws.terminate();
    }, 2000);
}

ws.on('open', () => {
    ws.send(JSON.stringify({
        type: 'listen',
        topic: 'eventstream-0-topic'
    }));
    heartBeat();
}).on('close', () => {
    console.error(`Event stream websocket disconnected`);
}).on('message', async (message) => {
    const data = JSON.parse(message);
    for(var i=0; i<data.length; i++) {
        console.log(JSON.stringify(data[i]));
    }
    ws.send(JSON.stringify({
        type: 'ack',
        topic: 'eventstream-0-topic'
    }));
}).on('pong', () => {
    heartBeat();
}).on('error', err => {
    console.error(`Event stream websocket error. ${err}`);
});
