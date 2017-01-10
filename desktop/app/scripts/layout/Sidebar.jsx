import React, { Component } from 'react';

export class Sidebar extends Component {
    constructor(props) {
        super(props);
    }

    render() {
        return(
            <div>
                <div style={style.element} onTouchTap={() => {this.props.onSwitchLayout(layouts.HOME)}}>H</div>
                <div style={style.element} onTouchTap={() => {this.props.onSwitchLayout(layouts.SMS)}}>S</div>
                <div style={style.element} onTouchTap={() => {this.props.onSwitchLayout(layouts.SETTINGS)}}>Se</div>
            </div>
        );
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