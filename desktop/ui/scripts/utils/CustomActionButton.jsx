import React, {Component} from 'react';
import FloatingActionButton from 'material-ui/FloatingActionButton';
import ContentAdd from 'material-ui/svg-icons/content/add';

export class CustomActionButton extends Component {
    constructor(props) {
        super(props);
    }
    
    render() {
        return(
            <div>
                <FloatingActionButton style={this.props.style} onTouchTap={this.props.onTouchTap} secondary={true}>
                    <ContentAdd/>
                </FloatingActionButton>
            </div>
        );
    }
}