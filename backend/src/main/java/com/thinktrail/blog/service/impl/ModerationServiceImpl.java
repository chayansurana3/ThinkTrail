package com.thinktrail.blog.service.impl;

import com.thinktrail.blog.model.ModerationResult;
import com.thinktrail.blog.service.ModerationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.regex.Pattern;

@Service
@Slf4j
public class ModerationServiceImpl implements ModerationService {

    @Value("${google.perspective.api.key}")
    private String apiKey;

    private static final String API_URL =
            "https://commentanalyzer.googleapis.com/v1alpha1/comments:analyze?key=";

    private static final List<String> ATTRIBUTES = List.of(
            "TOXICITY", "INSULT", "PROFANITY", "SEXUALLY_EXPLICIT", "THREAT"
    );

    private static final List<Pattern> BLOCKED_PATTERNS = List.of(
            Pattern.compile("\\b(fuck|shit|bitch|asshole|bastard|dick|pussy)\\b", Pattern.CASE_INSENSITIVE),
            Pattern.compile("\\b(nigga|nigger|slut|whore|cum|cock|porn|sex|horny|nude|naked|suck)\\b", Pattern.CASE_INSENSITIVE),
            Pattern.compile("\\b(chutiya|madarchod|bhosdike|lund|randi|gaand|tatte|behenchod|bhenchod)\\b", Pattern.CASE_INSENSITIVE)
    );

    @Override
    public ModerationResult moderateText(String text) {
        try {
            if (text == null || text.trim().isEmpty()) {
                return new ModerationResult(false, null, null);
            }

            for (Pattern pattern : BLOCKED_PATTERNS) {
                if (pattern.matcher(text).find()) {
                    log.warn("Blocked by keyword filter");
                    return new ModerationResult(true, "Offensive or explicit language detected.", null);
                }
            }

            RestTemplate restTemplate = new RestTemplate();

            Map<String, Object> requestedAttributes = new HashMap<>();
            for (String attr : ATTRIBUTES) {
                requestedAttributes.put(attr, new HashMap<>());
            }

            Map<String, Object> body = Map.of(
                    "comment", Map.of("text", text),
                    "languages", List.of("en"),
                    "requestedAttributes", requestedAttributes
            );

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

            ResponseEntity<Map> response = restTemplate.exchange(
                    API_URL + apiKey,
                    HttpMethod.POST,
                    entity,
                    Map.class
            );

            Map<String, Object> resp = response.getBody();
            if (resp == null || !resp.containsKey("attributeScores")) {
                return new ModerationResult(false, null, "No moderation data returned");
            }

            Map<String, Object> scores = (Map<String, Object>) resp.get("attributeScores");

            for (String attr : ATTRIBUTES) {
                Map<String, Object> attrData = (Map<String, Object>) scores.get(attr);
                double score = ((Number) ((Map<String, Object>) attrData.get("summaryScore")).get("value")).doubleValue();

                log.info(attr + " score: " + score);
                // Slightly stricter threshold
                if (score > 0.6) {
                    return new ModerationResult(true, "Content flagged for " + attr.toLowerCase(), null);
                }
            }

            log.info("Content Moderation Complete — clean");
            return new ModerationResult(false, null, null);

        } catch (Exception e) {
            log.error("Error during moderation", e);
            return new ModerationResult(true, null, e.getMessage());
        }
    }
}