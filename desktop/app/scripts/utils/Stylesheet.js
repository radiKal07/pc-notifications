export const colors = {
    darkPrimary: '#303F9F',
    defaultPrimary: '#3F51B5',
    lightPrimary: '#C5CAE9',
    accent: '#FF4081',
    primaryText: '#212121',
    secondaryText: '#757575',
    divider: '#BDBDBD',
}

export const appStyleSheet = {
    fullheight: {
        overflow: 'auto',
        height: '100vh'
    },
    staticFixed: {
        backgroundColor: colors.darkPrimary,
        width: '70px',
        float: 'left'
    },
    dinamic: {
        marginLeft: '70px'
    },
    menu: {
        backgroundColor: '#FFFFFF',
        width: '35%',
        float: 'left'
    },
    content: {
        backgroundColor: '#F4F4F4',
        width: '65%',
        float: 'right'
    },
    fullContent: {
        backgroundColor: '#F4F4F4',
        width: '100%',
        float: 'right'
    },
    actionBtn: {
        position: 'absolute',
        right: '5vh',
        bottom: '5vh'
    },
    centerMsg: {
        position: 'relative',
        top: '50%',
        transform: 'translateY(-50%)',
        display: 'flex',
        justifyContent: 'center'
    }
}