import { lazy } from 'react';

import Loadable from 'components/Loadable';
import MainLayout from 'layout/MainLayout';

const AlbumsPage = Loadable(lazy(() => import('pages/albums/album')));
const AboutPage = Loadable(lazy(() => import('pages/staticPages/about')));
const AddAlbumPage = Loadable(lazy(() => import('pages/albums/addAlbum')));
const ShowAlbumPage = Loadable(lazy(() => import('pages/albums/showAlbum')));
const UploadAlbumPage = Loadable(lazy(() => import('pages/albums/uploadAlbum')));
const EditAlbumPage = Loadable(lazy(() => import('pages/albums/editAlbum')));
const EditPhotoPage = Loadable(lazy(() => import('pages/albums/editPhoto')));
// ==============================|| MAIN ROUTING ||============================== //

const MainRoutes = {
  path: '/',
  element: <MainLayout />,
  children: [
    {
      path: '/',
      element: <AlbumsPage />
    },
    {
      path: '/album/add',
      element: <AddAlbumPage />
    },
    {
      path: '/about',
      element: <AboutPage />
    },
    {
      path: '/album/show',
      element: <ShowAlbumPage />
    },
    {
      path: '/album/upload',
      element: <UploadAlbumPage />
    },
    {
      path: '/album/edit',
      element: <EditAlbumPage />
    },
    {
      path: '/photo/edit',
      element: <EditPhotoPage />
    }
  ]
};

export default MainRoutes;
