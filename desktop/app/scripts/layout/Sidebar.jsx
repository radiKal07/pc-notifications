import React, { Component } from 'react';

export class Sidebar extends Component {
    constructor(props) {
        super(props);
    }

    render() {
        return(
            <div>
                <div style={style.element} onTouchTap={this.handleTouchTap.bind(this, layouts.HOME)}>
                    <i className="material-icons md-48">account_circle</i>
                </div>
                <div style={style.element} onTouchTap={this.handleTouchTap.bind(this, layouts.SMS)}>
                    <i className="material-icons md-48">textsms</i>
                </div>
                <div style={style.element} onTouchTap={this.handleTouchTap.bind(this, layouts.SETTINGS)}>
                    <i className="material-icons md-48">settings</i>
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
        paddingTop: '40%',
        paddingBottom: '40%',
        fontSize: '300%' // TODO replace font size with icons
    }
}