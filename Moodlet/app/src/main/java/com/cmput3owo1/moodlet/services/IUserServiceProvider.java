package com.cmput3owo1.moodlet.services;

import com.cmput3owo1.moodlet.models.FollowRequest;
import com.cmput3owo1.moodlet.models.User;
import java.util.ArrayList;

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
     * Interface for a listener that receives the results of a search for users. The search result
     * contains the list of users that match the searchText.
     */
    interface OnUserSearchListener {
        void onSearchResult(ArrayList<User> searchResult, String searchText);
    }

    /**
     * Interface for a listener that listens to the success of a follow request. The listener is
     * invoked when the follow request is sent to the specified user.
     */
    interface OnFollowRequestListener {
        void onRequestSuccess(User user);
    }

    /**
     * Interface for a listener that receives the username of the follower after accepting the request.
     */
    interface OnAcceptRequestListener {
        void onAcceptRequestSuccess(String newFollowerUsername);
    }

    /**
     * Interface for a listener to constantly update whenever a request is sent from one user to another.
     */
    interface OnRequestsUpdateListener {
        void onRequestsUpdate(ArrayList<FollowRequest> newRequests);
    }

    /**
     * This function is called to check if there is an existing instance of the logged in user.
     * @return Returns true if there is a logged in user; false otherwise.
     */
    boolean hasPreviousLogin();

    /**
     * This function first checks if the username is taken. If the username is not taken,
     * the account will be created, otherwise it will notify the user that their username is already taken
     *
     * @param user Details of the user to register
     * @param password Password of Account to register with.
     * @param listener Registration listener passed from fragment
     */
    void validateUsernameAndCreateUser(User user, String password, RegistrationListener listener);

    /**
     * This is a wrapper function that is called to login a user with their email and password.
     *
     * @param txt_email    Email to login with.
     * @param txt_password Password to login with.
     * @param listener     Login listener passed from fragment
     */
    void loginUser(String txt_email, String txt_password, LoginListener listener);

    /**
     * This is a wrapper function that is called to logout the current signed in user
     */
    void logoutUser();

    /**
     * Search for users of Moodlet that begin with the searchText. Pass the results to the listener.
     * @param searchText The username prefix to search with
     * @param listener The listener to pass the user search results to
     */
    void searchForUsers(String searchText, OnUserSearchListener listener);

    /**
     * Send a follow request to the user specified.
     * @param user The user to send the follow request to
     * @param listener The listener to inform of a success
     */
    void sendFollowRequest(User user, OnFollowRequestListener listener);

    /**
     * Gets the request updates whenever a request is sent from one user to another.
     * @param listener The listener to inform a request update.
     */
    void getRequestUpdates(OnRequestsUpdateListener listener);

    /**
     * Deletes the follow request from the Firestore database.
     * @param request Request that is sent from one user to another
     */
    void deleteFollowRequest(FollowRequest request);

    /**
     * Accepting the follow request being sent from one user to another.
     * @param request Request that is sent from one user to another
     * @param listener The listener to inform accepting the request
     */
    void acceptFollowRequest(FollowRequest request, OnAcceptRequestListener listener);

}