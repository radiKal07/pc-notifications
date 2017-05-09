import React, { Component } from 'react';
import { ChatBubble } from './ChatBubble.jsx';
import TextField from 'material-ui/TextField';
import IconButton from 'material-ui/IconButton';

export class ChatView extends Component {
    constructor(props) {
        super(props);
        this.state = {smsThread: props.smsThread};
    }

    componentWillMount() {
        this.getSmsThread = this.getSmsThread.bind(this);
        this.generateChatBubbles = this.generateChatBubbles.bind(this);
        this.scrollToBottom = this.scrollToBottom.bind(this);
        this.generateChatBubbles(this.state.smsThread);
    }

    componentWillReceiveProps(nextProps) {
        this.generateChatBubbles(nextProps);
    }

    render() {
        return(
            <div style={style.content} ref={(div) => {this.chatBubbles = div;}}>
                {this.state.chatBubbles}
                <div style={style.smsInput}>
                    <TextField style={style.textField} hintText="Type an SMS message" />
                    <IconButton style={style.mediumIcon}>
                        <i className="material-icons md-48">send</i>
                    </IconButton>
                </div>
            </div>
        );
    }

    componentDidUpdate() {
        this.scrollToBottom();
    }

    getSmsThread(thread) {
        if (thread && thread.length > 0) {
            return thread;
        }
        return [];
    }

    generateChatBubbles(props) {
        if (this.state.smsThread == props) {
            return;
        }


        let chatBubbles = [];
        let index = 0;
        let thread = this.getSmsThread(props.smsThread);
        while (index < thread.length) {
            let sms = thread[index++];
            chatBubbles.push(<ChatBubble key={sms.message} message={sms.message} msgType={sms.type}/>);
        }
        this.setState({...this.state, chatBubbles});
    }

    scrollToBottom() {
        const scrollHeight = this.chatBubbles.scrollHeight;
        const height = this.chatBubbles.clientHeight;
        const maxScrollTop = scrollHeight - height;
        this.chatBubbles.scrollTop = maxScrollTop > 0 ? maxScrollTop : 0;
    }
}

const style = {
    content: {
        overflow: 'auto',
        height: '100%'
    },
    smsInput: {
        marginTop: '10px',
        float: 'right',
        width: '100%',
        backgroundColor: 'white'
    },
    textField: {
        marginLeft: '10px'
    },
    mediumIcon: {
        width: '96px',
        height: '96px',
        marginRight: '10px',
    }
}