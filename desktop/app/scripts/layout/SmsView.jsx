import React, { Component } from 'react';
import { appStyleSheet } from '../utils/Stylesheet.js';
import { Sidebar } from './Sidebar.jsx';
import { SmsMenuItem } from '../model/SmsMenuItem.jsx';
import { ChatView } from './ChatView.jsx';
var {ipcRenderer} = window.require('electron');  // import it from window due to collision with browserify

export class SmsView extends Component {
    constructor(props) {
        super(props);
        this.state = {smsList: []};
    }

    componentWillMount() {
        this.handleSmsList = this.handleSmsList.bind(this);
        this.handleSmsListClick = this.handleSmsListClick.bind(this);
        
        ipcRenderer.on('sms_list_response', (event, smsList) => {
            this.handleSmsList(smsList);
        });
        
        ipcRenderer.send('retrieve_sms');
    }

    render() {
        return(
            <div>
                <div style={{...appStyleSheet.staticFixed, ...appStyleSheet.fullheight}}>
                    <Sidebar onSwitchLayout={(newLayout) => {this.props.onSwitchLayout(newLayout)}}/>
                </div>
                <div style={{...appStyleSheet.dinamic, ...appStyleSheet.fullheight}}>
                    <div style={{...appStyleSheet.menu, ...appStyleSheet.fullheight}}>
                        {this.state.smsList}
                    </div>
                    <div style={{...appStyleSheet.content, ...appStyleSheet.fullheight}}>
                        {
                            this.state.currentSmsThread
                            &&
                            <ChatView smsThread={this.state.currentSmsThread}/>
                        }
                    </div>
                </div>
            </div>
        );
    }

    handleSmsList(smsThreads) {
        let smsThreadsJson = JSON.parse(smsThreads);
        let smsList = [];
        for (var addr in smsThreadsJson) {
            if (!smsThreadsJson.hasOwnProperty(addr)) {
                continue;
            }
            let currentValue = smsThreadsJson[addr];
            let lastMessage = currentValue[currentValue.length - 1].message;
            let el = <SmsMenuItem key={addr} icon="message" senderName={addr} message={lastMessage} onSmsMenuItemSelected={this.handleSmsListClick.bind(this, currentValue)}/>;
            smsList.push(el);
        }
        this.setState({...this.state, smsList});
    }

    handleSmsListClick(smsList) {
        let rand = Math.random() * (10 - 1) + 1;
        this.setState({...this.state, currentSmsThread: smsList, rand});
    }
}