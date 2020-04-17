package com.techelevator.model;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotBlank;

/**
 * User
 */
public class User {
    @NotBlank(message = "Email is required")
    private String email;
    @NotBlank(message = "First name is required")
    private String firstName;
    @NotBlank(message = "Last name is required")
    private String lastName;

    @NotBlank(message = "Phone is required")
    private String phoneNumber;

    @NotBlank(message = "Role is required")
    private String role;
    private long id;

    @NotBlank(message = "Password is required")
    private String password;
    private String confirmPassword;

    private int numberOfAssignedRequests;

    @AssertTrue(message = "Passwords must match")
    public boolean isPasswordMatching() {
        if (password != null) {
            return password.equals(confirmPassword);
        }
        return true;
    }

    public int getNumberOfAssignedRequests() {
        return numberOfAssignedRequests;
    }

    public void setNumberOfAssignedRequests(int numberOfAssignedRequests) {
        this.numberOfAssignedRequests = numberOfAssignedRequests;
    }

    public String getPassword() {
        return password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    /**
     * @return the username
     */
    public String getEmail() {
        return email;
    }
    public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

    /**
     * @return the id
     */
    public long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(final long id) {
        this.id = id;
    }

    /**
     * @return the role
     */
    public String getRole() {
        return role;
    }

    /**
     * @param role the role to set
     */
    public void setRole(final String role) {
        this.role = role;
    }

    /**
     * @param username the username to set
     */
    public void setEmail(final String email) {
        this.email = email;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public void setConfirmPassword(final String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}