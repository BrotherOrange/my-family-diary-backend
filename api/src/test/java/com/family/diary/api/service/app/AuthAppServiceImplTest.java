package com.family.diary.api.service.app;

import com.family.diary.api.dto.request.user.UserLoginRequest;
import com.family.diary.api.dto.request.user.UserRegisterRequest;
import com.family.diary.api.dto.response.user.UserLoginResponse;
import com.family.diary.api.dto.response.user.UserRegisterResponse;
import com.family.diary.api.mapper.user.UserApiMapper;
import com.family.diary.api.service.app.impl.AuthAppServiceImpl;
import com.family.diary.api.service.tencentcloud.COSService;
import com.family.diary.api.service.token.TokenService;
import com.family.diary.api.service.token.model.TokenPair;
import com.family.diary.api.service.user.AuthService;
import com.family.diary.common.exceptions.UnauthorizedException;
import com.family.diary.domain.entity.user.UserEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthAppServiceImplTest {

    @Mock
    private AuthService authService;

    @Mock
    private UserApiMapper userApiMapper;

    @Mock
    private TokenService tokenService;

    @Mock
    private COSService cosService;

    @InjectMocks
    private AuthAppServiceImpl authAppService;

    @Test
    void registerReturnsMappedResponse() {
        var request = new UserRegisterRequest();
        request.setOpenId("openid");

        var entity = new UserEntity();
        entity.setOpenId("openid");

        var response = new UserRegisterResponse();

        when(userApiMapper.toUserEntity(request)).thenReturn(entity);
        when(authService.register(entity)).thenReturn(entity);
        when(userApiMapper.toUserRegisterResponse(entity)).thenReturn(response);

        var result = authAppService.register(request);

        assertSame(response, result);
        verify(authService).register(entity);
    }

    @Test
    void loginReturnsTokensAndAvatar() {
        var request = new UserLoginRequest();
        request.setOpenId("openid");
        request.setPassword("password");

        var user = new UserEntity();
        user.setOpenId("openid");

        var response = new UserLoginResponse();

        when(authService.login("openid", "password")).thenReturn(user);
        when(userApiMapper.toUserLoginResponse(user)).thenReturn(response);
        when(tokenService.issueTokens("openid")).thenReturn(new TokenPair("access", "refresh"));
        when(cosService.getAvatarUrl("openid")).thenReturn("avatar");

        var result = authAppService.login(request);

        assertEquals("access", result.getAccessToken());
        assertEquals("refresh", result.getRefreshToken());
        assertEquals("avatar", result.getAvatarUrl());
    }

    @Test
    void logoutWithoutUserThrowsUnauthorized() {
        assertThrows(UnauthorizedException.class, () -> authAppService.logout(null));
    }

    @Test
    void logoutWithUserInvalidatesTokens() {
        var user = new UserEntity();
        user.setOpenId("openid");

        authAppService.logout(user);

        verify(tokenService).invalidateAllTokens("openid");
    }
}
