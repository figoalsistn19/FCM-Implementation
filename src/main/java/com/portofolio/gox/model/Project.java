package com.portofolio.gox.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "projects")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String projectName;

    private String description;

    @ElementCollection
    private List<String> tags;

    private String imageUrl;

    public Project() {}

    public Project(String projectName, String description, List<String> tags, String imageUrl){
        this.description = description;
        this.projectName = projectName;
        this.tags = tags;
        this.imageUrl = imageUrl;
    }

    public int getId() {
        return id;
    }

    public List<String> getTags() {
        return tags;
    }

    public String getDescription() {
        return description;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName){
        this.projectName = projectName;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }
}
