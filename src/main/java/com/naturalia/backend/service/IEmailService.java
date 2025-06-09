package com.naturalia.backend.service;

public interface IEmailService {
    void sendReservationConfirmation(String to, String content);
}
