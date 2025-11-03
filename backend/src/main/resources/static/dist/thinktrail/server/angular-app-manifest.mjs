
export default {
  bootstrap: () => import('./main.server.mjs').then(m => m.default),
  inlineCriticalCss: true,
  baseHref: '/',
  locale: undefined,
  routes: [
  {
    "renderMode": 0,
    "route": "/"
  },
  {
    "renderMode": 0,
    "route": "/posts"
  },
  {
    "renderMode": 0,
    "route": "/posts/*"
  },
  {
    "renderMode": 0,
    "route": "/write"
  },
  {
    "renderMode": 0,
    "route": "/login"
  },
  {
    "renderMode": 0,
    "route": "/myposts"
  },
  {
    "renderMode": 0,
    "route": "/signup"
  },
  {
    "renderMode": 0,
    "route": "/profile/*"
  },
  {
    "renderMode": 0,
    "route": "/userComments"
  },
  {
    "renderMode": 0,
    "route": "/error"
  },
  {
    "renderMode": 0,
    "redirectTo": "/",
    "route": "/**"
  }
],
  entryPointToBrowserMapping: undefined,
  assets: {
    'index.csr.html': {size: 9260, hash: '26e23751b639a5fbdf932c8f789cca2bc6a62fdf8cebb66890eb01e394f03a0d', text: () => import('./assets-chunks/index_csr_html.mjs').then(m => m.default)},
    'index.server.html': {size: 1090, hash: 'a5bdea6507af112798318703e1fd40012034d47d83743b912ac802f1127746a0', text: () => import('./assets-chunks/index_server_html.mjs').then(m => m.default)},
    'styles-GZOK2SWH.css': {size: 37585, hash: 'NBWbNWvhrJc', text: () => import('./assets-chunks/styles-GZOK2SWH_css.mjs').then(m => m.default)}
  },
};
