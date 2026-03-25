package com.smartcampus.smartcampus.repository;

import com.smartcampus.smartcampus.entity.User;
import com.smartcampus.smartcampus.enums.Program;
import com.smartcampus.smartcampus.enums.School;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    List<User> findBySchool(School school);

    List<User> findBySchoolAndProgramIn(School school, List<Program> programs);

    List<User> findBySchoolAndProgramInAndYearIn(
        School school,
        List<Program> programs,
        List<Integer> years
    );
}