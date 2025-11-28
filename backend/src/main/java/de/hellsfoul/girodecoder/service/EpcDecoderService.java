package de.hellsfoul.girodecoder.service;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import de.hellsfoul.girodecoder.dto.EpcQrResult;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EpcDecoderService {

    public EpcQrResult decode(MultipartFile file) throws IOException {
        return decode(file.getInputStream());
    }

    public EpcQrResult decode(java.io.InputStream inputStream) throws IOException {
        BufferedImage image = ImageIO.read(inputStream);
        if (image == null) {
            throw new IllegalArgumentException("Uploaded file is not a supported image or is empty");
        }

        try {
            BufferedImageLuminanceSource source = new BufferedImageLuminanceSource(image);
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
            Result result = new MultiFormatReader().decode(bitmap);
            String text = result.getText();
            return parseEpcText(text);
        } catch (NotFoundException e) {
            throw new IllegalArgumentException("No QR code found in image", e);
        }
    }

    private EpcQrResult parseEpcText(String text) {
        EpcQrResult res = new EpcQrResult();
        res.setRawText(text);

        // Normalize line endings and split
        String normalized = text.replace("\r", "");
        List<String> lines = Arrays.stream(normalized.split("\n", -1))
                .map(String::trim)
                .collect(Collectors.toList());
        res.setLines(lines);

        // Safely grab by index
        res.setServiceTag(getLine(lines, 0));
        res.setVersion(getLine(lines, 1));
        res.setCharacterSet(getLine(lines, 2));
        res.setIdentification(getLine(lines, 3));
        res.setBic(getLine(lines, 4));
        res.setName(getLine(lines, 5));
        res.setIban(getLine(lines, 6));

        String amountLine = getLine(lines, 7);
        if (amountLine != null && !amountLine.isEmpty()) {
            // Amounts sometimes are prefixed with currency letters, e.g. "EUR12.34"
            String currency = amountLine.replaceAll("[^A-Z]", "");
            String value = amountLine.replaceAll("[^0-9\\.,]", "");
            if (currency.isEmpty()) currency = null;
            res.setAmountCurrency(currency);
            res.setAmountValue(value);
        }

        res.setPurpose(getLine(lines, 8));
        res.setRemittance(getLine(lines, 9));
        res.setInformation(getLine(lines, 10));

        // Any further lines put into extras with numeric keys
        for (int i = 11; i < lines.size(); i++) {
            String v = lines.get(i);
            if (v != null && !v.isEmpty()) {
                res.getExtras().put("line_" + i, v);
            }
        }

        return res;
    }

    private String getLine(List<String> lines, int idx) {
        if (lines == null || idx < 0 || idx >= lines.size()) return null;
        String v = lines.get(idx);
        return (v == null || v.isEmpty()) ? null : v;
    }
}
