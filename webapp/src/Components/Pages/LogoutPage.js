import {clearAuthenticatedUser} from '../../utils/auths';
import Navigate from '../Router/Navigate';
import {stopInterval} from "../Navbar/Navbar";

const LogoutPage = () => {
  stopInterval();
  clearAuthenticatedUser();
  Navigate('/');
};

export default LogoutPage;
