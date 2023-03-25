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

  // Find the route that matches the best the current URI
  // If no route matches, the 404 page will be rendered
  const routeKey = Object.keys(routes)
    .filter((r) => uri.match(`^${r.replace(/:[a-z]+/g, '[a-z0-9]*')}$`))
    .sort((a, b) => b.length - a.length)[0];

  const componentToRender = routes[routeKey];

  if (!componentToRender) routes['/404']();
  else {
    // Extract the parameters from the URI
    const params = {};
    const uriParts = uri.split('/');
    const routeParts = routeKey.split('/');
    routeParts.forEach((part, index) => {
      if (part.startsWith(':')) {
        params[part.replace(':', '')] = uriParts[index];
      }
    });

    componentToRender(params);
  }
}

export default Router;
