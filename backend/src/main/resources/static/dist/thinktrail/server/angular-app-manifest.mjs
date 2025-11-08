
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
    'index.csr.html': {size: 9311, hash: '2b93a0b499d66a184e0a529d285ee53633642b6f67d27897381552d3eae474dd', text: () => import('./assets-chunks/index_csr_html.mjs').then(m => m.default)},
    'index.server.html': {size: 1141, hash: '1dba39b115efe990733a1504a5438f08a9a0aa6c4049ff768775e801f3c66e77', text: () => import('./assets-chunks/index_server_html.mjs').then(m => m.default)},
    'styles-SHIGE3RF.css': {size: 37662, hash: 'CO8d2Leix6c', text: () => import('./assets-chunks/styles-SHIGE3RF_css.mjs').then(m => m.default)}
  },
};
