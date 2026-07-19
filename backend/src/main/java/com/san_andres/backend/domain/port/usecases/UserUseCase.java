package com.san_andres.backend.domain.port.usecases;

import com.san_andres.backend.application.dto.auth.PasswordRequest;
import com.san_andres.backend.domain.models.User;

import java.util.List;

public interface UserUseCase {

    User findByEmail(String email);

    User findById(Long id);

    List<User> findByStatus(String status);

    List<User> findByEmailAndStatus(String email, String status);

    User save( String email,String username, String password, String role);

    User update(Long id , String email,String username, String password,String role);

    User activateUser (Long id);

    User deactivateUser (Long id);

    User blockedUser (Long id);

    User changePassword(Long userId, PasswordRequest request);
}
