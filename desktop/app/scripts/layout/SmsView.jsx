import React, { Component } from 'react';
import { appStyleSheet } from '../utils/Stylesheet.js';
import { Sidebar } from './Sidebar.jsx';
import { SmsMenuItem } from '../model/SmsMenuItem.jsx';
import { ChatView } from './ChatView.jsx';
var {ipcRenderer} = window.require('electron');  // import it from window due to collision with browserify

export class SmsView extends Component {
    constructor(props) {
        super(props);
        this.state = {currentSmsThread: []};
    }

    componentWillMount() {
        this.handleSmsList = this.handleSmsList.bind(this);
        this.handleSmsListClick = this.handleSmsListClick.bind(this);
        this.onNewSms = this.onNewSms.bind(this);
        this.generateSmsList = this.generateSmsList.bind(this);
    }

    render() {
        return(
            <div>
                <div style={{...appStyleSheet.staticFixed, ...appStyleSheet.fullheight}}>
                    <Sidebar onSwitchLayout={(newLayout) => {this.props.onSwitchLayout(newLayout)}}/>
                </div>
                <div style={{...appStyleSheet.dinamic, ...appStyleSheet.fullheight}}>
                    <div style={{...appStyleSheet.menu, ...appStyleSheet.fullheight}}>
                        {this.generateSmsList(this.state.smsThreads)}
                    </div>
                    <div style={{...appStyleSheet.content, ...appStyleSheet.fullheight}}>
                        <ChatView smsThread={this.state.currentSmsThread} onNewSms={this.onNewSms}/>
                    </div>
                </div>
            </div>
        );
    }

    componentDidMount() {
        ipcRenderer.on('sms_list_response', (event, smsList) => {
            this.handleSmsList(smsList);
        });

        ipcRenderer.on('new_sms', (event, sms) => {
            console.log('new_sms - ', sms);
            this.onNewSms(sms);
        });

        ipcRenderer.send('retrieve_sms');
    }

    handleSmsList(smsThreads) {
        this.setState({...this.state, smsThreads: JSON.parse(smsThreads)});
    }

    generateSmsList(smsThreads) {
        if (smsThreads == null || smsThreads == undefined) {
            return;
        }
        let smsList = [];
        for (var addr in smsThreads) {
            if (!smsThreads.hasOwnProperty(addr)) {
                continue;
            }
            let currentValue = smsThreads[addr];
            let lastMessage = currentValue[currentValue.length - 1];
            let el = <SmsMenuItem key={addr} icon="message" senderName={lastMessage.contact.displayName} message={lastMessage.message} onSmsMenuItemSelected={this.handleSmsListClick.bind(this, currentValue)}/>;
            smsList.push(el);
        }
        return smsList;
    }

    handleSmsListClick(smsList) {
        this.setState({...this.state, currentSmsThread: smsList});
    }

    onNewSms(sms) {
        let smsThreads = this.state.smsThreads;
        let conversation = smsThreads[sms.contact.displayName];
        if (conversation == undefined || conversation == null) {
            smsThreads[sms.contact.displayName] = [];
            conversation = smsThreads[sms.contact.displayName];
        }
        conversation.push(sms);

        this.setState({...this.state, smsThreads});
    }
}