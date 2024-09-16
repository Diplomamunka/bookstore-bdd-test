Feature: Author testing
  Testing if the authors api is working fine

  Background:
    Given User data is deleted

  Scenario: Record a new author
    When User records the author 'J. K. Rowling'
    Then User can read the previously recorded author 'J. K. Rowling'

  Scenario: User cannot recorded two authors with the same name
    Given User records the author 'J. K. Rowling'
    Then User cannot record the author 'J. K. Rowling'

  Scenario: Get all authors
    Given User records the author 'J. K. Rowling'
    Given User records the author 'Gárdonyi Géza'
    Given User records the author 'J. R. R. Tolkien'
    Then User can read the following authors:
      | J. K. Rowling    |
      | Gárdonyi Géza    |
      | J. R. R. Tolkien |

  Scenario: Update the recorded author's name
    Given User records the author 'J. K. Rolwing'
    When User updates the previously recorded author's name from 'J. K. Rolwing' to 'J. K. Rowling'
    Then User can read the previously modified author 'J. K. Rowling'

  Scenario: User cannot update an author's name to an existing one
    Given User records the author 'J. K. Rowling'
    Given User records the author 'J. K. R.'
    Then User cannot update the author 'J. K. R.' to 'J. K. Rowling'

  Scenario: Delete an author
    Given User records the author 'J. K. Rowling'
    When User deletes the recorded author 'J. K. Rowling'
    Then User cannot read the author 'J. K. Rowling'

  Scenario: Cannot delete authors if they have a book
    Given User records the following book:
      | title          | price | category | discount | authors       | available |
      | Egri csillagok | 5000  | novel    | 0        | Gárdonyi Géza | true      |
    When User cannot delete the author 'Gárdonyi Géza'
    Then User can read the previously recorded author 'Gárdonyi Géza'