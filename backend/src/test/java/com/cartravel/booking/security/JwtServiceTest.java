package com.cartravel.booking.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("JwtService Security & Parsing Tests")
class JwtServiceTest {

    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
        ReflectionTestUtils.setField(jwtService, "jwtSecret", "carTravelBookingSuperSecretKeyForJwtAuthentication2026ShouldBe256BitsLong!");
        ReflectionTestUtils.setField(jwtService, "jwtExpirationMs", 3600000L); // 1 hour
    }

    @Test
    @DisplayName("Generate token from email and extract subject correctly")
    void testGenerateAndParseToken() {
        String email = "testuser@kavintravels.com";
        String token = jwtService.generateTokenFromEmail(email);

        assertNotNull(token);
        assertTrue(token.split("\\.").length == 3); // Valid 3-part JWT

        String extractedEmail = jwtService.getEmailFromToken(token);
        assertEquals(email, extractedEmail);
        assertTrue(jwtService.validateToken(token));
    }

    @Test
    @DisplayName("Validate token returns false for tampered token")
    void testValidateToken_TamperedToken_ReturnsFalse() {
        String token = jwtService.generateTokenFromEmail("user@example.com");
        String tamperedToken = token.substring(0, token.length() - 5) + "abcde";

        assertFalse(jwtService.validateToken(tamperedToken));
    }

    @Test
    @DisplayName("Validate token returns false for invalid malformed string")
    void testValidateToken_MalformedString_ReturnsFalse() {
        assertFalse(jwtService.validateToken("not-a-valid-jwt-token"));
        assertFalse(jwtService.validateToken(""));
    }
}
