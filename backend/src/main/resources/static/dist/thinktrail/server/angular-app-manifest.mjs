
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
    'index.csr.html': {size: 9260, hash: 'c4e76f6c2b4e6072b05028b352fbce3ae6a476195b4da07b1b7b904098d56719', text: () => import('./assets-chunks/index_csr_html.mjs').then(m => m.default)},
    'index.server.html': {size: 1090, hash: '53bc120b27ddb7728d747fa01d6e6e8d0b1f7b44993f0bcbee781d6dc7bf133b', text: () => import('./assets-chunks/index_server_html.mjs').then(m => m.default)},
    'styles-GZOK2SWH.css': {size: 37585, hash: 'NBWbNWvhrJc', text: () => import('./assets-chunks/styles-GZOK2SWH_css.mjs').then(m => m.default)}
  },
};
