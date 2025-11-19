// assets
import { QuestionOutlined } from '@ant-design/icons';

// icons
const icons = {
  QuestionOutlined
};

const about_page = {
  id: 'pages',
  title: 'pages',
  type: 'group',
  children: [
    {
      id: 'About',
      title: 'About',
      type: 'item',
      url: '/about',
      icon: icons.QuestionOutlined
    }
  ]
};

export default about_page;
