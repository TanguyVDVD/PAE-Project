import './stylesheets/main.scss';

import Navbar from './Components/Navbar/Navbar';
import Router from './Components/Router/Router';

import { refreshAuthenticatedUser } from './utils/auths';

Navbar();

Router();

refreshAuthenticatedUser();
