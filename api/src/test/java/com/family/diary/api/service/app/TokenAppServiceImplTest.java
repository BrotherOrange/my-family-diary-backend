package com.family.diary.api.service.app;

import com.family.diary.api.dto.request.token.TokenRefreshRequest;
import com.family.diary.api.service.app.impl.TokenAppServiceImpl;
import com.family.diary.api.service.token.TokenService;
import com.family.diary.api.service.token.model.TokenPair;
import com.family.diary.common.exceptions.UnauthorizedException;
import io.jsonwebtoken.MalformedJwtException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TokenAppServiceImplTest {

    @Mock
    private TokenService tokenService;

    @InjectMocks
    private TokenAppServiceImpl tokenAppService;

    @Test
    void refreshReturnsNewTokens() {
        var request = new TokenRefreshRequest();
        request.setRefreshToken("refresh");

        when(tokenService.extractOpenId("refresh")).thenReturn("openid");
        when(tokenService.validateRefreshToken("refresh", "openid")).thenReturn(true);
        when(tokenService.issueTokens("openid")).thenReturn(new TokenPair("access", "refresh2"));

        var response = tokenAppService.refresh(request);

        assertEquals("access", response.getAccessToken());
        assertEquals("refresh2", response.getRefreshToken());
    }

    @Test
    void refreshWithInvalidTokenThrowsUnauthorized() {
        var request = new TokenRefreshRequest();
        request.setRefreshToken("bad");

        when(tokenService.extractOpenId("bad")).thenThrow(new MalformedJwtException("bad"));

        assertThrows(UnauthorizedException.class, () -> tokenAppService.refresh(request));
    }
}
