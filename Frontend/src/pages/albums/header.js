import { AppBar, Button, Toolbar, Typography } from '@mui/material';
import { fetchDeleteDataWithAuth } from 'client/Client';
import { Link, useLocation } from 'react-router-dom';
// ==============================|| ALBUM HEADER ||============================== //

const Header = () => {
  const location = useLocation();
  const queryParams = new URLSearchParams(location.search);
  const id = queryParams.get('id');

  const handleDelete = () => {
    const isConfirmed = window.confirm('Are you sure you want to delete the album?');

    if (isConfirmed) {
      console.log('Item deleted!');
      fetchDeleteDataWithAuth('/albums/' + id + '/delete-album').then((res) => {
        console.log(res);
        window.location.href = '/';
      });
    } else {
      console.log('Delte operation cancelled!');
    }
  };

  return (
    <AppBar position="static">
      <Toolbar>
        <Typography variant="h6" component="div" sx={{ flexGrow: 1 }}>
          Photo Gallery
        </Typography>
        <Button
          component={Link}
          to={`/album/upload?id=${id}`}
          color="secondary"
          variant="contained"
          sx={{ mr: 2, backgroundColor: '#4CAF50', '&:hover': { backgroundColor: '#1976d2' } }}
        >
          Upload Photos
        </Button>

        <Button
          component={Link}
          to={`/album/edit?id=${id}`}
          color="secondary"
          variant="contained"
          sx={{ mr: 2, backgroundColor: '#FF9800', '&:hover': { backgroundColor: '#018786' } }}
        >
          Edit Album
        </Button>

        <Button
          onClick={handleDelete}
          variant="contained"
          sx={{ mr: 2, backgroundColor: '#DC143C', '&:hover': { backgroundColor: '#388e3c' } }}
        >
          Delete Album
        </Button>
      </Toolbar>
    </AppBar>
  );
};

export default Header;
