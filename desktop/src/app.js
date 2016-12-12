import {app, BrowserWindow} from 'electron';
import path from 'path';
import url from 'url';

var Toaster = require('electron-toaster');
var toaster = new Toaster();

let win

function createWindow () {
    win = new BrowserWindow({width: 1280, height: 720})
    toaster.init(win);

    win.loadURL(url.format({
        pathname: path.join(__dirname, '../app/index.html'),
        protocol: 'file:',
        slashes: true
    }))

    //win.webContents.openDevTools()
    
    win.on('closed', () => {
        win = null
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