package com.hr.service;

import com.hr.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class SpringSecurityUserService2 implements UserDetailsService {

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public  Map<String, User> map = new HashMap<>();


    public void initUserData() {
        User user1 = new User();
        user1.setUsername("admin");
        //使用bcrypt提供的方法对密码进行加密
        user1.setPassword(passwordEncoder.encode("admin"));
        User user2 = new User();
        user2.setUsername("xiaoming");
        user2.setPassword(passwordEncoder.encode("1234"));
        map.put(user1.getUsername(),user1);
        map.put(user2.getUsername(),user2);
    }



    //根据用户名查询用户信息
    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {

        initUserData();

        System.out.println("用户输入的用户名为" + s);
        //根据用户名查询数据库获得用户信息
        User user = map.get(s);
        if(user == null) {
            //用户名不存在
            return null;
        }else {
            //将用户信息返回给框架
            //框架会进行密码比对(页面提交的和数据库中查询的密码进行比对)
            //为框架的User创建对象
            Collection<GrantedAuthority> list = new ArrayList<>();
            //为当前登录用户授权,后期需要改为从数据库查询当前用户对应的权限
            list.add(new SimpleGrantedAuthority("permission_A"));  //授权
            list.add(new SimpleGrantedAuthority("permission_B"));
            if("admin".equals(s)) {
                list.add(new SimpleGrantedAuthority("ROLE_ADMIN"));  //授予角色
                list.add(new SimpleGrantedAuthority("add"));  //授权
            }
            //框架会对提交的加密密码进行比对
            org.springframework.security.core.userdetails.User securityUser = new org.springframework.security.core.userdetails.User(s,user.getPassword(),list);
            return securityUser;
        }


    }
}
