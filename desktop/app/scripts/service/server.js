import http from 'http';
import IO from 'socket.io';
import ip from 'ip';
import {ipcMain} from 'electron';

export const startServer = (win, settingsDao) => {
    let server = http.createServer();
    let io = new IO(server);
    let port = 0;

    io.on('connection', function (socket) {
        socket.on('trying', function(data, callback){
            console.log('client trying to connect: ', data);
            win.webContents.send('pairing-done', port);
            settingsDao.save({port: port})
            callback({msg: 'primit'});
        });
    });

    ipcMain.on('start-pairing', async (event, arg) => {
        let port = await settingsDao.getPort();
        if (!port) {
            console.log('port is not setted');
            port = 0;
        }
        if (server.address() == null) {
            server.listen(port, ip.address(), function() {
                if (port == 0) {
                    port = server.address().port;
                    settingsDao.save({port: port});
                }
                event.sender.send('port-found', port);
            });
        } else {
            event.sender.send('port-found', server.address().port);
        }
    });
}