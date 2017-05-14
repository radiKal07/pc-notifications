import React, { Component } from 'react';
import { ChatBubble } from './ChatBubble.jsx';
import TextField from 'material-ui/TextField';
import IconButton from 'material-ui/IconButton';
var {ipcRenderer} = window.require('electron');  // import it from window due to collision with browserify

// use this to add proper keys to messages even if the same message is sent multiple times
let gIndex = 0;

export class ChatView extends Component {
    constructor(props) {
        super(props);
        this.state = {smsThread: props.smsThread, smsMessage: '', showInput: props.showInput};
    }

    componentWillMount() {
        this.getSmsThread = this.getSmsThread.bind(this);
        this.generateChatBubbles = this.generateChatBubbles.bind(this);
        this.scrollToBottom = this.scrollToBottom.bind(this);
        this.sendSms = this.sendSms.bind(this);
        this.handleTextFieldChange = this.handleTextFieldChange.bind(this);
        this.generateUi = this.generateUi.bind(this);

        document.addEventListener("keydown", (event) => {
            if (event.keyCode == 13) {
                this.sendSms();
            }
        });

        ipcRenderer.on('send_sms_response', (event, response) => {
            if (response == 'OK') {
                console.log('send_sms_response - OK');
                let addrToSend = this.state.smsThread[0].contact;
                let sms = {message: this.state.smsMessage, contact: addrToSend, type: 0, date: new Date()}

                this.props.onNewSms(sms);
                this.setState({...this.state, smsMessage: ''});
            } else {
                console.log('send_sms_response - ERROR');
            }
        });
    }

    componentWillReceiveProps(nextProps) {
        this.generateChatBubbles(nextProps);
    }

    render() {
        return(
            <div style={style.content} ref={(div) => {this.chatBubbles = div;}}>
                {this.state.chatBubbles}
                {
                    this.state.showInput
                    &&
                    <div style={style.smsInput}>
                        <TextField style={style.textField} hintText="Type an SMS message" value={this.state.smsMessage} onChange={this.handleTextFieldChange}/>
                        <IconButton style={style.mediumIcon} onTouchTap={this.sendSms}>
                            <i className="material-icons md-48">send</i>
                        </IconButton>
                    </div>
                }
            </div>
        );
    }

    componentDidMount() {
        this.generateUi();
    }

    componentDidUpdate() {
        this.generateUi();
    }

    generateUi() {
        this.generateChatBubbles(this.state.smsThread);
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
            chatBubbles.push(<ChatBubble key={sms.message + index} message={sms.message} msgType={sms.type}/>);
        }
        this.setState({...this.state, smsThread: props.smsThread, chatBubbles, showInput: props.showInput});
    }

    scrollToBottom() {
        const scrollHeight = this.chatBubbles.scrollHeight;
        const height = this.chatBubbles.clientHeight;
        const maxScrollTop = scrollHeight - height;
        this.chatBubbles.scrollTop = maxScrollTop > 0 ? maxScrollTop : 0;
    }

    sendSms() {
        if (this.state.smsMessage == null || this.state.smsMessage == undefined || this.state.smsMessage === '' || this.state.smsMessage === "") {
            return;
        }
        let addrToSend = this.state.smsThread[0].contact;
        let sms = {message: this.state.smsMessage, contact: addrToSend}

        ipcRenderer.send('send_sms', sms);
    }

    handleTextFieldChange(event) {
        this.setState({...this.state, smsMessage: event.target.value});
    }
}

const style = {
    content: {
        overflow: 'auto',
        height: 'calc(100% - 100px)'
    },
    smsInput: {
        float: 'right',
        width: 'calc(65% - 45.5px)', // width of the 3rd column minus the sidebar (sidebar is 70px, 65% of 70px is 45.5px)
        backgroundColor: 'white',
        position: 'absolute',
        bottom: '0'
    },
    textField: {
        width: 'calc(100% - 116px)',
        marginLeft: '10px',
        marginTop: '10px'
    },
    mediumIcon: {
        width: '96px',
        height: '96px',
        marginRight: '10px',
    }
}