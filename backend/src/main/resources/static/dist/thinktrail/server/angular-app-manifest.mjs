
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
    'index.csr.html': {size: 9311, hash: '8b33ce1d27d4eab658c613da422b89201174634d180eb5297d75ad7b542ec9d2', text: () => import('./assets-chunks/index_csr_html.mjs').then(m => m.default)},
    'index.server.html': {size: 1141, hash: '2377998819ac678b4e184724733b31bc740c3a022dd7e61177a8f3523a6445f7', text: () => import('./assets-chunks/index_server_html.mjs').then(m => m.default)},
    'styles-OZN4OKQQ.css': {size: 61844, hash: '+uPmz5dozXg', text: () => import('./assets-chunks/styles-OZN4OKQQ_css.mjs').then(m => m.default)}
  },
};
