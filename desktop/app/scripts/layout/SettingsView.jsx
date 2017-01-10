import React, { Component } from 'react';
import { appStyleSheet } from '../utils/Stylesheet.js';
import { Sidebar } from './Sidebar.jsx';

export class SettingsView extends Component {
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
                    <div style={{...appStyleSheet.fullContent, ...appStyleSheet.fullheight}}>
                        <p style={appStyleSheet.centerMsg}>Settings view</p>
                    </div>
                </div>
            </div>
        );
    }
}