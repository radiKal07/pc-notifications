import React, { Component } from 'react';

export class SmsMenuItem extends Component {
    constructor(props) {
        super(props);
    }

    render() {
        return(
            <div style={style.item}>
                <div style={style.icon}>
                    <i style="material-icons">{this.props.icon}</i>
                </div>
                <div style={style.name}>
                    {this.props.name}
                </div>
                <div style={style.content}>
                    {this.props.content}
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
    name: {
        marginLeft: '20%'
    },
    content: {
        marginLeft: '20%'
    }
}