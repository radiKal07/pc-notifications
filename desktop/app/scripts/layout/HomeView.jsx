import React, { Component } from 'react';
import { appStyleSheet } from '../utils/Stylesheet.js';
import { Sidebar, layouts } from './Sidebar.jsx';

export class HomeView extends Component {
    constructor(props) {
        super(props);
    }

    render() {
        return(
            <div>
                <div style={{...appStyleSheet.staticFixed, ...appStyleSheet.fullheight}}>
                    <Sidebar layout={layouts.HOME} onSwitchLayout={(newLayout) => {this.props.onSwitchLayout(newLayout)}}/>
                </div>
                <div style={{...appStyleSheet.dinamic, ...appStyleSheet.fullheight}}>
                    <div style={{...appStyleSheet.fullContent, ...appStyleSheet.fullheight}}>
                        <p style={appStyleSheet.centerMsg}><i className="material-icons md-48">insert_emoticon</i></p>
                        <p style={appStyleSheet.centerMsg}>Connected</p>
                    </div>
                </div>
            </div>
        );
    }
}