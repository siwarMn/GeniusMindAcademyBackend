package com.pfeproject.GeniusMind.Controller;

import com.pfeproject.GeniusMind.Entity.CoursEntity;
import com.pfeproject.GeniusMind.Repository.CoursRepository;
import com.pfeproject.GeniusMind.dto.CourseResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriUtils;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

// This controller handles all file operations for courses
// It allows uploading, downloading, listing, and deleting PDF files
@RestController
@RequestMapping("/api/v1/auth/File")
@AllArgsConstructor
@Slf4j
public class FileController {

    @Autowired
    private CoursRepository coursRepository;

    // Upload a new PDF file
    // POST /api/v1/auth/File/Add
    @PostMapping("/Add")
    public ResponseEntity<CourseResponse> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            log.info("Uploading file: " + file.getOriginalFilename());

            // Create a new entity to save
            CoursEntity entity = new CoursEntity();
            entity.setFileName(file.getOriginalFilename());
            entity.setFileContent(file.getBytes());

            // Save to database
            CoursEntity savedEntity = coursRepository.save(entity);

            // Return only the metadata, not the file bytes
            CourseResponse response = new CourseResponse(
                savedEntity.getId(),
                savedEntity.getFileName()
            );

            return ResponseEntity.ok(response);
        } catch (IOException e) {
            log.error("Error uploading file: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Get all courses WITHOUT file content (just metadata)
    // This is important because sending all PDF bytes would be too slow
    // GET /api/v1/auth/File/GETAll
    @GetMapping("/GETAll")
    public List<CourseResponse> getAllCourses() {
        List<CoursEntity> entities = coursRepository.findAll();
        List<CourseResponse> responses = new ArrayList<>();

        // Convert each entity to a response (without file bytes)
        for (CoursEntity entity : entities) {
            CourseResponse response = new CourseResponse(
                entity.getId(),
                entity.getFileName()
            );
            responses.add(response);
        }

        return responses;
    }

    // Download a PDF file by ID
    // This returns the actual PDF bytes for viewing or downloading
    // GET /api/v1/auth/File/{id}
    @GetMapping("/{id}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable Long id) {
        Optional<CoursEntity> optionalEntity = coursRepository.findById(id);
        if (optionalEntity.isPresent()) {
            CoursEntity entity = optionalEntity.get();
            String encodedFileName = UriUtils.encode(entity.getFileName(), StandardCharsets.UTF_8);

            HttpHeaders headers = new HttpHeaders();
            // Use PDF content type so browsers can display it
            headers.setContentType(MediaType.APPLICATION_PDF);
            // Use inline so browser opens it, not downloads automatically
            headers.setContentDisposition(ContentDisposition.builder("inline").filename(encodedFileName).build());

            return ResponseEntity.ok().headers(headers).body(entity.getFileContent());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Download a PDF file for saving to device (forces download)
    // GET /api/v1/auth/File/download/{id}
    @GetMapping("/download/{id}")
    public ResponseEntity<byte[]> downloadFileForSave(@PathVariable Long id) {
        Optional<CoursEntity> optionalEntity = coursRepository.findById(id);
        if (optionalEntity.isPresent()) {
            CoursEntity entity = optionalEntity.get();
            String encodedFileName = UriUtils.encode(entity.getFileName(), StandardCharsets.UTF_8);

            HttpHeaders headers = new HttpHeaders();
            // Use octet-stream to force download
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            // Use attachment to force browser to download
            headers.setContentDisposition(ContentDisposition.builder("attachment").filename(encodedFileName).build());

            return ResponseEntity.ok().headers(headers).body(entity.getFileContent());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Delete a course/file by ID
    // DELETE /api/v1/auth/File/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteFile(@PathVariable Long id) {
        try {
            coursRepository.deleteById(id);
            return ResponseEntity.ok().build();
        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity.notFound().build();
        }
    }
}


