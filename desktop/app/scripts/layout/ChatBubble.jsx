import React, { Component } from 'react';
import { colors } from '../utils/Stylesheet.js';

export class ChatBubble extends Component {
    constructor(props) {
        super(props);
        this.state = {message: props.message, msgType: props.msgType};
    }

    componentWillMount() {
        this.getStyle = this.getStyle.bind(this);
    }

    render() {
        return(
            <div style={{...this.getStyle(this.state.msgType), ...style.common}}>
                {this.state.message}
            </div>
        );
    }

    getStyle(msgType) {
        if (msgType == 0) {
            return style.right;
        }
        return style.left;
    }
}

const style = {
    right: {
        backgroundColor: '#FFFFFF',
        float: 'right',
        alignSelf: 'flex-end',
        alignItems: 'flex-end',
        justifyContent: 'flex-end',
        marginRight: '10px',
    },
    left: {
        backgroundColor: colors.lightPrimary,
        float: 'left',
        marginLeft: '10px'
    },
    common: {
        display: 'block',
        padding: '15px',
        clear: 'both',
        borderRadius: '30px',
        marginTop: '4px',
        maxWidth: '50%'
    }
}