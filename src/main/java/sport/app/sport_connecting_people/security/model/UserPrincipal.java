package sport.app.sport_connecting_people.security.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import sport.app.sport_connecting_people.entity.User;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class UserPrincipal implements UserDetails, OAuth2User {

    private final Long id;
    private final String email;
    private final String firstname;
    private final String lastname;
    private final String password;
    private final Collection<? extends GrantedAuthority> authorities;
    private Map<String, Object> attributes;
    private boolean isOAuth;
    private boolean isEnabled;

    public UserPrincipal(Long id, String email,
                         String password, String firstname, String lastname,
                         Collection<? extends GrantedAuthority> authorities,
                         boolean isOAuth, boolean isEnabled) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.firstname = firstname;
        this.lastname = lastname;
        this.authorities = authorities;
        this.isOAuth = isOAuth;
        this.isEnabled = isEnabled;
    }

    public static UserPrincipal create(User user) {
        List<GrantedAuthority> authorities = Collections.
                singletonList(new SimpleGrantedAuthority(user.getRole().getName()));

        return new UserPrincipal(
                user.getId(),
                user.getEmail(),
                user.getPassword(),
                user.getFirstname(),
                user.getLastname(),
                authorities,
                user.getProvider().toString().equals("google"),
                user.isEnabled()
        );
    }

    public static UserPrincipal create(User user, Map<String, Object> attributes) {
        UserPrincipal userPrincipal = UserPrincipal.create(user);
        userPrincipal.setAttributes(attributes);
        return userPrincipal;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public void setOAuth(boolean oAuth) {
        this.isOAuth = oAuth;
    }

    @Override
    public String getName() {
        return String.valueOf(id);
    }
}
