import React, { Component } from 'react';

export class ChatBubble extends Component {
    constructor(props) {
        super(props);
        console.log('bubble: ', props);
        this.state = {message: props.message, msgType: props.msgType};
    }

    render() {
        return(
            <div>
                {
                    this.state.msgType == 0
                    &&
                    <div style={style.left}>
                        {this.state.message}
                    </div>
                }

                {
                    this.state.msgType == 1
                    &&
                    <div style={style.right}>
                        {this.state.message}
                    </div>
                }
            </div>
        );
    }
}

const style = {
    left: {
        color: 'blue'
    },
    right: {
        color: 'red'
    }
}