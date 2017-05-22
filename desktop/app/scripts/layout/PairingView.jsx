import React, {Component} from 'react';
import RaisedButton from 'material-ui/RaisedButton';
import Dialog from 'material-ui/Dialog';
import FlatButton from 'material-ui/FlatButton';
import QRCode from 'qrcode.react';
import {CustomActionButton} from '../utils/CustomActionButton.jsx';
import {appStyleSheet} from '../utils/Stylesheet.js';
var {ipcRenderer} = window.require('electron');  // import it from window due to collision with browserify

export class PairingView extends Component {
    constructor(props) {
        super(props);
        this.state = {pairDialogOpen: false, qrCodeValue: 'test'};
    }

    componentWillMount() {
        this.handlePairDevice = this.handlePairDevice.bind(this);
        this.handleClosePairDialog = this.handleClosePairDialog.bind(this);

        ipcRenderer.on('qrcode_found', (event, qrCodeValue) => {
            this.setState({...this.state, qrCodeValue});
        });

        ipcRenderer.on('client_connected', (event) => {
            console.log('ipcRenderer - client_connected');
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
                            <QRCode value={this.state.qrCodeValue} />
                        </Dialog>
                    </div>
                </div>
            </div>
        );
    }

    handlePairDevice() {
        this.setState({...this.state, pairDialogOpen: true});
        ipcRenderer.send('retrieve_qrcode');
    }

    handleClosePairDialog() {
        this.setState({...this.state, pairDialogOpen: false});
    }
}