import HomePage from '../Pages/HomePage';
import NewPage from '../Pages/NewPage';
import LoginPage from '../Pages/LoginPage';
import RegisterPage from '../Pages/RegisterPage';
import LogoutPage from '../Pages/LogoutPage';

const routes = {
  '/': HomePage,
  '/new': NewPage,
  '/login': LoginPage,
  '/register': RegisterPage,
  '/logout': LogoutPage,
};

export default routes;
