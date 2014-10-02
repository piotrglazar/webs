package com.piotrglazar.webs.model.repositories;

import com.piotrglazar.webs.model.entities.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LoanRepository extends JpaRepository<Loan, Long> {

    @Query("select u.loans from WebsUser u where u.username = ?1")
    List<Loan> findByUsername(String username);
}
