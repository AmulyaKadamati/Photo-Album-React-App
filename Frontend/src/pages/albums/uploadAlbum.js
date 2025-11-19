import React, { useCallback, useState } from 'react';
import { useDropzone } from 'react-dropzone';
import {
  Box,
  Button,
  Grid,
  Typography,
  Paper,
  IconButton,
  Snackbar,
  Alert,
} from '@mui/material';
import CloudUploadIcon from '@mui/icons-material/CloudUpload';
import DeleteIcon from '@mui/icons-material/Delete';
import {useEffect} from 'react';
import { useNavigate,useLocation } from 'react-router-dom';
import Header from './header';
import { fetchPostFileUploadWithAuth } from 'client/Client';

// ==============================|| UPLOAD PHOTOS ||============================== //

const UploadPhotos = () => {

  const location = useLocation();
  const queryParams = new URLSearchParams(location.search);
  const id = queryParams.get('id');

  const navigate = useNavigate();     
  const [files, setFiles] = useState([]);
  const [uploading, setUploading] = useState(false);
  const [successMsg, setSuccessMsg] = useState('');
  const [errorMsg, setErrorMsg] = useState('');


   useEffect(() => {
    const isLoggedIn = localStorage.getItem('token');
    if(!isLoggedIn){
      navigate('/login')
      window.location.reload();
    }
  }, []); 

  // Drag-and-drop handler
  const onDrop = useCallback((acceptedFiles) => {
    setFiles((prev) => [...prev, ...acceptedFiles]);
  }, []);

  const { getRootProps, getInputProps, isDragActive } = useDropzone({
    onDrop,
    accept: { 'image/*': [] },
    multiple: true
  });

  const handleUpload = async () => {

    if (files.length === 0) return;

    const formData = new FormData();
    files.forEach((file) => formData.append('files', file));

    try {
      setUploading(true);
      
      fetchPostFileUploadWithAuth('/albums/'+id+'/upload-photos', formData)
      .then((res) => {
        console.log(res.data);
        navigate('/album/show?id='+id);
      }
      )
      setSuccessMsg('Photos uploaded successfully!');
      setFiles([]);
    } catch (err) {
      setErrorMsg('Upload failed. Please try again.');
    } finally {
      setUploading(false);
    }
  };

  const removeFile = (index) => {
    const newFiles = [...files];
    newFiles.splice(index, 1);
    setFiles(newFiles);
  };

  return (
     <div>
      <Header />
      <div style={{ marginTop: '20px', padding: '20px' }}>
    <Box sx={{ p: 4 }}>
      <Typography variant="h4" gutterBottom>
        Upload Photos
      </Typography>

      {/* Dropzone */}
      <Paper
        {...getRootProps()}
        elevation={4}
        sx={{
          border: '2px dashed #90caf9',
          borderRadius: 2,
          p: 4,
          textAlign: 'center',
          backgroundColor: isDragActive ? '#f0f8ff' : '#fafafa',
          cursor: 'pointer'
        }}
      >
        <input {...getInputProps()} />
        <CloudUploadIcon sx={{ fontSize: 48, color: '#90caf9' }} />
        <Typography variant="body1" mt={2}>
          {isDragActive
            ? 'Drop the files here...'
            : 'Drag & drop photos here or click to select'}
        </Typography>
      </Paper>

      {/* Selected files preview */}
      {files.length > 0 && (
        <Box sx={{ mt: 4 }}>
          <Typography variant="h6" gutterBottom>
            Selected Photos
          </Typography>
          <Grid container spacing={2}>
            {files.map((file, index) => (
              <Grid item key={index} xs={12} sm={4} md={3}>
                <Paper
                  elevation={3}
                  sx={{
                    p: 1,
                    position: 'relative',
                    textAlign: 'center'
                  }}
                >
                  <img
                    src={URL.createObjectURL(file)}
                    alt={file.name}
                    style={{ maxWidth: '100%', height: '120px', objectFit: 'cover', borderRadius: 8 }}
                  />
                  <IconButton
                    size="small"
                    sx={{ position: 'absolute', top: 4, right: 4 }}
                    onClick={() => removeFile(index)}
                  >
                    <DeleteIcon fontSize="small" />
                  </IconButton>
                  <Typography variant="caption">{file.name}</Typography>
                </Paper>
              </Grid>
            ))}
          </Grid>

          <Box mt={3}>
            <Button
              variant="contained"
              color="primary"
              onClick={handleUpload}
              disabled={uploading}
            >
              {uploading ? 'Uploading...' : 'Upload Photos'}
            </Button>
          </Box>
        </Box>
      )}

      {/* Snackbar messages */}
      <Snackbar
        open={!!successMsg}
        autoHideDuration={4000}
        onClose={() => setSuccessMsg('')}
      >
        <Alert severity="success">{successMsg}</Alert>
      </Snackbar>

      <Snackbar
        open={!!errorMsg}
        autoHideDuration={4000}
        onClose={() => setErrorMsg('')}
      >
        <Alert severity="error">{errorMsg}</Alert>
      </Snackbar>
    </Box>
    </div>
    </div>
  );
};

export default UploadPhotos;
