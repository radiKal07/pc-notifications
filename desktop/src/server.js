import http from 'http';
import IO from 'socket.io';
import ip from 'ip';

var server = http.createServer();
var io = new IO(server);

server.listen(0, ip.address(), function() {
    let port = server.address().port;
});

io.on('connection', function (socket) {
    socket.on('trying', function(data, callback){
        console.log('received from client: ', data);
        callback({msg: 'primit'});
    });
});