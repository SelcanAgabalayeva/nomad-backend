package nomad.example.nomad_backend.config;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.ValueRange;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GoogleSheetsService {

    private final Sheets sheets;

    @Value("${google.sheet.id}")
    private String spreadsheetId;

    public List<List<Object>> read(String range) throws Exception {

        ValueRange response = sheets.spreadsheets().values()
                .get(spreadsheetId, range)
                .execute();

        return response.getValues();
    }
}
