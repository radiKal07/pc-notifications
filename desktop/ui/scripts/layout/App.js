import React, {Component} from 'react';
import RaisedButton from 'material-ui/RaisedButton';
import {PairingView} from './PairingView.js';

export class App extends Component {
    constructor(props) {
        super(props);
    }

    render() {
        return(
            <div>
                <div style={{...style.staticFixed, ...style.fullheight}}>
                    sidebar
                </div>
                <div style={{...style.dinamic, ...style.fullheight}}>
                    <div style={{...style.menu, ...style.fullheight}}>menu</div>
                    <div style={{...style.content, ...style.fullheight}}>
                        <PairingView style={style.pairButton}/>
                    </div>
                </div>
            </div>    
        );
    }
}

const style = {
    fullheight: {
        height: '100vh'    
    },
    staticFixed: {
        backgroundColor: '#00796B',
        width: '70px',
        float: 'left'
    },
    dinamic: {
        marginLeft: '70px'
    },
    menu: {
        backgroundColor: '#009688',
        width: '35%',
        float: 'left'
    },
    content: {
        backgroundColor: '#B2DFDB',
        width: '65%',
        float: 'right'
    },
    pairButton: {
        float: 'right',
        justifyContent: 'flex-end',
        alignSelf: 'flex-end',
        width: 300
    }
}
