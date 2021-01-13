package com.feri.alessandro.attsw.project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.feri.alessandro.attsw.project.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

}
