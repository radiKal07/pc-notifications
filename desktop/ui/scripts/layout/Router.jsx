import React, { Component } from 'react';
import Snackbar from 'material-ui/Snackbar';
import { PairingView } from './PairingView.jsx';
import { HomeView } from './HomeView.jsx';
import { SmsView } from './SmsView.jsx';
import { SettingsView } from './SettingsView.jsx';

export class Router extends Component {
    constructor(props) {
        super(props);
        this.state = { layout: layouts.PAIRING, paired: false, snackbarOpen: false, snackbarMsg: ''};
    }

    componentWillMount() {
    }

    render() {
        return (
            <div>
                {
                    this.state.layout == layouts.PAIRING
                    &&
                    <PairingView onFinish={(port) => { 
                                this.setState({...this.state, layout: layouts.HOME, snackbarOpen: true, snackbarMsg: 'Connection successfully'}) 
                            } 
                        } 
                    />
                }

                {
                    this.state.layout == layouts.HOME
                    &&
                    <HomeView/>
                }

                {
                    this.state.layout == layouts.SMS
                    &&
                    <SmsView/>
                }

                {
                    this.state.layout == layouts.SETTINGS
                    &&
                    <SettingsView/>
                }
                <Snackbar
                    open={this.state.snackbarOpen}
                    message={this.state.snackbarMsg}
                    autoHideDuration={4000}
                    onRequestClose={() => {
                            this.setState({ ...this.state, snackbarOpen: false });
                        }
                    }
                />
            </div>
        );
    }
}

const layouts = {
    PAIRING: 'PAIRING',
    HOME: 'HOME',
    SMS: 'SMS',
    SETTINGS: 'SETTINGS'
}; 