package com.portofolio.gox.service;

import com.portofolio.gox.model.Project;
import com.portofolio.gox.repository.ProjectRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final FirebaseStorageService firebaseStorageService;

    public ProjectService(ProjectRepository projectRepository, FirebaseStorageService firebaseStorageService){
        this.projectRepository = projectRepository;
        this.firebaseStorageService = firebaseStorageService;
    }

    public List<Project> getAllProjects (){
        return projectRepository.findAll();
    }

    public Project saveProjectWithImage(Project project, MultipartFile file) throws IOException {
        String imageUrl = firebaseStorageService.uploadImage(file);
        project.setImageUrl(imageUrl);
        return projectRepository.save(project);
    }

    public List<Project> getProjectByName (String name){
        return projectRepository.findByProjectName(name);
    }
}
