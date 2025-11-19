// material-ui
import { Typography } from '@mui/material';
import MainCard from 'components/MainCard';

const AlbumsPage = () => {

  return(<MainCard title="Photo Album React App">
    <Typography variant="body2">
        This project is a photo album management web application built with React (frontend) and Spring Boot (backend). Users can create albums, upload photos, view, download, and delete images, all with secure authentication.
      <Typography variant="h5" gutterBottom>
        Features
      </Typography>
      <ul>
        <li>User authentication (JWT-based)</li>
        <li>Create, edit, and delete albums</li>
        <li>Upload, view, download, and delete photos</li>
        <li>Responsive Material UI dashboard</li>
        <li>REST API integration with Spring Boot backend</li>
      </ul>
    </Typography>
  </MainCard>
)};

export default AlbumsPage;
