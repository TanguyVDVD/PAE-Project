import { clearAuthenticatedUser } from '../../utils/auths';
import Navigate from '../Router/Navigate';

const LogoutPage = () => {
  clearAuthenticatedUser();
  Navigate('/');
};

export default LogoutPage;
