package com.example.demo.utils;

import lombok.Getter;
import org.springframework.http.HttpStatus;


public class ApiUtils {
    public static <T> ApiResult<T> success(T response) {
        return new ApiResult<>(true, response, null);
    }

    public static ApiResult<?> error(String message, HttpStatus status) {
        return new ApiResult<>(false, null, new ApiError(message, status));
    }

    public static ApiResult<?> error(Throwable message, HttpStatus status) {
        return new ApiResult<>(false, null, new ApiError(message, status));
    }

    @Getter
    public static class ApiError {
        private final String message;
        private final int status;

        public ApiError(String message, HttpStatus status) {
            this.message = message;
            this.status = status.value();
        }

        public ApiError(Throwable throwable, HttpStatus status) {
            this(throwable.getMessage(), status);
        }

        @Override
        public String toString() {
            return "ApiError{" +
                    "message='" + message + '\'' +
                    ", status=" + status +
                    '}';
        }
    }

    @Getter
    public static class ApiResult<T> {
        private final boolean success;
        private final T response;
        private final ApiError error;

        public ApiResult(boolean success, T response, ApiError error) {
            this.success = success;
            this.response = response;
            this.error = error;
        }

        @Override
        public String toString() {
            return "ApiResult{" +
                    "success=" + success +
                    ", response=" + response +
                    ", error=" + error +
                    '}';
        }
    }
}
