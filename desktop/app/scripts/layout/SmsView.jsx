import React, { Component } from 'react';
import { appStyleSheet } from '../utils/Stylesheet.js';
import { Sidebar } from './Sidebar.jsx';
import { SmsMenuItem } from '../model/SmsMenuItem.jsx';
var {ipcRenderer} = window.require('electron');  // import it from window due to collision with browserify

export class SmsView extends Component {
    constructor(props) {
        super(props);
        this.state = {smsMenuList: []};
    }

    componentWillMount() {
        this.handleSmsList = this.handleSmsList.bind(this);
        
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
                        {this.state.smsMenuList}
                    </div>
                    <div style={{...appStyleSheet.content, ...appStyleSheet.fullheight}}>
                        <p style={appStyleSheet.centerMsg}>sms view</p>
                    </div>
                </div>
            </div>
        );
    }

    handleSmsList(smsList) {
        let smsListJson = JSON.parse(smsList);
        console.log(smsListJson);
        let smsMenuList = [];
        for (var addr in smsListJson) {
            if (!smsListJson.hasOwnProperty(addr)) {
                continue;
            }
            let currentValue = smsListJson[addr];
            let lastMessage = currentValue[currentValue.length - 1].message;
            smsMenuList.push(<SmsMenuItem icon="message" senderName={addr} message={lastMessage}/>);
        }
        console.log('smsMenuList ', smsMenuList);
        this.setState({...this.state, smsMenuList});
    }
}