package com.bilgeadam.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ErrorMessage {
    private int code;
    private String message;
    @Builder.Default
    private LocalDateTime dateTime = LocalDateTime.now();
}
