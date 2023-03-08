/* eslint-disable import/no-cycle */
import Navbar from '../Components/Navbar/Navbar';
import API from './api';

const STORE_USER = 'user';
const STORE_REMEMBER = 'remember';
let currentUser;

const clearAuthenticatedUser = () => {
  localStorage.removeItem(STORE_USER);
  sessionStorage.removeItem(STORE_USER);

  localStorage.removeItem(STORE_REMEMBER);
  currentUser = undefined;

  // Update the navbar
  Navbar();
};

const getAuthenticatedUser = () => {
  if (currentUser !== undefined) return currentUser;

  const serializedUser = localStorage.getItem(STORE_USER) ?? sessionStorage.getItem(STORE_USER);
  if (!serializedUser) return undefined;

  currentUser = JSON.parse(serializedUser);
  return currentUser;
};

const setAuthenticatedUser = (authenticatedUser, remember = false) => {
  clearAuthenticatedUser();

  const serializedUser = JSON.stringify(authenticatedUser);
  (remember ? localStorage : sessionStorage).setItem(STORE_USER, serializedUser);
  localStorage.setItem(STORE_REMEMBER, remember);

  currentUser = authenticatedUser;

  // Update the navbar
  Navbar();
};

const isAuthenticated = () => currentUser !== undefined || getAuthenticatedUser() !== undefined;

const getRememberMe = () => sessionStorage.getItem('remember') === 'true';

const refreshAuthenticatedUser = () => {
  const authenticatedUser = getAuthenticatedUser();
  if (!authenticatedUser) return;

  const remember = getRememberMe();

  API.get('users/my')
    .then((data) => {
      setAuthenticatedUser({ ...authenticatedUser, ...data }, remember);
    })
    .catch(clearAuthenticatedUser);
};

export {
  getAuthenticatedUser,
  setAuthenticatedUser,
  isAuthenticated,
  clearAuthenticatedUser,
  getRememberMe,
  refreshAuthenticatedUser,
};
