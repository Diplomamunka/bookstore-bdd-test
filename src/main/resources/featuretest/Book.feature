Feature: Book testing
  Testing if the books api is working fine

  Background:
    Given User data is deleted

  Scenario: User can record a book with 6 attributes
    When User records the following book with required values:
      | title          | price | category | discount | authors       | available |
      | Egri csillagok | 5000  | novel    | 0        | Gárdonyi Géza | true      |
    Then User can read the previously recorded book 'Egri csillagok'
    Then User can read the previously recorded author 'Gárdonyi Géza'