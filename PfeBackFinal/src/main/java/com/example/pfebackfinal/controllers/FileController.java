package com.example.pfebackfinal.controllers;

import com.example.pfebackfinal.services.ClassroomService;
import com.example.pfebackfinal.services.CourseService;
import com.example.pfebackfinal.services.ICourseService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
@RestController
@RequestMapping("/folder")
@AllArgsConstructor

@CrossOrigin(origins="http://localhost:4200")

public class FileController {
    private static final Logger logger = LoggerFactory.getLogger(CoursesController.class);

    private final ICourseService iCourseService;
    private final CourseService courseService;
    private final ClassroomService classroomService;

    private static final String UPLOAD_DIRECTORY = "C:/Users/lenovo/IdeaProjects/uploads/";
    @GetMapping("/files/{fileName:.+}")
    public ResponseEntity<Resource> serveFile(@PathVariable String fileName) {
        try {
            Path filePath = Paths.get(UPLOAD_DIRECTORY).resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists() || resource.isReadable()) {
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            } else {
                logger.error("File not found: {}", fileName);
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (MalformedURLException e) {
            logger.error("Error while serving file: {}", e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
