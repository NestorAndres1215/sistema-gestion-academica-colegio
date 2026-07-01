package com.san_andres.backend.domain.port.usecases;

import com.san_andres.backend.application.dto.auth.PasswordRequest;
import com.san_andres.backend.domain.enums.UserStatus;
import com.san_andres.backend.domain.models.User;

import java.util.List;

public interface UserUseCase {

    User findByEmail(String email);

    User findById(String id);

    List<User> findByStatus(UserStatus userStatus);

    List<User> findByEmailAndStatus(String email, UserStatus userStatus);

    User save(String id , String email,String username, String password, String role);

    User update(String id , String email,String username, String password,String role);

    User activateUser (String id);

    User deactivateUser (String id);

    User changePassword(String userId, PasswordRequest request);
}
