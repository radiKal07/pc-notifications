import http from 'http';
import IO from 'socket.io';
import ip from 'ip';
import {ipcMain} from 'electron';

export const startServer = (win) => {
    let server = http.createServer();
    let io = new IO(server);
    let port = 0;

    io.on('connection', function (socket) {
        socket.on('trying', function(data, callback){
            console.log('client trying to connect: ', data);
            win.webContents.send('pairing-done', port);
            callback({msg: 'primit'});
        });
    });

    ipcMain.on('start-pairing', (event, arg) => {
        if (server.address() == null) {
            server.listen(0, ip.address(), function() {
                port = server.address().port;
                event.sender.send('port-found', port);
            });
        } else {
            event.sender.send('port-found', port);
        }
    });
}