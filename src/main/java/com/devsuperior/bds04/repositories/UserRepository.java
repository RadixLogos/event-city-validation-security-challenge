package com.devsuperior.bds04.repositories;

import com.devsuperior.bds04.dto.UserProjection;
import com.devsuperior.bds04.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query(nativeQuery = true, value = "SELECT tb_user.email, tb_user.password, tb_role.id AS role_id, tb_role.authority " +
            "FROM tb_user " +
            "INNER JOIN tb_user_role ON tb_user.id = tb_user_role.user_id " +
            "INNER JOIN tb_role ON tb_user_role.role_id = tb_role.id " +
            "WHERE tb_user.email = :email")
    public List<UserProjection> findUserByUsername(String email);

    public User findUserByEmail(String email);

}
