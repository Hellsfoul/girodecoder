package de.hellsfoul.girodecoder.controller;

import de.hellsfoul.girodecoder.dto.EpcQrResult;
import de.hellsfoul.girodecoder.service.EpcDecoderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/epc")
public class EpcController {

    private final EpcDecoderService decoderService;

    @Autowired
    public EpcController(EpcDecoderService decoderService) {
        this.decoderService = decoderService;
    }

    @PostMapping(value = "/decode", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> decode(@RequestParam("file") MultipartFile file) {
        if (file == null || file.isEmpty()) {
            Map<String, String> err = new HashMap<>();
            err.put("error", "No file uploaded or file is empty");
            return ResponseEntity.badRequest().body(err);
        }

        try {
            EpcQrResult result = decoderService.decode(file);
            return ResponseEntity.ok(result);
        } catch (IllegalArgumentException | IOException e) {
            Map<String, String> err = new HashMap<>();
            err.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
        } catch (Exception e) {
            Map<String, String> err = new HashMap<>();
            err.put("error", "Internal server error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(err);
        }
    }

    @PostMapping(value = "/decode", consumes = {"image/jpeg", "image/png", "application/octet-stream"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> decodeRaw(@RequestBody byte[] imageBytes, @RequestHeader(value = "Content-Type", required = false) String contentType) {
        if (imageBytes == null || imageBytes.length == 0) {
            Map<String, String> err = new HashMap<>();
            err.put("error", "No image data in request body or body is empty");
            return ResponseEntity.badRequest().body(err);
        }

        try (java.io.ByteArrayInputStream is = new java.io.ByteArrayInputStream(imageBytes)) {
            EpcQrResult result = decoderService.decode(is);
            return ResponseEntity.ok(result);
        } catch (IllegalArgumentException | IOException e) {
            Map<String, String> err = new HashMap<>();
            err.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
        } catch (Exception e) {
            Map<String, String> err = new HashMap<>();
            err.put("error", "Internal server error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(err);
        }
    }
}
