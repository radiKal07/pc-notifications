import React, { Component } from 'react';
import { colors } from '../utils/Stylesheet.js';

export class Sidebar extends Component {
    constructor(props) {
        super(props);
        this.state = {layout: props.layout};
    }

    render() {
        return(
            <div>
                <div style={this.state.layout == layouts.HOME ? style.selectedElement : style.element} onTouchTap={this.handleTouchTap.bind(this, layouts.HOME)}>
                    <i className="material-icons md-48 md-light">account_circle</i>
                </div>
                <div style={this.state.layout == layouts.SMS ? style.selectedElement : style.element} onTouchTap={this.handleTouchTap.bind(this, layouts.SMS)}>
                    <i className="material-icons md-48 md-light">textsms</i>
                </div>
                <div style={this.state.layout == layouts.SETTINGS ? style.selectedElement : style.element} onTouchTap={this.handleTouchTap.bind(this, layouts.SETTINGS)}>
                    <i className="material-icons md-48 md-light">settings</i>
                </div>
            </div>
        );
    }

    handleTouchTap(layout) {
        this.props.onSwitchLayout(layout);
    }
}

export const layouts = {
    PAIRING: 'PAIRING',
    HOME: 'HOME',
    SMS: 'SMS',
    SETTINGS: 'SETTINGS'
};

const style = {
    element: {
        position: 'relative',
        display: 'flex',
        justifyContent: 'center',
        marginTop: '40%',
        marginBottom: '40%',
        cursor: 'pointer',
        fontSize: '300%' // TODO replace font size with icons
    },
    selectedElement: {
        position: 'relative',
        display: 'flex',
        justifyContent: 'center',
        marginTop: '40%',
        marginBottom: '40%',
        backgroundColor: colors.accent,
        cursor: 'pointer',
        fontSize: '300%' // TODO replace font size with icons
    }
}