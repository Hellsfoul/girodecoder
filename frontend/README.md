# EPC-QR Decoder Frontend

Vue 3 + Vite frontend for the EPC-QR Code decoder backend.

## Getting Started

### Install Dependencies

```bash
cd frontend
npm install
```

### Development Server

```bash
npm run dev
```

The app will be available at `http://localhost:5173` with API proxy to backend at `http://localhost:8080/api`.

### Build for Production

```bash
npm run build
```

Output will be in the `dist/` folder.

### Preview Production Build

```bash
npm run preview
```

## Features

- **Image Drop Zone**: Drag and drop EPC-QR images or click to select
- **Image Preview**: Shows selected image before upload
- **API Integration**: Sends image to backend `/api/epc/decode` endpoint
- **Structured Display**: Shows parsed EPC-QR fields in organized sections
- **Raw Data**: Displays raw QR text for debugging
- **Error Handling**: User-friendly error messages

## Structure

```
frontend/
├── src/
│   ├── App.vue                 # Root component
│   ├── main.js                 # App entry point
│   ├── api/
│   │   └── epcApi.js          # Backend API client
│   └── components/
│       ├── ImageDropZone.vue  # File upload component
│       └── ResultDisplay.vue  # Result display component
├── index.html
├── vite.config.js
└── package.json
```

## How to Use

1. Start the backend (Spring Boot) at `http://localhost:8080`
2. Start the frontend dev server with `npm run dev`
3. Open `http://localhost:5173` in your browser
4. Drag and drop an EPC-QR image or click to select one
5. Click "Upload & Decode"
6. View the parsed EPC-QR data in the result section
