package com.example.librarymanagement.app.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.librarymanagement.app.entity.Books;
import com.example.librarymanagement.app.entity.History;
import com.example.librarymanagement.app.repository.AuditRepository;

@Service
public class AuditService {

   @Autowired
   AuditRepository auditRepository;

     public History addRecords(History history) {
        History h = auditRepository.save(history);
        return h;
    }

    public List<History> getHistoryBasedOnQueryParams(Map<String, String> queryParams) {
        Iterable<History> allHistory = auditRepository.findAll();
        return StreamSupport.stream(allHistory.spliterator(), false)
                .filter(history -> {
                    boolean matches = true;
                    for (Map.Entry<String, String> entry : queryParams.entrySet()) {
                        switch (entry.getKey()) {
                            case "userid":
                                matches = matches && history.getUserInfo().getId() == Long.parseLong(entry.getValue());
                                break;
                            case "bookid":
                                matches = matches && history.getBooks().getBookid() == Long.parseLong(entry.getValue());
                                break;
                            default:
                                matches = false;
                        }
                    }
                    return matches;
                })
                .collect(Collectors.toList());
    }

    
    
}
