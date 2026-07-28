package nomad.example.nomad_backend.service;

import nomad.example.nomad_backend.dtos.NotificationSettingsRequestDto;
import nomad.example.nomad_backend.dtos.NotificationSettingsResponseDto;
import nomad.example.nomad_backend.entity.Opportunity;

public interface NotificationSettingsService {
    NotificationSettingsResponseDto getSettings();
    NotificationSettingsResponseDto update(NotificationSettingsRequestDto request);
    public void notifyInterestedUsers(Opportunity opportunity);
}
