package nomad.example.nomad_backend.service.impls;

import nomad.example.nomad_backend.entity.VisaType;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class VisaService {

    private static final Set<String> VISA_FREE_COUNTRIES = Set.of(

            // II qrup
            "Belarus",
            "Gürcüstan",
            "Qazaxıstan",
            "Qırğızıstan",
            "Moldova",
            "Özbəkistan",
            "Rusiya",
            "Tacikistan",
            "Ukrayna",
            "Türkiyə",
            "Qətər",
            "BƏƏ",
            "Birləşmiş Ərəb Əmirlikləri",
            "Serbiya",
            "Albaniya",
            "Mərakeş",
            "Çin",
            "Maldiv",

            // III qrup
            "İran",
            "Monteneqro",
            "Çernoqoriya",
            "Bosniya və Herseqovina",
            "Bosniya",
            "Malayziya",
            "Livan"
    );

    public VisaType determine(String country) {

        if (country == null || country.isBlank()) {
            return null;
        }

        String normalizedCountry = country
                .trim();

        if (VISA_FREE_COUNTRIES.contains(normalizedCountry)) {
            return VisaType.VISA_FREE;
        }

        return VisaType.VISA_REQUIRED;
    }
}
