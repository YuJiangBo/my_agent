package com.yu.my_agent.controller;

import com.yu.my_agent.entity.postgresql.DocumentVector;
import com.yu.my_agent.entity.postgresql.dto.SaveDocumentRequest;
import com.yu.my_agent.entity.postgresql.dto.SearchRequest;
import com.yu.my_agent.service.VectorDocumentService;

import org.springframework.ai.document.Document;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// VectorController.java
@RestController
@RequestMapping("/api/vector")
public class VectorController {
    
    @Autowired
    private VectorDocumentService vectorDocumentService;
    
    @PostMapping("/documents")
    public ResponseEntity<DocumentVector> saveDocument(
            @RequestBody SaveDocumentRequest request) {
        DocumentVector doc = vectorDocumentService.saveDocument(
            request.getContent(), 
            request.getVector(), 
            request.getMetadata()
        );
        return ResponseEntity.ok(doc);
    }
    
    @PostMapping("/search")
    public ResponseEntity<List<DocumentVector>> searchDocuments(
            @RequestBody SearchRequest request) {
        List<DocumentVector> results = vectorDocumentService.searchSimilarDocuments(
            request.getQueryVector(), 
            request.getLimit()
        );
        return ResponseEntity.ok(results);
    }
    
    @GetMapping("/search-ai")
    public ResponseEntity<List<Document>> searchDocumentsWithAI(
            @RequestParam String query) {
        List<Document> results = vectorDocumentService.searchSimilarDocumentsWithAI(query);
        return ResponseEntity.ok(results);
    }
}