package me.beomchu.security.oauth.provider;

public interface Oauth2Info {
    String getProviderId();
    String getProvider();
    String getEmail();
    String getName();
}
