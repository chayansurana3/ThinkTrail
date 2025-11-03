
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
    'index.csr.html': {size: 9260, hash: 'c754f72235a6a5ffc5617470edb7f9e6ad9a17cf5f4b1209e7a88bcdd0f6fcf7', text: () => import('./assets-chunks/index_csr_html.mjs').then(m => m.default)},
    'index.server.html': {size: 1090, hash: '2278270b4fa152b34b529a41123501e990a7f0b6a6e2f292303b5ee34ac4525f', text: () => import('./assets-chunks/index_server_html.mjs').then(m => m.default)},
    'styles-GZOK2SWH.css': {size: 37585, hash: 'NBWbNWvhrJc', text: () => import('./assets-chunks/styles-GZOK2SWH_css.mjs').then(m => m.default)}
  },
};
