package com.eventsphere.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String resource, Long id) {
        super(resource + "Not found with ID : " + id);
    }

    public ResourceNotFoundException(String resource) {
        super(resource);
    }
}
