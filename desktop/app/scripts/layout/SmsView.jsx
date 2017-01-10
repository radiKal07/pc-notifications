import React, { Component } from 'react';
import { appStyleSheet } from '../utils/Stylesheet.js';
import { Sidebar } from './Sidebar.jsx';
import { SmsMenuItem } from '../model/SmsMenuItem.jsx';

export class SmsView extends Component {
    constructor(props) {
        super(props);
    }

    render() {
        return(
            <div>
                <div style={{...appStyleSheet.staticFixed, ...appStyleSheet.fullheight}}>
                    <Sidebar onSwitchLayout={(newLayout) => {this.props.onSwitchLayout(newLayout)}}/>
                </div>
                <div style={{...appStyleSheet.dinamic, ...appStyleSheet.fullheight}}>
                    <div style={{...appStyleSheet.menu, ...appStyleSheet.fullheight}}>
                        <SmsMenuItem icon="message" senderName="John Smith" message="hello this is test message"/>
                        <SmsMenuItem icon="message" senderName="John Smith" message="hello this is test message"/>
                        <SmsMenuItem icon="message" senderName="John Smith" message="hello this is test message"/>
                    </div>
                    <div style={{...appStyleSheet.content, ...appStyleSheet.fullheight}}>
                        <p style={appStyleSheet.centerMsg}>sms view</p>
                    </div>
                </div>
            </div>
        );
    }
}