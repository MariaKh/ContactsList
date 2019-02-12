package com.wowapp.contactslist.domain.models;

public class Contact {
    private String firstName;
    private String lastName;
    @AnnotationStatus.Status
    private String statusIcon = AnnotationStatus.AWAY;
    private String statusMessage;

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getStatusIcon() {
        return statusIcon;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

}
