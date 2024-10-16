Feature: Authentication/user testing
  Testing if the user management and security working okay

  Background:
    Given User data is deleted

  Scenario: Adam hires Mike as Staff
    Given Adam hires Mike
    When Mike sign up and creates a password
    Then Mike can access his profile with his data

  Scenario: Sue signs up
    When Sue sign up and creates a password
    Then Sue can access his profile with her data

  Scenario: Sue updates her data
    Given Sue sign up and creates a password
    When Sue updates her last name to: Williams

  Scenario: Sue cannot update her role
    Given Sue sign up and creates a password
    When Sue cannot update her role

  Scenario: Adam hires Sue as Staff
    Given Sue sign up and creates a password
    When Adam updates Sue's role to Staff

  Scenario: Adam can update Mike's data
    Given Adam hires Mike
    Given Mike sign up and creates a password
    When Adam updates Mike's last name to: Williams

  Scenario: Staff don't have the rights to update user's data
    Given Adam hires Mike
    Given Mike sign up and creates a password
    Given Sue sign up and creates a password
    Then Mike doesn't have the rights to update Sue's last name to: Williams

  Scenario: Customers don't have the rights to update user's data
    Given Adam hires Mike
    Given Mike sign up and creates a password
    Given Sue sign up and creates a password
    Then Sue doesn't have the rights to update Mike's last name to: Williams

  Scenario: Users without sign in cannot update user's data
    Given Sue sign up and creates a password
    Then Anyone cannot update Sue's last name to: Williams

  Scenario: Adam can get all registered users
    Given Adam hires Mike
    Given Mike sign up and creates a password
    Given Sue sign up and creates a password
    Then Adam can get all users

  Scenario: Staff don't have the rights to get all users
    Given Adam hires Mike
    Given Mike sign up and creates a password
    Then Mike doesn't have the rights to get all users

  Scenario: Customers don't have the rights to get all users
    Given Sue sign up and creates a password
    Then Sue doesn't have the rights to get all users

  Scenario: Users without sign in cannot get the registered users
    Given Sue sign up and creates a password
    Then Anyone cannot get all registered users

  Scenario: Adam can delete a user
    Given Sue sign up and creates a password
    When Adam deletes Sue
    Then Sue cannot access her profile

  Scenario: Staff don't have the rights to delete a user
    Given Adam hires Mike
    Given Mike sign up and creates a password
    Given Sue sign up and creates a password
    Then Mike doesn't have the rights to delete Sue

  Scenario: Customers don't have the rights to delete another user
    Given Adam hires Mike
    Given Mike sign up and creates a password
    Given Sue sign up and creates a password
    Then Sue doesn't have the rights to delete Mike

  Scenario: Users without sign in cannot delete a user
    Given Sue sign up and creates a password
    Then Anyone cannot delete Sue