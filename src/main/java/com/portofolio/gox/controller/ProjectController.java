package com.portofolio.gox.controller;

import com.portofolio.gox.model.Project;
import com.portofolio.gox.service.FirebaseStorageService;
import com.portofolio.gox.service.ProjectService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/projects")
public class ProjectController {
    private ProjectService projectService;
    private FirebaseStorageService firebaseStorageService;

    ProjectController (ProjectService projectService, FirebaseStorageService firebaseStorageService) {
        this.projectService = projectService;
        this.firebaseStorageService = firebaseStorageService;
    }

    @GetMapping
    public List<Project> getProjectsAll(){
        return projectService.getAllProjects();
    }

    @PostMapping("/upload")
    public String uploadFile (@RequestParam("file") MultipartFile file) {
        try {
            return firebaseStorageService.uploadImage(file);
        } catch (IOException e){
            return "error upload: " + e.getMessage();
        }
    }
    @GetMapping("/byName")
    public List<Project> getProjectByName(@RequestParam(value = "name") String name){
        return projectService.getProjectByName(name);
    }

    @PostMapping("/createProject")
    public Project createProject(
            @RequestParam ("projectName") String projectName,
            @RequestParam ("description") String description,
            @RequestParam ("tags") List<String> tags,
            @RequestParam ("image") MultipartFile file
    ){
        try {
            Project project = new Project(projectName, description, tags,null);
            return projectService.saveProjectWithImage(project, file);
        } catch (IOException e){
            return null;
        }

    }

}
