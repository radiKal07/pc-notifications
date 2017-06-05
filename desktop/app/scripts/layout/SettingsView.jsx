import React, { Component } from 'react';
import { appStyleSheet } from '../utils/Stylesheet.js';
import { Sidebar, layouts } from './Sidebar.jsx';

export class SettingsView extends Component {
    constructor(props) {
        super(props);
    }

    render() {
        return(
            <div>                
                <div style={{...appStyleSheet.staticFixed, ...appStyleSheet.fullheight}}>
                    <Sidebar layout={layouts.SETTINGS} onSwitchLayout={(newLayout) => {this.props.onSwitchLayout(newLayout)}}/>
                </div>
                <div style={{...appStyleSheet.dinamic, ...appStyleSheet.fullheight}}>
                    <div style={{...appStyleSheet.fullContent, ...appStyleSheet.fullheight}}>
                        <p style={appStyleSheet.centerMsg}>PC-Notifications v0.0.1</p>
                    </div>
                </div>
            </div>
        );
    }
}