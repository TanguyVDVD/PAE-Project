import HomePage from '../Pages/HomePage';
import NewPage from '../Pages/NewPage';
import LoginPage from '../Pages/LoginPage';
import RegisterPage from '../Pages/RegisterPage';
import LogoutPage from '../Pages/LogoutPage';
import AdminUsersPage from '../Pages/admin/AdminUsersPage';

const routes = {
  '/': HomePage,
  '/new': NewPage,
  '/login': LoginPage,
  '/register': RegisterPage,
  '/logout': LogoutPage,
  '/admin/users': AdminUsersPage,
};

export default routes;
