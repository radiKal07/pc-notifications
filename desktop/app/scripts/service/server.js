import http from 'http';
import IO from 'socket.io';
import ip from 'ip';
import os from 'os';
import dgram from 'dgram';
import { ipcMain } from 'electron';
import notifier from 'node-notifier';

export const startServer = async (win, settingsDao) => {
    let server = http.createServer();
    let io = new IO(server);

    io.on('connection', async (socket) => {
        console.log('connection fired');

        socket.on('server_discovery', async (clientIp, callback) => {
            console.log('Client trying to connect', clientIp);
            await settingsDao.saveClientIp(clientIp);
            win.webContents.send('client_connected');
            callback(os.hostname());
        });

        socket.on('notification_posted', function (data) {
            let notification = JSON.parse(data);
            console.log('received notification: ', data);
            notifier.notify({
                'title': notification.title,
                'message': notification.text
            });
        });

        socket.on('disconnect', async () => {
            console.log('Client disconnected');
        });

        win.webContents.send('client_connected');
    });

    let port = await settingsDao.getPort();
    if (!port) {
        port = 0;
    }

    server.listen(port, ip.address(), async () => {
        if (port == 0) {
            port = server.address().port;
            await settingsDao.savePort(port);
        }
        console.log('Socket.IO listening (' + server.address().address + ':' + server.address().port + ')');
        
        let clientIp = await settingsDao.getClientIp();
        if (port && clientIp) {
            sendWakeUpSignal(clientIp, port);
        }

        // TODO after server started successfully send event to UI that everything is ready
        // meanwhile UI can display a loading dialog
        // this way the user can't start pairing unless the server started with success
    });

    ipcMain.on('retrieve-port', async (event, arg) => {
        if (!port) {
            console.log('port is not setted');
            port = server.address().port;
            await settingsDao.savePort(port);
        }
        event.sender.send('port-found', port);
    });
};

const sendWakeUpSignal = (clientIp, port) => {
    console.log('Sending wake up signal ', clientIp, port);
    var message = new Buffer('wake');
    var client = dgram.createSocket('udp4');
    client.bind(port);

    client.on("message", function (msg, rinfo) {
        console.log("Got UDP package: " + msg + " from " +
            rinfo.address + ":" + rinfo.port);

    });

    client.on("listening", function () {
        var address = client.address();
        console.log("UDP listening " + address.address + ":" + address.port);
    });

    client.send(message, 0, message.length, port, clientIp, (error, bytes) => {
        if (error) {
            console.log('UDP error: ', error, bytes);
        }
    });
};