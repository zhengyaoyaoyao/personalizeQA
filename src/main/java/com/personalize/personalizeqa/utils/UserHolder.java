package com.personalize.personalizeqa.utils;

import com.personalize.personalizeqa.dto.UserDTO;

public class UserHolder {
    private static final ThreadLocal<UserDTO> tl = new ThreadLocal<>();
    public static void saveUser(UserDTO userId) {tl.set(userId);}
    public static UserDTO getUser(){return tl.get();}
    public static void removeUser(){
        tl.remove();
    }
}
