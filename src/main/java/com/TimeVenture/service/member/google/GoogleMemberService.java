package com.TimeVenture.service.member.google;

import com.TimeVenture.model.entity.member.Member;
import com.TimeVenture.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.Instant;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GoogleMemberService {

    private final MemberRepository memberRepository;
    private final OAuth2AuthorizedClientService authorizedClientService;
    private final OAuth2AuthorizedClientManager authorizedClientManager;
    private final String uploadDir = "C:/upload";

    public void updateAccount(String email, String name, MultipartFile profilePicture) throws IOException {
        Member member = memberRepository.findById(email)
                .orElseThrow(() -> new IllegalStateException("User not found"));

        String imgPath = null;
        if (profilePicture != null && !profilePicture.isEmpty()) {
            imgPath = saveProfilePicture(profilePicture);
        }

        member.update(name, imgPath);
        memberRepository.save(member);
    }

    public void saveRefreshToken(String email, String refreshToken) {
        Member member = memberRepository.findById(email)
                .orElseThrow(() -> new IllegalStateException("User not found"));

        member.updateRefreshToken(refreshToken);
        memberRepository.save(member);
    }

    public Optional<String> getRefreshToken(String email) {
        return memberRepository.findById(email)
                .map(Member::getRefreshToken);
    }

    private String saveProfilePicture(MultipartFile profilePicture) throws IOException {
        if (profilePicture.isEmpty()) {
            return null;
        }

        String fileName = profilePicture.getOriginalFilename();
        Path filePath = Paths.get(uploadDir, fileName);

        int count = 1;
        while (Files.exists(filePath)) {
            String newFileName = fileNameWithoutExtension(fileName) + "_" + count++ + fileExtension(fileName);
            filePath = Paths.get(uploadDir, newFileName);
        }

        Files.copy(profilePicture.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        return filePath.toString();
    }

    private String fileNameWithoutExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');
        return (dotIndex == -1) ? fileName : fileName.substring(0, dotIndex);
    }

    private String fileExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');
        return (dotIndex == -1) ? "" : fileName.substring(dotIndex);
    }

    public OAuth2AccessToken refreshAccessToken(String email) {
        OAuth2AuthorizedClient authorizedClient = authorizedClientService.loadAuthorizedClient("google", email);
        OAuth2AccessToken accessToken = authorizedClient.getAccessToken();

        if (accessToken != null && accessToken.getExpiresAt().isBefore(Instant.now())) {
            // Refresh the access token using the refresh token
            OAuth2AuthorizeRequest authorizeRequest = OAuth2AuthorizeRequest.withClientRegistrationId("google")
                    .principal(email)
                    .build();
            authorizedClient = authorizedClientManager.authorize(authorizeRequest);
            accessToken = authorizedClient.getAccessToken();
            // Save the updated refresh token
            saveRefreshToken(email, authorizedClient.getRefreshToken().getTokenValue());
        }

        return accessToken;
    }
}
