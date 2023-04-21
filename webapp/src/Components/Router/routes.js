import PageNotFoundPage from '../Pages/_PageNotFoundPage';
import HomePage from '../Pages/HomePage';
import LoginPage from '../Pages/LoginPage';
import RegisterPage from '../Pages/RegisterPage';
import LogoutPage from '../Pages/LogoutPage';
import AdminUsersPage from '../Pages/admin/AdminUsersPage';
import OfferPage from '../Pages/OfferPage';
import AdminObjectsPage from "../Pages/admin/AdminObjectsPage";
import AdminOffersPage from "../Pages/admin/AdminOffersPage";
import ObjectPage from "../Pages/ObjectPage";
import UserPage from "../Pages/UserPage";
import AdminAvailabilitiesPage from "../Pages/admin/AdminAvailabilitiesPage";

const routes = {
  '/404': PageNotFoundPage,
  '/': HomePage,
  '/propose': OfferPage,
  '/admin/availabilities': AdminAvailabilitiesPage,
  '/admin/offers': AdminOffersPage,
  '/admin/objects': AdminObjectsPage,
  '/admin/users': AdminUsersPage,
  '/object/:id': ObjectPage,
  '/user/:id': UserPage,
  '/login': LoginPage,
  '/register': RegisterPage,
  '/logout': LogoutPage
};

export default routes;
