package com.duanHaiTao.dongLiYunKe.Common;

import com.duanHaiTao.dongLiYunKe.model.TUser;
import lombok.SneakyThrows;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import java.util.Collection;

public class JwtAuthenticationToken extends AbstractAuthenticationToken {

    private TUser user;

    public JwtAuthenticationToken(TUser tUser, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.user = tUser;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @SneakyThrows
    @Override
    public Object getPrincipal() {
        return user ;
    }
}
