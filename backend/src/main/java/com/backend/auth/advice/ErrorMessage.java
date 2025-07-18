package com.backend.auth.advice;

import java.util.Date;

public record ErrorMessage(int statusCode, Date timestamp, String message, String description) {
}