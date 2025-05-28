// PlaceHolder.jsx
// A simple placeholder React component
import Box from '@mui/material/Box';
import Typography from '@mui/material/Typography';

const PlaceHolder = ({ node, depth, text }) => {
  return (
    <Box
      className="placeholder-component"
      sx={{
        marginLeft: (depth || 0) * 3,
        px: 2,
        py: 1,
        bgcolor: 'primary.50',
        border: '1px dashed',
        borderColor: 'primary.200',
        borderRadius: 1,
        color: 'primary.600',
        fontStyle: 'italic',
        display: 'inline-block',
        boxShadow: '0 0 5px rgba(0, 0, 0, 0.05)',
        transition: 'all 0.2s ease',
        width: 'calc(100% - ' + ((depth || 0) * 3) + 'px - 16px)',
        maxWidth: '600px',
        animation: 'pulse 1.5s infinite ease-in-out',
        '@keyframes pulse': {
          '0%': { opacity: 0.7 },
          '50%': { opacity: 1 },
          '100%': { opacity: 0.7 },
        },
      }}
    >
      <Typography variant="body2" component="span">
        {text || '放置在这里...'}
      </Typography>
    </Box>
  );
};

export default PlaceHolder;
