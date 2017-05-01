import { app, BrowserWindow } from 'electron';
import path from 'path';
import url from 'url';
import datastore from 'nedb-promise';
import { Server } from './Server.js';
import { ipcMain } from 'electron';
import {SettingsDao} from './SettingsDao.js'

let win

let settingsStore = datastore({filename: app.getPath('userData') + '/settings.json', autoload: true})
let settingsDao = new SettingsDao({settingsStore});
let server = new Server(settingsDao);

function createWindow() {
        win = new BrowserWindow({ width: 1280, height: 720 })

        win.loadURL(url.format({
            pathname: path.join(__dirname, '../../index.html'),
            protocol: 'file:',
            slashes: true
        }))

        win.on('closed', () => {
            win = null
        })

        server.startServer();   
        server.setOnClientConnected(() => {
            win.webContents.send('client_connected');
        }); 

        server.setOnSmsListRecevied((smsList) => {
            win.webContents.send('sms_list_response', smsList);
        });

        ipcMain.on('retrieve_port', async (event, arg) => {
            let port = await server.getPort();
            event.sender.send('port_found', port);
        });

        ipcMain.on('retrieve_sms', async (event, arg) => {
            server.getSmsList();
        })
    }

app.on('ready', createWindow)

app.on('window-all-closed', () => {
    // On macOS it is common for applications and their menu bar
    // to stay active until the user quits explicitly with Cmd + Q
    if (process.platform !== 'darwin') {
        app.quit()
    }
})

app.on('activate', () => {
    // On macOS it's common to re-create a window in the app when the
    // dock icon is clicked and there are no other windows open.
    if (win === null) {
        createWindow()
    }
})