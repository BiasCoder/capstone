package academy.doku.da3duawebserviceapi.mekaniku.user.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserSecurity implements UserDetails {
    private UserEntity userEntity;
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }
    @Override
    public String getPassword() {
        return userEntity.getPassword();
    }
    @Override
    public String getUsername() {
        return userEntity.getEmail();
    }
    public String getFullName() {
        return userEntity.getFullName();
    }
    public String getPhoneNumber(){
        return userEntity.getPhone();
    }
    public Integer getId(){
        return userEntity.getId();
    }
    public UserRole getRole(){
        return userEntity.getRole();
    }

    public String getEmail() {
        return userEntity.getEmail();
    }

    public boolean getSuspend() {
        return userEntity.getSuspend();
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
        return isNotSuspended();
    }

    private boolean isNotSuspended(){
        return !userEntity.getSuspend();
    }
}
