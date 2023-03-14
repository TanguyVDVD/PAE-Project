// eslint-disable-next-line no-unused-vars
import { Navbar as BootstrapNavbar } from 'bootstrap';
// eslint-disable-next-line import/no-cycle
import { getAuthenticatedUser } from '../../utils/auths';
import Navigate from '../Router/Navigate';

import logo from '../../img/RieCochet_Logo.png';

/**
 * Render the Navbar which is styled by using Bootstrap
 * Each item in the Navbar is tightly coupled with the Router configuration :
 * - the URI associated to a page shall be given in the attribute "data-uri" of the Navbar
 * - the router will show the Page associated to this URI when the user click on a nav-link
 */

const Navbar = () => {
  const navbarWrapper = document.querySelector('#navbarWrapper');

  const authenticatedUser = getAuthenticatedUser();

  const navbar = `
    <nav class="navbar navbar-expand-lg navbar-light bg-light">
      <div class="container-fluid">
        <a class="navbar-brand nav-link" href="#" data-uri="/">
          <img src="${logo}" alt="" width="50" height="45" />
        </a>
        <div class="collapse navbar-collapse" id="navbarNavDropdown">
          <ul class="navbar-nav">
            <li class="nav-item">
              <a class="nav-link" href="#" data-uri="/">Accueil</a>
            </li>
            <li class="nav-item">
              <a class="nav-link" href="#" data-uri="/propose">Proposer un objet</a>
            </li>
            ${
              authenticatedUser && authenticatedUser.isHelper
                ? `
                  <li class="nav-item">
                    <a class="nav-link" href="#" data-uri="/admin/propositions">Propositions</a>
                  </li>
                  <li class="nav-item">
                    <a class="nav-link" href="#" data-uri="/admin/objects">Objets</a>
                  </li>
                  <li class="nav-item">
                    <a class="nav-link" href="#" data-uri="/admin/users">Utilisateurs</a>
                  </li>
                  <li class="nav-item">
                    <a class="nav-link" href="#" data-uri="/admin">Tableau de bord</a>
                  </li>
                `
                : ''
            }
          </ul>
        </div>
        ${
          authenticatedUser
            ? `
              <div class="d-flex gap-4">
                <div class="dropdown align-self-center">
                  <a
                    href="#"
                    class="nav-link"
                    data-bs-toggle="dropdown"
                    data-bs-auto-close="outside"
                    aria-expanded="false"
                  >
                    <i class="bi bi-bell" style="font-size: 1.5em"></i>
                    <span
                      class="position-absolute top-0 start-100 translate-middle badge rounded-pill bg-danger"
                    >
                      1
                    </span>
                  </a>
                  <ul class="dropdown-menu dropdown-menu-end shadow">
                    <li>
                      <span class="dropdown-item"
                        >Votre proposition d'objet "Bmw 335I" a été refusée : “Objet trop
                        grand”.</span
                      >
                    </li>
                    <li>
                      <span class="dropdown-item text-muted"
                        >Votre proposition d'objet "Chaise" a été acceptée.</span
                      >
                    </li>
                    <li>
                      <span class="dropdown-item text-muted"
                        >Votre proposition d'objet "Guitare" a été acceptée.</span
                      >
                    </li>
                  </ul>
                </div>

                <div class="flex-shrink-0 dropdown">
                  <a
                    href="#"
                    class="d-block link-dark text-decoration-none dropdown-toggle"
                    data-bs-toggle="dropdown"
                    aria-expanded="false"
                  >
                    <img
                      src="https://api.lorem.space/image/face?w=32&h=32"
                      alt="avatar"
                      width="32"
                      height="32"
                      class="rounded-circle me-1"
                    />
                    <span>${authenticatedUser.firstName} ${authenticatedUser.lastName}</span>
                  </a>
                  <ul class="dropdown-menu dropdown-menu-end shadow">
                    <li><a class="dropdown-item" href="#" data-uri="/profile">Mon profil</a></li>
                    <li>
                      <a class="dropdown-item" href="#" data-uri="/logout">Se déconnecter</a>
                    </li>
                  </ul>
                </div>
              </div>
            `
            : `
              <div class="d-flex align-items-center">
                <ul class="navbar-nav">
                  <li class="nav-item">
                    <a class="nav-link" href="#" data-uri="/register">S'inscrire</a>
                  </li>
                  <li class="nav-item">
                    <a class="nav-link" href="#" data-uri="/login">S'identifier</a>
                  </li>
                </ul>
              </div>
            `
        }
      </div>
    </nav>
  `;
  navbarWrapper.innerHTML = navbar;

  navbarWrapper.querySelectorAll('[data-uri]').forEach((link) => {
    link.addEventListener('click', (e) => {
      e.preventDefault();
      Navigate(link.dataset.uri);
    });
  });
};

export default Navbar;
