import PageNotFoundPage from '../Pages/_PageNotFoundPage';
import HomePage from '../Pages/HomePage';
import LoginPage from '../Pages/LoginPage';
import RegisterPage from '../Pages/RegisterPage';
import LogoutPage from '../Pages/LogoutPage';
import UserPage from '../Pages/UserPage';
import AdminUsersPage from '../Pages/admin/AdminUsersPage';
import PropositionPage from '../Pages/PropositionPage';
import AdminObjectsPage from "../Pages/admin/AdminObjectsPage";
import AdminOffersPage from "../Pages/admin/AdminOffersPage";

const routes = {
  '/404': PageNotFoundPage,
  '/': HomePage,
  '/login': LoginPage,
  '/register': RegisterPage,
  '/logout': LogoutPage,
  '/user/:id': UserPage,
  '/admin/offers': AdminOffersPage,
  '/admin/objects': AdminObjectsPage,
  '/admin/users': AdminUsersPage,
  '/propose': PropositionPage,
};

export default routes;
