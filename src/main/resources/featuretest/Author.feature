Feature: Author testing
  Testing if the authors api is working fine

  Background:
    Given User data is deleted
    Given Adam hires Mike
    Given Mike sign up and creates a password

  Scenario: Adam records a new author
    When Adam records the author 'J. K. Rowling'
    Then Anyone can read the previously recorded author 'J. K. Rowling'

  Scenario: Mike records a new author
    When Mike records the author 'J. K. Rowling'
    Then Anyone can read the previously recorded author 'J. K. Rowling'

  Scenario: Customers don't have the rights to record a new author
    Given Sue sign up and creates a password
    Then Sue doesn't have the rights to record the author 'J. K. Rowling'

  Scenario: Users without sign in cannot record a new author
    When Anyone is not able to record the author 'J. K. Rowling'

  Scenario: Adam cannot recorded two authors with the same name
    Given Adam records the author 'J. K. Rowling'
    Then Adam cannot record the author 'J. K. Rowling'

  Scenario: Mike cannot recorded two authors with the same name
    Given Mike records the author 'J. K. Rowling'
    Then Mike cannot record the author 'J. K. Rowling'

  Scenario: Get all authors
    Given Mike records the author 'J. K. Rowling'
    Given Mike records the author 'Gárdonyi Géza'
    Given Adam records the author 'J. R. R. Tolkien'
    Then Anyone can read the following authors:
      | J. K. Rowling    |
      | Gárdonyi Géza    |
      | J. R. R. Tolkien |

  Scenario: Adam update the recorded author's name
    Given Mike records the author 'J. K. Rolwing'
    When Adam updates the previously recorded author's name from 'J. K. Rolwing' to 'J. K. Rowling'
    Then Anyone can read the previously modified author 'J. K. Rowling'

  Scenario: Mike update the recorded author's name
    Given Adam records the author 'J. K. Rolwing'
    When Mike updates the previously recorded author's name from 'J. K. Rolwing' to 'J. K. Rowling'
    Then Anyone can read the previously modified author 'J. K. Rowling'

  Scenario: Customers don't have the rights to update an author's name
    Given Sue sign up and creates a password
    Given Adam records the author 'J. K. Rolwing'
    Then Sue doesn't have the rights to update the previously recorded author's name from 'J. K. Rolwing' to 'J. K. Rowling'

  Scenario: Users without sign in cannot update an author's name
    Given Adam records the author 'J. K. Rolwing'
    Then Anyone is not able to update the previously recorded author's name from 'J. K. Rolwing' to 'J. K. Rowling'

  Scenario: Adam cannot update an author's name to an existing one
    Given Adam records the author 'J. K. Rowling'
    Given Mike records the author 'J. K. R.'
    Then Adam cannot update the author 'J. K. R.' to 'J. K. Rowling'

  Scenario: Mike cannot update an author's name to an existing one
    Given Adam records the author 'J. K. Rowling'
    Given Mike records the author 'J. K. R.'
    Then Mike cannot update the author 'J. K. R.' to 'J. K. Rowling'

  Scenario: Adam deletes an author
    Given Mike records the author 'J. K. Rowling'
    When Adam deletes the recorded author 'J. K. Rowling'
    Then Anyone cannot read the author 'J. K. Rowling'

  Scenario: Mike deletes an author
    Given Adam records the author 'J. K. Rowling'
    When Mike deletes the recorded author 'J. K. Rowling'
    Then Anyone cannot read the author 'J. K. Rowling'

  Scenario: Customers don't have the rights to delete an author
    Given Sue sign up and creates a password
    Given Adam records the author 'J. K. Rowling'
    Then Sue doesn't have the rights to delete the author 'J. K. Rowling'

  Scenario: Users without sign in cannot delete an author
    Given Adam records the author 'J. K. Rowling'
    Then Anyone is not able to delete the author 'J. K. Rowling'

  Scenario: Adam cannot delete authors if they have a book
    Given Mike records the following book:
      | title          | price | category | discount | authors       | available |
      | Egri csillagok | 5000  | novel    | 0        | Gárdonyi Géza | true      |
    When Adam cannot delete the author 'Gárdonyi Géza'
    Then Anyone can read the previously recorded author 'Gárdonyi Géza'

  Scenario: Mike cannot delete authors if they have a book
    Given Adam records the following book:
      | title          | price | category | discount | authors       | available |
      | Egri csillagok | 5000  | novel    | 0        | Gárdonyi Géza | true      |
    When Mike cannot delete the author 'Gárdonyi Géza'
    Then Anyone can read the previously recorded author 'Gárdonyi Géza'