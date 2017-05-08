import React, { Component } from 'react';
import { ChatBubble } from './ChatBubble.jsx';

export class ChatView extends Component {
    constructor(props) {
        super(props);
        this.state = {smsThread: props.smsThread};
    }

    componentWillMount() {
        this.getSmsThread = this.getSmsThread.bind(this);
    }

    componentWillReceiveProps(nextProps) {
        console.log('nici asta???; ', nextProps);
        if (this.state.smsThread == nextProps) {
            console.log('the same');
            return;
        }

        console.log('in chatview', this.state.smsThread);

        let chatBubbles = [];
        let index = 0;
        let thread = this.getSmsThread(nextProps.smsThread);
        while (index < thread.length) {
            let sms = thread[index++];
            console.log('sms: ', sms);
            let msgType = sms.type;
            console.log('msgTYpe: ', msgType);
            chatBubbles.push(<ChatBubble key={sms.message} message={sms.message} msgType={msgType}/>);
        }
        console.log('catbubbles populated: ', chatBubbles)
        this.setState({...this.state, chatBubbles});
    }

    render() {
        return(
            <div>{this.state.chatBubbles}</div>
        );
    }

    getSmsThread(thread) {
        if (thread && thread.length > 0) {
            console.log('return: ', thread);
            return thread;
        }
        console.log('return empty');
        return [];
    }
}