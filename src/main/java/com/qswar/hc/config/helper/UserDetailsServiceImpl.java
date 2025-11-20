package com.qswar.hc.config.helper;


import com.qswar.hc.model.Employee;
import com.qswar.hc.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class UserDetailsServiceImpl implements UserDetailsService, UserDetailsInterface{

    private EmployeeRepository employeeRepository;


    public UserDetailsServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Employee employee = employeeRepository.findByAnyOfUniqueField(username);

        AuthUser authUser = AuthUser.getUser(username, employee);

        if (authUser == null) {
            throw new UsernameNotFoundException(username);
        }
        return new org.springframework.security.core.userdetails.User(authUser.getUsername(), authUser.getPassword(), Collections.emptyList());
    }


    @Override
    public Employee getUser(String username) throws UsernameNotFoundException {
        return employeeRepository.findByAnyOfUniqueField(username);
    }
}