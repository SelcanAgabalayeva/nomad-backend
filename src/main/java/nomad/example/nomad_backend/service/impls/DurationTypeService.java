package nomad.example.nomad_backend.service.impls;

import nomad.example.nomad_backend.entity.DurationType;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Service
public class DurationTypeService {

    public DurationType determine(String duration) {

        if (duration == null || duration.isBlank()) {
            return null;
        }

        String value = duration.toLowerCase().trim();

        // İl varsa → həmişə LONG_TERM
        if (value.matches(".*\\d+\\s*il.*")) {
            return DurationType.LONG_TERM;
        }

        // Həftə
        Matcher weeksMatcher =
                Pattern.compile("(\\d+)\\s*həft").matcher(value);

        if (weeksMatcher.find()) {
            int weeks = Integer.parseInt(weeksMatcher.group(1));

            return weeks <= 8
                    ? DurationType.SHORT_TERM
                    : DurationType.LONG_TERM;
        }

        // Ay
        Matcher monthsMatcher =
                Pattern.compile("(\\d+)\\s*ay").matcher(value);

        if (monthsMatcher.find()) {
            int months = Integer.parseInt(monthsMatcher.group(1));

            return months <= 2
                    ? DurationType.SHORT_TERM
                    : DurationType.LONG_TERM;
        }

        // Gün
        Matcher daysMatcher =
                Pattern.compile("(\\d+)\\s*gün").matcher(value);

        if (daysMatcher.find()) {
            int days = Integer.parseInt(daysMatcher.group(1));

            return days <= 60
                    ? DurationType.SHORT_TERM
                    : DurationType.LONG_TERM;
        }

        return null;
    }
}