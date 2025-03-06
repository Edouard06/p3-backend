package com.openclassrooms.springsecurityauth.dto;

public class MessageDTO {
    private String message;
    private Integer user_id;
    private Long rental_id;

    public MessageDTO() {
    }

    public MessageDTO(String message, Integer user_id, Long rental_id) {
        this.message = message;
        this.user_id = user_id;
        this.rental_id = rental_id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public Long getRental_id() {
        return rental_id;
    }

    public void setRental_id(Long rental_id) {
        this.rental_id = rental_id;
    }
}
