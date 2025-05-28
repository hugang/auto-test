import { createTheme } from '@mui/material/styles';

const theme = createTheme({
  palette: {
    primary: {
      main: '#008756',
      dark: '#006942',
      light: '#33a97b',
      contrastText: '#fff',
      disabled: '#b6e5d0',
    },
    secondary: {
      main: '#00a758',
    },
    success: {
      main: '#41C27C',
    },
    warning: {
      main: '#FFA940',
    },
    error: {
      main: '#F5483B',
    },
    info: {
      main: '#1890FF',
    },
    text: {
      primary: '#333333',
      secondary: '#666666',
      disabled: '#BFBFBF',
    },
    background: {
      default: '#F7F8FA',
      paper: '#FFFFFF',
      card: '#FFFFFF',
      popup: '#FFFFFF',
      emphasis: '#E6FAF3',
    },
    divider: '#F0F0F0',
    border: '#E5E6EB',
    action: {
      hover: '#83f4c8',
      selected: '#00a758',
      disabled: '#BFBFBF',
      active: '#006942',
    },
  },
  typography: {
    allVariants: {
      color: '#222222',
    },
  },
  shape: {
    borderRadius: 8,
  },
});

export default theme;

