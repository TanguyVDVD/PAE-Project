import routes from './routes';

const Router = () => {
  onFrontendLoad();
  // Since the navbar changes dynamically, onNavBarClick has been moved to the Navbar component
  onHistoryChange();
};

function onHistoryChange() {
  window.addEventListener('popstate', renderRoute);
}

function onFrontendLoad() {
  window.addEventListener('load', renderRoute);
}

function renderRoute() {
  const uri = window.location.pathname;
  const componentToRender = routes[uri];

  if (!componentToRender) routes['/404']();
  else componentToRender();
}

export default Router;
