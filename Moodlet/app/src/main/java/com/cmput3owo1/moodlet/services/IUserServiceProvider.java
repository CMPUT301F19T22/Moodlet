package com.cmput3owo1.moodlet.services;

/**
 * Interface that abstracts user service functions. It contains functions for
 * user registration and login. User registration functions includes validating
 * usernames, creating the account and putting their information into the database.
 * Login functions include login and making checking if there was a previous instance
 * of a logged in account
 */
public interface IUserServiceProvider {
    /**
     * Interface that creates a listener for the register fragment. It contains
     * functions that are defined in the register fragment. These functions help
     * with the flow of the registration process.
     */
    interface RegistrationListener {
        void onRegistrationSuccess();
        void onRegistrationFailure();
        void onDatabaseAccessFailure();
        void onUsernameIsTaken();
    }

    /**
     * Interface that creates a listener for the login fragment. It contains
     * functions that are defined in the login fragment. These functions help
     * with the flow of the login process
     */
    interface LoginListener {
        void onLoginSuccess();
        void onLoginFailure();
    }

    /**
     * This function is called to check if there is an existing instance of the logged in user.
     */
    boolean hasPreviousLogin();


    /**
     * This function first checks if the username is taken. If the username is not taken,
     * the account will be created, otherwise it will notify the user that their username is already taken
     * @param username Username to register with.
     * @param email Email to register with.
     * @param password Password of Account to register with.
     * @param fullname Full name of user registering.
     * @param listener Registration listener passed from fragment
     */
    void validateUsernameAndCreateUser(final String username, final String email, final String password,
                                              final String fullname, final RegistrationListener listener);

    /**
     * This a wrapper function that is called to create a user account with their email and password.
     * @param username Username to register with.
     * @param email Email to register with.
     * @param password Password of Account to register with.
     * @param fullname Full name of user registering.
     * @param listener Registration listener passed from fragment
     */
    void createUser(final String username, final String email, String password, final String fullname, final RegistrationListener listener);

    /**
     * This function is called to put the registered user into the database.
     * @param username Username to register with.
     * @param email Email to register with.
     * @param fullname Full name of user registering.
     * @param listener Registration listener passed from fragment
     * @return none
     */
    void putUserIntoDB(final String email, final String fullname, final String username, final RegistrationListener listener);

    /**
     * This is a wrapper function that is called to login a user with their email and password.
     * @param txt_email Email to login with.
     * @param txt_password Password to login with.
     * @param listener Login listener passed from fragment
     */
    void loginUser(String txt_email, String txt_password, final LoginListener listener);

    }