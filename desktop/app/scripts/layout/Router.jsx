import React, { Component } from 'react';
import Snackbar from 'material-ui/Snackbar';
import { PairingView } from './PairingView.jsx';
import { HomeView } from './HomeView.jsx';
import { SmsView } from './SmsView.jsx';
import { SettingsView } from './SettingsView.jsx';
import { layouts } from './Sidebar.jsx';

export class Router extends Component {
    constructor(props) {
        super(props);
        this.state = { layout: layouts.PAIRING, paired: false, snackbarOpen: false, snackbarMsg: ''};
    }

    componentWillMount() {
        this.onSwitchLayout = this.onSwitchLayout.bind(this);
    }

    render() {
        return (
            <div>
                {
                    this.state.layout == layouts.PAIRING
                    &&
                    <PairingView onFinish={(port) => { 
                                this.setState({...this.state, layout: layouts.HOME, snackbarOpen: true, snackbarMsg: 'Connected successfully'}) 
                            } 
                        } 
                    />
                }

                {
                    this.state.layout == layouts.HOME
                    &&
                    <HomeView onSwitchLayout={this.onSwitchLayout}/>
                }

                {
                    this.state.layout == layouts.SMS
                    &&
                    <SmsView onSwitchLayout={this.onSwitchLayout}/>
                }

                {
                    this.state.layout == layouts.SETTINGS
                    &&
                    <SettingsView onSwitchLayout={this.onSwitchLayout}/>
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

    onSwitchLayout(newLayout) {
        this.setState({...this.state, layout: newLayout});
    }
}