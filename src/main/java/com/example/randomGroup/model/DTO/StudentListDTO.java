
package com.example.randomGroup.model.DTO;

public class StudentListDTO {
    private Long id;
    private String name;
    private Long userId;

    public StudentListDTO() {
    }

    public StudentListDTO(Long id, String name, Long userId) {
        this.id = id;
        this.name = name;
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}