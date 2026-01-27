//package com.shiftplanner.shiftplanner_api.repository;
//
//import com.shiftplanner.shiftplanner_api.domain.User;
//import org.springframework.boot.webmvc.autoconfigure.WebMvcProperties;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//import java.util.Optional;
//import java.util.UUID;
//
//@Repository
//public interface UserRepository extends JpaRepository<User, UUID> {
//
//
//    Optional<User> findByEmailIgnoreCase(String email);
//    List<User> findUserByNameIgnoreCase(String name);
//    List<User> findByRolle(User.Rolle rolle);
//}
