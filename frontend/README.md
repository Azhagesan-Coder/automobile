# AutoShowroom Angular workspace

Requires Node.js 22.12+ or 24 and npm. From this directory:

```powershell
npm install
npm start
```

Open http://localhost:4200. Start the Java services as described in the root README first. The Angular development server proxies `/api` to the API gateway at `http://localhost:8765`; change `proxy.conf.json` if your gateway runs elsewhere. Register a new account or sign in with an existing gateway account. Authentication tokens are stored in session storage and sent as Bearer tokens. A 401 clears the session.

The workspace includes inventory and customer create/edit/delete, sales creation, service appointment creation/deletion/status updates, date-filtered sales reports, and file upload/download by ID. Data and totals come from real API responses. No sample records are inserted. Currency display uses INR; change `money()` in `src/main.ts` if your showroom uses another currency.

Sales do not automatically deduct inventory; this follows the backend's existing behavior. Documents can be downloaded by ID because the backend has no listing endpoint. Partial service failures display an error while available data remains visible.

```powershell
npm run build
```

Production assets are emitted to `dist/showroom/browser`. Serve them through a web server that proxies `/api` to the gateway, with SPA fallback to `index.html`. The Angular development proxy is not included in the production bundle. Keep business services private behind the gateway.
