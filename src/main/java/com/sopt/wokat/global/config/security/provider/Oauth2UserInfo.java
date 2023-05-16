package com.sopt.wokat.global.config.security.provider;

public interface Oauth2UserInfo {
    String getProviderId();
    String getProvider();
    String getUserEmail();
    String getNickName();
    String getProfileImage();
}