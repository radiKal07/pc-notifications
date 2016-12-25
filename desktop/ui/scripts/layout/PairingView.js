import React, {Component} from 'react';
import FloatingActionButton from 'material-ui/FloatingActionButton';
import ContentAdd from 'material-ui/svg-icons/content/add';

export class PairingView extends Component {
    constructor(props) {
        super(props);
    }

    render() {
        return(<div>
            <FloatingActionButton secondary={true}>
                <ContentAdd />
            </FloatingActionButton>
        </div>);
    }
}

const style = {
    backgroundColor: '#FF5722',
    marginRight: 20,
};