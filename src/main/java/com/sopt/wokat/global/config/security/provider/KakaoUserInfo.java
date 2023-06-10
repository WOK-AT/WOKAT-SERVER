package com.sopt.wokat.global.config.security.provider;

import java.util.Map;

//! 카카오에서 가져오는 유저의 정보들 
// https://developers.kakao.com/docs/latest/ko/kakaologin/rest-api의 값들을 key로 설정해 가져오기 
public class KakaoUserInfo implements Oauth2UserInfo {

    private Map<String, Object> attributes;

    public KakaoUserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public String getProviderId() {
        return String.valueOf(attributes.get("id"));
    }

    @Override
    public String getProvider() {
        return "kakao";
    }

    @Override
    public String getUserEmail() {
        return (String) getKakaoAccount().get("email");
    }

    @Override
    public String getNickName() {
        return (String) getProfile().get("nickname");
    }

    @Override
    public String getProfileImage() {
        Boolean isDefaultImage = (Boolean) getProfile().get("is_default_image");
        String imageURL = (isDefaultImage) ? "https://wokat-default-image.s3.ap-northeast-2.amazonaws.com/default-profileImage.png"
                                : (String) getProfile().get("profile_image_url");

        return imageURL;
    }

    public Map<String, Object> getKakaoAccount(){
        return(Map<String, Object>) attributes.get("kakao_account");
    }

    public Map<String, Object> getProfile(){
        return (Map<String, Object>) getKakaoAccount().get("profile");
    }

}
