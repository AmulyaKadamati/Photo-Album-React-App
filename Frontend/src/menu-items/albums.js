// assets
import { PictureOutlined, FileImageOutlined} from '@ant-design/icons';

// icons
const icons = {
  PictureOutlined,
  FileImageOutlined

};

// ==============================|| MENU ITEMS - SAMPLE PAGE & DOCUMENTATION ||============================== //

const album = {
  id: 'albums',
  title: 'Albums',
  type: 'group',
  children: [
    {
      id: 'album',
      title: 'Albums',
      type: 'item',
      url: '/',
      icon: icons.PictureOutlined
    },
    {
      id: 'Add album',
      title: 'Add Album',
      type: 'item',
      url: '/album/add',
      icon: icons.FileImageOutlined
    }
  ]
};

export default album;
