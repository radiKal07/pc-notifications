import http from 'http';
import IO from 'socket.io';
import ip from 'ip';
import os from 'os';
import dgram from 'dgram';
import notifier from 'node-notifier';

export class Server {
    constructor(settingsDao) {
        this.settingsDao = settingsDao;
        this.server = null;
        this.onClientConnected = null;
        this.onSmsListRecevied = null;
        this.socket = null;
    }

    async startServer() {
        this.server = http.createServer();
        let io = new IO(this.server);

        io.on('connection', async (socket) => {
            console.log('connection fired');
            this.socket = socket;
            this.addSocketEvents();
            if (this.onClientConnected) {
                this.onClientConnected();
            }
        });

        let port = await this.settingsDao.getPort();
        if (!port) {
            port = 0;
        }

        this.server.listen(port, ip.address(), async () => {
            if (port == 0) {
                port = this.server.address().port;
                await this.settingsDao.savePort(port);
            }
            console.log('Socket.IO listening (' + this.server.address().address + ':' + this.server.address().port + ')');
            
            let clientIp = await this.settingsDao.getClientIp();
            if (port && clientIp) {
                this.sendWakeUpSignal(clientIp, port);
            }

            // TODO after server started successfully send event to UI that everything is ready
            // meanwhile UI can display a loading dialog
            // this way the user can't start pairing unless the server started with success
        });
    }

    async sendWakeUpSignal(clientIp, port) {
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
    }

    async addSocketEvents() {
        this.socket.on('server_discovery', async (clientIp, callback) => {
            console.log('Client trying to connect', clientIp);
            await this.settingsDao.saveClientIp(clientIp);
            callback(os.hostname());
            if (this.onClientConnected) {
                this.onClientConnected();
            }
        });

        this.socket.on('notification_posted', (data) => {
            let notification = JSON.parse(data);
            console.log('received notification: ', data);
            
            notifier.notify({
                'title': notification.title,
                'message': notification.text
            });
        });

        this.socket.on('new_sms', (smsJsonAsString) => {
            console.log('new_sms - ', smsJsonAsString);
            this.onNewSmsReceived(JSON.parse(smsJsonAsString));
        });
    }

    async getPort() {
        let port = await this.settingsDao.getPort();
        if (!port || port != this.server.address().port) {
            console.log('port is not setted');
            port = this.server.address().port;
            await this.settingsDao.savePort(port);
        }
        return port;
    }

    setOnClientConnected(callback) {
        this.onClientConnected = callback;
    }

    setOnSmsListRecevied(callback) {
        this.onSmsListRecevied = callback;
    }

    setOnNewSmsReceived(callback) {
        this.onNewSmsReceived = callback;
    }

    getSmsList() {
        console.log('get_all_sms');
        this.socket.emit('get_all_sms', async (smsList) => {
            this.onSmsListRecevied(smsList);
        });
    }

    sendSms(sms, callback) {
        this.socket.emit('send_sms', JSON.stringify(sms), (response) => {
             console.log('send_sms - response - ', response);
             callback(response);
        });
    }
}
