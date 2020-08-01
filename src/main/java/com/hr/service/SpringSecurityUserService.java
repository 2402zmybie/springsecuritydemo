package com.hr.service;

import com.hr.pojo.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class SpringSecurityUserService implements UserDetailsService {

    //模拟数据库中的用户数据
    public  static Map<String, User> map = new HashMap<>();
    static {
        com.hr.pojo.User user1 = new com.hr.pojo.User();
        user1.setUsername("admin");
        user1.setPassword("admin");  //明文密码(没有加密)
        com.hr.pojo.User user2 = new com.hr.pojo.User();
        user2.setUsername("xiaoming");
        user2.setPassword("1234");
        map.put(user1.getUsername(),user1);
        map.put(user2.getUsername(),user2);
    }

    //根据用户名查询用户信息
    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
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
            }
            org.springframework.security.core.userdetails.User securityUser = new org.springframework.security.core.userdetails.User(s,"{noop}" + user.getPassword(),list);
            return securityUser;
        }


    }
}
