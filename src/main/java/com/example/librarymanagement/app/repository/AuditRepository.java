package com.example.librarymanagement.app.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.example.librarymanagement.app.entity.History;

import java.util.List;
import java.util.Optional;

public interface AuditRepository extends CrudRepository<History, Long> {

    @Query(value = "SELECT * FROM librarymanagementschema.history h where h.userid=:userid and h.bookid=:bookid order by auditid desc limit 1", nativeQuery = true)
    public Optional<History> findByUserAndBook(@Param("userid") Long userid, @Param("bookid") Long bookid);

    @Query("SELECT h FROM History h " +
    "LEFT JOIN FETCH h.userInfo " +
    "LEFT JOIN FETCH h.books")
    public List<History> findAllWithUserAndBook();

    @Query(value = "SELECT * FROM librarymanagementschema.history h where h.userid=:userid and h.bookid=:bookid order by auditid desc limit 1", nativeQuery = true)
    public History findAlreadyBorrowed(@Param("userid") Long userid, @Param("bookid") Long bookid);


}
