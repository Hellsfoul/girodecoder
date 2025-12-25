# GiroDecoder Copilot Instructions

## Architecture Overview
This is a web application for decoding EPC-QR (GiroCode) codes from images. It consists of two main services:
- **Backend**: Spring Boot REST API that decodes QR codes using ZXing and parses EPC data
- **Frontend**: Vue.js 3 SPA with drag-and-drop image upload and result display

Services communicate via HTTP API, orchestrated with Docker Compose for production.

## Key Components
- **Backend API**: `EpcController` handles `/api/epc/decode` with multipart file upload
- **Decoding Service**: `EpcDecoderService` uses ZXing to extract QR text, then parses into `EpcQrResult` DTO
- **Frontend Upload**: `ImageDropZone.vue` component manages file selection and preview
- **Result Display**: `ResultDisplay.vue` shows structured EPC fields (BIC, IBAN, amount, etc.)

## Development Workflow
- **Backend**: Run `mvn spring-boot:run` in `backend/` folder (port 8080)
- **Frontend**: `npm install` then `npm run dev` in `frontend/` (port 5173, proxies /api to backend)
- **Docker Build**: Use `build.bat` in each service folder to create images
- **Full Stack**: Run `run compose.bat` to start containers (backend:8082, frontend:3003)

## Code Patterns
- **EPC Parsing**: Line-based parsing in `EpcDecoderService.parseEpcText()`, maps lines 0-10 to specific fields, extras beyond
- **Error Handling**: Controller returns JSON errors for invalid files, no QR found, or server errors
- **API Calls**: Frontend uses `epcApi.decodeImage()` with FormData for multipart upload
- **Styling**: Frontend uses scoped CSS in Vue components, no global framework

## Conventions
- **Package Naming**: `de.hellsfoul.girodecoder` with subpackages for controller/service/dto
- **File Upload**: Supports JPEG/PNG, validates non-empty files
- **Amount Parsing**: Extracts currency prefix (e.g., "EUR12.34" → currency: "EUR", value: "12.34")
- **Docker Images**: Tagged as `hellsfoul/girodecoder-backend` and `-frontend`

## Integration Points
- **ZXing Library**: For QR code detection from BufferedImage
- **Docker Registry**: Supports custom registries (e.g., Forgejo) with insecure options for home networks
- **CORS**: Handled by Spring Boot for frontend-backend communication

Reference: [backend/src/main/java/de/hellsfoul/girodecoder/service/EpcDecoderService.java](backend/src/main/java/de/hellsfoul/girodecoder/service/EpcDecoderService.java) for decoding logic, [frontend/src/components/ImageDropZone.vue](frontend/src/components/ImageDropZone.vue) for upload handling.</content>
<parameter name="filePath">c:\Users\Alexander\source\repos\girodecoder\.github\copilot-instructions.md