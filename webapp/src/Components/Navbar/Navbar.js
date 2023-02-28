// eslint-disable-next-line no-unused-vars
import {Navbar as BootstrapNavbar} from 'bootstrap';
import logo from '../../img/RieCochet_Logo.png';

/**
 * Render the Navbar which is styled by using Bootstrap
 * Each item in the Navbar is tightly coupled with the Router configuration :
 * - the URI associated to a page shall be given in the attribute "data-uri" of the Navbar
 * - the router will show the Page associated to this URI when the user click on a nav-link
 */

const Navbar = () => {
    const navbarWrapper = document.querySelector('#navbarWrapper');
    const navbar = `
    <nav class="navbar navbar-expand-lg navbar-light bg-light">
    <div class="container-fluid">
      <a class="navbar-brand" href="#">
        <img src="${logo}" alt="" width="50" height="45">
      </a>
      <div class="collapse navbar-collapse" id="navbarNavDropdown">
        <ul class="navbar-nav">
          <li class="nav-item">
            <a class="nav-link" aria-current="page" href="#">Accueil</a>
          </li>
          <li class="nav-item">
            <a class="nav-link" href="#">Proposer un objet</a>
          </li>
          <li class="nav-item hidden">
            <a class="nav-link" href="#">Propositions</a>
          </li>
          <li class="nav-item">
            <a class="nav-link hidden" href="#">Objets</a>
          </li>
          <li class="nav-item">
            <a class="nav-link hidden" href="#">Utilisateurs</a>
          </li>
          <li class="nav-item">
            <a class="nav-link hidden" href="#">Tableau de bord</a>
          </li>
        </ul>
      </div>
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
    </div>
  </nav>
    `;
    navbarWrapper.innerHTML = navbar;
};

export default Navbar;
