import { Button, TextField } from '@mui/material';
import { fetchPostDataWithAuth } from 'client/Client';
import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';

const AddAlbumForm = () => {
  const navigate = useNavigate();

  useEffect(() => {
    const isLoggedIn = localStorage.getItem('token');
    if (!isLoggedIn) {
      navigate('/login');
      window.location.reload();
    }
  }, []);

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
    // Form validation
    let isValid = true;
    const newErrors = { name: '', description: '' };

    if (!formData.name.trim()) {
      newErrors.name = 'Name is required';
      isValid = false;
    }
    if (!formData.description.trim()) {
      newErrors.name = 'Description is Required';
      isValid = false;
    }

    setErrors(newErrors);

    if (isValid) {
      const payload = {
        name: formData.name,
        description: formData.description
      };
      fetchPostDataWithAuth('/albums/add', payload)
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
        Add Album
      </Button>
    </form>
  );
};

export default AddAlbumForm;
