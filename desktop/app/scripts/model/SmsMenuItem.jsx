import React, { Component } from 'react';

export class SmsMenuItem extends Component {
    constructor(props) {
        super(props);
    }

    render() {
        return(
            <div style={style.item}>
                <div style={style.icon}>
                    <i className="material-icons md-48">{this.props.icon}</i>
                </div>
                <div style={style.senderName}>
                    {this.props.senderName}
                </div>
                <div style={style.message}>
                    {this.props.message}
                </div>
            </div>
        );
    }
}

const style = {
    item: {
        padding: '5%'
    },
    icon: {
        float: 'left',
        fontSize: '300%' // replace with icon size
    },
    senderName: {
        marginLeft: '20%'
    },
    message: {
        marginLeft: '20%'
    }
}