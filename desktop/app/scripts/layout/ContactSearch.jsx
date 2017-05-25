import React, { Component } from 'react';
import SelectField from 'material-ui/SelectField';
import MenuItem from 'material-ui/MenuItem';
import FlatButton from 'material-ui/FlatButton';

export class ContactSearch extends Component {
    constructor(props) {
        super(props);
        this.state = { value: null, contacts: props.contacts, items: []};
    }

    componentWillMount() {
        this.handleChange = this.handleChange.bind(this);
        this.handleNewSms = this.handleNewSms.bind(this);
    }

    render() {
        return (
            <div style={style.container}>
                <SelectField
                    style={style.searchContact}
                    floatingLabelText="Search for a contact"
                    value={this.state.value}
                    onChange={this.handleChange}
                    maxHeight={200}
                >
                    {this.state.items}
                </SelectField>
                
                <FlatButton onTouchTap={this.handleNewSms} style={style.newButton} label="New" secondary={true} />
            </div>
        );
    }

    componentWillReceiveProps(nextProps) {
        let  items = [];
        items.push(<MenuItem value={null} primaryText="" />);
        for (let i = 0; i < nextProps.contacts.length; i++ ) {
            items.push(<MenuItem value={i} key={i} primaryText={nextProps.contacts[i].displayName} />);
        }
        this.setState({...this.state, contacts: nextProps.contacts, items});
    }

    handleChange(event, index, value) {
        this.setState({value});
    };

    handleNewSms() {
        let contact = this.state.contacts[this.state.value];
        this.props.onNewSmsToContact(contact);
    }
}


const style = {
    searchContact: {
        width: '80%',
        marginLeft: '30px'
    },
    newButton: {
        float: 'right'
    },
    container: {
        display: 'flex',
        alignItems: 'center'
    }
};