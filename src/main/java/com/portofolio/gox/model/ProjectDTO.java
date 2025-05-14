package com.portofolio.gox.model;

import java.util.List;

public class ProjectDTO {
    private int id ;
    private String projectName;
    private List<String> tags;
    private String description;

    public ProjectDTO (int id, String projectName, List<String> tags, String description){
        this.id = id;
        this.projectName = projectName;
        this.description = description;
        this.tags = tags;
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

    public void setTags(List<String> tags) {
        this.tags = tags;
    }
}
