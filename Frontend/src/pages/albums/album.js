// material-ui
import React, {useEffect, useState} from 'react';
import { Grid, Card,CardContent } from '@mui/material';
import { makeStyles } from '@mui/styles';
import { fetchGetDataWithAuth } from 'client/Client';
import { useNavigate } from 'react-router-dom';
import { Link } from 'react-router-dom';

  const brightPopColors = [
    '#FF0000', '#00FF00', '#0000FF', '#FFA500', '#2196F3', 
    '#3CB371', '#9400D3', '#F44336', '#9C27B0', '#708090 ',
    '#D2691E ', '#40E0D0 ', '#FF69B4', '#000080 ', '#DAA520 '

  ];


  const getRandomColor = () =>{          
    const randomIndex = Math.floor(Math.random() * brightPopColors.length);
    return brightPopColors[randomIndex];
  };

  const useStyles = makeStyles((theme)=>({
    Card:{
      backgroundColor : getRandomColor(),
      textAlign : 'center',
      padding : theme.spacing(3),
      borderRadius : theme.spacing(2),
      height : '250px',
      display : 'flex',
      flexDirection : 'column',
      justifyContent : 'center',
    },

  }));

// ==============================|| ALBUM DYNAMIC GRID PAGE ||============================== //
  const AlbumDynamicGridPage = () =>{
  
    const[dataArray, setDataArray] = useState([]);
    const navigate = useNavigate();  
     useEffect(() => {
      const isLoggedIn = localStorage.getItem('token');
      if(!isLoggedIn){
        navigate('/login')
        window.location.reload();
      }
      fetchGetDataWithAuth('/albums')
      .then(res =>{
        setDataArray(res.data);
        console.log('Data Array :', dataArray)
      })
    }, []);

    const classes = useStyles();
    // Render the grid of albums
    return(
      <Grid container spacing={2}>
        {dataArray.map((data, index) => (                                
          <Grid item key={index} xs={12} sm={6} md={4} lg={3}>   
          <Link to={`/album/show?id=${data.id}`} >
            <Card className={classes.Card} style= {{backgroundColor: getRandomColor() }}>
              <CardContent>
                <h1 style= {{fontSize: '2rem', margin:0, color:'white'}}>
                  {data.name}
                </h1>
              </CardContent>
            </Card>
            </Link>
            </Grid>
        ))}
      </Grid>
    );

  };



export default AlbumDynamicGridPage;