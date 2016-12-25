import React from 'react';
import ReactDOM from 'react-dom';
import MuiThemeProvider from 'material-ui/styles/MuiThemeProvider';
import darkBaseTheme from 'material-ui/styles/baseThemes/darkBaseTheme';
import getMuiTheme from 'material-ui/styles/getMuiTheme';
import injectTapEventPlugin from 'react-tap-event-plugin';
import {App} from './layout/App.js';

injectTapEventPlugin();

const MainApp = () => (
  <MuiThemeProvider muiTheme={muiTheme}>
    <App />
  </MuiThemeProvider>
);

ReactDOM.render(
  <MainApp />,
  document.getElementById('mainApp')
);

const muiTheme = getMuiTheme({
  palette: {
    primary1Color: '#00796B',
    primary2Color: '#009688',
    primary3Color: '#B2DFDB',
    accent1Color: '#FF5722',
    accent2Color: '#FF5722',
    accent3Color: '#FF5722',
    textColor: '#212121',
    alternateTextColor: '#757575',
    canvasColor: '#FFFFFF',
    borderColor: '#BDBDBD',
    disabledColor: '#212121',
    pickerHeaderColor: '#FF5722',
    clockCircleColor: '#212121',
    shadowColor: '#212121',
  }
});