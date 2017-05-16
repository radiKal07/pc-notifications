import React, { Component } from 'react';
import {List, ListItem, makeSelectable} from 'material-ui/List';
import Divider from 'material-ui/Divider';
import Subheader from 'material-ui/Subheader';
import Avatar from 'material-ui/Avatar';
import FontIcon from 'material-ui/FontIcon';
import CircularProgress from 'material-ui/CircularProgress';
import { appStyleSheet } from '../utils/Stylesheet.js';
import { Sidebar, layouts } from './Sidebar.jsx';
import { ChatView } from './ChatView.jsx';
import { colors } from '../utils/Stylesheet.js';
var {ipcRenderer} = window.require('electron');  // import it from window due to collision with browserify

let SelectableList = makeSelectable(List);

function wrapState(ComposedComponent) {
  return class SelectableList extends Component {
    static propTypes = {
      children: React.PropTypes.node.isRequired,
      defaultValue: React.PropTypes.number.isRequired,
    };

    componentWillMount() {
      this.setState({
        selectedIndex: this.props.defaultValue,
      });
    }

    handleRequestChange = (event, index) => {
      this.setState({
        selectedIndex: index,
      });
    };

    render() {
      return (
        <ComposedComponent
          value={this.state.selectedIndex}
          onChange={this.handleRequestChange}
        >
          {this.props.children}
        </ComposedComponent>
      );
    }
  };
}

SelectableList = wrapState(SelectableList);

export class SmsView extends Component {
    constructor(props) {
        super(props);
        this.state = {currentSmsThread: [], showInput: false, loading: true};
    }

    componentWillMount() {
        this.handleSmsList = this.handleSmsList.bind(this);
        this.handleSmsListClick = this.handleSmsListClick.bind(this);
        this.onNewSms = this.onNewSms.bind(this);
        this.generateSmsList = this.generateSmsList.bind(this);
    }

    render() {
        return(
            <div>
                <div style={{...appStyleSheet.staticFixed, ...appStyleSheet.fullheight}}>
                    <Sidebar layout={layouts.SMS} onSwitchLayout={(newLayout) => {this.props.onSwitchLayout(newLayout)}}/>
                </div>
                <div style={{...appStyleSheet.dinamic, ...appStyleSheet.fullheight}}>
                    <div style={{...appStyleSheet.menu, ...appStyleSheet.fullheight}}>
                        {
                            !this.state.loading
                            &&
                            <SelectableList defaultValue={1}>
                                <Subheader>Latest messages</Subheader>
                                {this.generateSmsList(this.state.smsThreads)}
                            </SelectableList>
                        }
                        {
                            this.state.loading
                            &&
                            <CircularProgress style={style.progress} color={colors.accent} />
                        }
                    </div>
                    <div style={{...appStyleSheet.content, ...appStyleSheet.fullheight}}>
                        {
                            !this.state.loading
                            &&
                            <ChatView smsThread={this.state.currentSmsThread} showInput={this.state.showInput} onNewSms={this.onNewSms}/>
                        }
                        {
                            this.state.loading
                            &&
                            <CircularProgress style={style.progress} color={colors.accent} size={80}/>
                        }
                    </div>
                </div>
            </div>
        );
    }

    componentDidMount() {
        ipcRenderer.on('sms_list_response', (event, smsList) => {
            this.handleSmsList(smsList);
        });

        ipcRenderer.on('new_sms', (event, sms) => {
            console.log('new_sms - ', sms);
            this.onNewSms(sms);
        });

        ipcRenderer.send('retrieve_sms');
    }

    handleSmsList(smsThreads) {
        let smsThreadsJson = JSON.parse(smsThreads);
        this.setState({...this.state, smsThreads: smsThreadsJson, loading: false});
        this.handleSmsListClick(smsThreadsJson[Object.keys(smsThreadsJson)[0]]);
    }

    generateSmsList(smsThreads) {
        if (smsThreads == null || smsThreads == undefined) {
            return;
        }
        let smsList = [];
        let index = 1;
        for (var addr in smsThreads) {
            if (!smsThreads.hasOwnProperty(addr)) {
                continue;
            }
            let currentValue = smsThreads[addr];
            let lastMessage = currentValue[currentValue.length - 1];

            let fancyEl = <ListItem
                            value={index++}
                            key={addr}
                            leftAvatar={<Avatar icon={<FontIcon className="material-icons md-48">message</FontIcon>} />}
                            primaryText={lastMessage.contact.displayName}
                            secondaryText={
                                <p>
                                {lastMessage.message}
                                </p>
                            }
                            onTouchTap={this.handleSmsListClick.bind(this, currentValue)}
                            />

            smsList.push(fancyEl);
            smsList.push(<Divider key={addr + "_div"} inset={true} />);
        }
        return smsList;
    }

    handleSmsListClick(smsList) {
        this.setState({...this.state, currentSmsThread: smsList, showInput: true});
    }

    onNewSms(sms) {
        let smsThreads = this.state.smsThreads;
        let conversation = smsThreads[sms.contact.displayName];
        if (conversation == undefined || conversation == null) {
            smsThreads[sms.contact.displayName] = [];
            conversation = smsThreads[sms.contact.displayName];
        }
        conversation.push(sms);

        this.setState({...this.state, smsThreads});
    }
}

const style = {
    progress: {
        position: 'relative',
        top: '50%',
        transform: 'translateY(-50%)',
        display: 'flex',
        justifyContent: 'center',
        width: '100%'
    }
};