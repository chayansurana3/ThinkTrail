
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
    'index.csr.html': {size: 9311, hash: '7dc9254fcc01e6c0b23eaf040d071f0a2d4608687b623c599e3d485e73b1a1ba', text: () => import('./assets-chunks/index_csr_html.mjs').then(m => m.default)},
    'index.server.html': {size: 1141, hash: '4c3442def0faafe3fe5af575bac843496e3fb1fd6e7a1060bb08bbcf207bd3c8', text: () => import('./assets-chunks/index_server_html.mjs').then(m => m.default)},
    'styles-5Q3X2RNK.css': {size: 61946, hash: 'MXDJso5vr7A', text: () => import('./assets-chunks/styles-5Q3X2RNK_css.mjs').then(m => m.default)}
  },
};
