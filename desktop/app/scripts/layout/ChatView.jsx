import React, { Component } from 'react';

export class ChatView extends Component {
    constructor(props) {
        super(props);
        console.log('ChatView created');
        //console.log('in chatview: ', props.smsThread[props.smsThread.length -1].message);
        this.state = {smsThread: props.smsThread};
        console.log('ChatView created!!!!!!');
    }

    componentWillMount() {
        this.getSmsThread = this.getSmsThread.bind(this);
        console.log('in chatview', this.state.smsThread);
    }

    render() {
        return(
            <div>Test chatview {this.getSmsThread()}</div>
        );
    }

    getSmsThread() {
        let thread = this.state.smsThread;
        if (thread && thread.length > 0) {
            return thread;
        }
        return [];
    }

}