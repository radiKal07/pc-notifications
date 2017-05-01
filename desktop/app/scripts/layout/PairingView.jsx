import React, {Component} from 'react';
import RaisedButton from 'material-ui/RaisedButton';
import Dialog from 'material-ui/Dialog';
import FlatButton from 'material-ui/FlatButton';
import {CustomActionButton} from '../utils/CustomActionButton.jsx';
import {appStyleSheet} from '../utils/Stylesheet.js';
var {ipcRenderer} = window.require('electron');  // import it from window due to collision with browserify

export class PairingView extends Component {
    constructor(props) {
        super(props);
        this.state = {pairDialogOpen: false, port: ''};
    }

    componentWillMount() {
        this.handlePairDevice = this.handlePairDevice.bind(this);
        this.handleClosePairDialog = this.handleClosePairDialog.bind(this);

        ipcRenderer.on('port_found', (event, port) => {
            this.setState({...this.state, port: port});
        });

        ipcRenderer.on('client_connected', (event) => {
            this.setState({...this.state, pairDialogOpen: false});
            this.props.onFinish();
        });
    }

    render() {
        const actions = [
            <FlatButton
                label="Cancel"
                primary={true}
                onTouchTap={this.handleClosePairDialog}
            />
        ];

        return(
            <div>
                <div style={{...appStyleSheet.staticFixed, ...appStyleSheet.fullheight}}>
                    sidebar
                </div>
                <div style={{...appStyleSheet.dinamic, ...appStyleSheet.fullheight}}>
                    <div style={{...appStyleSheet.fullContent, ...appStyleSheet.fullheight}}>
                        <p style={appStyleSheet.centerMsg}>Please pair device</p>
                        <CustomActionButton style={appStyleSheet.actionBtn} onTouchTap={this.handlePairDevice}/>
                        <Dialog
                                title="Pairing Mode"
                                actions={actions}
                                modal={false}
                                open={this.state.pairDialogOpen}
                                onRequestClose={this.handleClosePairDialog}
                            >
                            Open pairing mode on your phone and use this pairing code: {this.state.port}
                        </Dialog>
                    </div>
                </div>
            </div>
        );
    }

    handlePairDevice() {
        this.setState({...this.state, pairDialogOpen: true});
        ipcRenderer.send('retrieve_port');
    }

    handleClosePairDialog() {
        this.setState({...this.state, pairDialogOpen: false});
    }
}