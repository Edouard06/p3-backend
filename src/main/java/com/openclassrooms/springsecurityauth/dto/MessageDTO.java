package com.openclassrooms.springsecurityauth.dto;

public class MessageDTO {
    private String message;
    private Long userId;
    private Long rentalId;

    public MessageDTO() {}

    public MessageDTO(String message, Long userId, Long rentalId) {
        this.message = message;
        this.userId = userId;
        this.rentalId = rentalId;
    }

    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public Long getUserId() {
        return userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    public Long getRentalId() {
        return rentalId;
    }
    public void setRentalId(Long rentalId) {
        this.rentalId = rentalId;
    }
}
