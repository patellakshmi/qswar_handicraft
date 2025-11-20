package com.qswar.hc.config.helper;

import com.qswar.hc.model.Employee;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface UserDetailsInterface {
    public Employee getUser(String username) throws UsernameNotFoundException;
}
