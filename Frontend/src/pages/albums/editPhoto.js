import { Button, TextField } from '@mui/material';
import { fetchPutDataWithAuth } from 'client/Client';
import { useEffect, useState } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
// ==============================|| EDIT PHOTO FORM ||============================== //

const EditPhoto = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const queryParams = new URLSearchParams(location.search);
  const album_id = queryParams.get('album_id');
  const photo_id = queryParams.get('photo_id');
  const photo_name = queryParams.get('photo_name');
  let photo_desc = queryParams.get('photo_description');

  useEffect(() => {
    const isLoggedIn = localStorage.getItem('token');
    if (!isLoggedIn) {
      navigate('/login');
      window.location.reload();
    }
    if (photo_desc == 'null') {
      photo_desc = '';
    }
    setFormData((prevData) => ({ ...prevData, name: photo_name, description: photo_desc }));
  }, [navigate]);
  const [formData, setFormData] = useState({
    name: '',
    description: ''
  });

  const [errors, setErrors] = useState({
    name: '',
    description: ''
  });

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData((prevData) => ({
      ...prevData,
      [name]: value
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    let isValid = true;
    const newErrors = { name: '', description: '' };

    if (!formData.name.trim()) {
      newErrors.name = 'Name is required';
      isValid = false;
    }
    if (!formData.description.trim()) {
      newErrors.name = 'Description is required';
      isValid = false;
    }

    setErrors(newErrors);

    if (isValid) {
      const payload = {
        name: formData.name,
        description: formData.description
      };
      fetchPutDataWithAuth('/albums/' + album_id + '/photos/' + photo_id + '/update-photo', payload)
        .then((response) => {
          console.log(response);
        })
        .catch((error) => {
          console.log('login error: ', error);
        });
      console.log('Form submitted: ');
      navigate('/');
    }
  };

  return (
    <form onSubmit={handleSubmit}>
      <TextField
        label="Name"
        name="name"
        variant="outlined"
        margin="normal"
        fullWidth
        value={formData.name}
        onChange={handleInputChange}
        error={!!errors.name}
        helperText={errors.name}
      />

      <TextField
        label="Description"
        name="description"
        variant="outlined"
        margin="normal"
        fullWidth
        value={formData.description}
        onChange={handleInputChange}
        error={!!errors.description}
        helperText={errors.description}
        multiline
        rows={4}
      />

      <Button type="submit" variant="contained" color="primary">
        Edit Photo
      </Button>
    </form>
  );
};

export default EditPhoto;
