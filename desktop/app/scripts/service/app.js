import { app, BrowserWindow } from 'electron';
import path from 'path';
import url from 'url';
import datastore from 'nedb-promise';
import { startServer } from './server.js';
import {SettingsDao} from './SettingsDao.js'

let win

let settingsStore = datastore({filename: app.getPath('userData') + '/settings.json', autoload: true})
var settingsDao = new SettingsDao({settingsStore});

function createWindow() {
        win = new BrowserWindow({ width: 1280, height: 720 })

        win.loadURL(url.format({
            pathname: path.join(__dirname, '../../index.html'),
            protocol: 'file:',
            slashes: true
        }))

        //win.webContents.openDevTools()

        win.on('closed', () => {
            win = null
        })

        startServer(win, settingsDao);
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