Feature: Book testing
  Testing if the books api is working fine

  Background:
    Given User data is deleted

  Scenario: User can record a book with 6 attributes
    When User records the following book:
      | title          | price | category | discount | authors       | available |
      | Egri csillagok | 5000  | novel    | 0        | Gárdonyi Géza | true      |
    Then User can read the previously recorded book 'Egri csillagok'
    Then User can read the previously recorded author 'Gárdonyi Géza'
    Then User can read the previously recorded book 'Egri csillagok' in the books of 'Gárdonyi Géza'

  Scenario: User can record a book with all attributes
    Given User records the author 'Gárdonyi Géza'
    When User records the following book:
      | title          | price | category | short description                                                                     | discount | authors       | available | release date |
      | Egri csillagok | 5000  | novel    | A novel about the topic how the turkish empire wanted to conquer a castle in Hungary. | 0        | Gárdonyi Géza | true      | 1899         |
    Then User can read the previously recorded book 'Egri csillagok'
    Then User can read the previously recorded book 'Egri csillagok' in the books of 'Gárdonyi Géza'

  Scenario: User uploads an image of the previously recorded book
    Given User records the following book:
      | title          | price | category | discount | authors       | available |
      | Egri csillagok | 5000  | novel    | 0        | Gárdonyi Géza | true      |
    When User uploads an image of the book 'Egri csillagok'
    Then User can download the image of the book 'Egri csillagok'

  Scenario: Get all books
    Given User records the following book:
      | title          | price | category | discount | authors       | available |
      | Egri csillagok | 5000  | novel    | 0        | Gárdonyi Géza | true      |
    Given User records the following book:
      | title                          | price | category | discount | authors       | available |
      | Harry Potter és a bölcsek köve | 4600  | novel    | 0        | J. K. Rowling | true      |
    Then User can read the following books:
      | Egri csillagok                 |
      | Harry Potter és a bölcsek köve |

  Scenario: User updates the previously recorded book
    Given User records the following book:
      | title          | price | category | discount | authors       | available |
      | Egri csillagok | 5000  | novel    | 0        | Gárdonyi Géza | true      |
    When User adds the following description to book 'Egri csillagok': 'A novel about the topic how the turkish empire wanted to conquer a castle in Hungary.'
    Then User can read the previously modified book 'Egri csillagok'

  Scenario: Delete a book
    Given User records the following book:
      | title          | price | category | discount | authors       | available |
      | Egri csillagok | 5000  | novel    | 0        | Gárdonyi Géza | true      |
    When User deletes the recorded book 'Egri csillagok'
    Then User can read the previously recorded author 'Gárdonyi Géza'
    Then User cannot read the book 'Egri csillagok'

  Scenario: Cannot delete category if it has a book
    Given User adds the category 'novella'
    Given User records the following book:
      | title               | price | category | discount | authors         | available |
      | Bede Anna tartozása | 2000  | novella  | 0        | Mikszáth Kálmán | true      |
    When User cannot delete the category 'novella'
    Then User can read the previously recorded book 'Bede Anna tartozása'
    Then User can read the previously created category 'novella'

  Scenario: Cannot delete authors if they have a book
    Given User records the following book:
      | title          | price | category | discount | authors       | available |
      | Egri csillagok | 5000  | novel    | 0        | Gárdonyi Géza | true      |
    When User cannot delete the author 'Gárdonyi Géza'
    Then User can read the previously recorded author 'Gárdonyi Géza'

  Scenario: Delete all books in a category
    Given User adds the category 'novella'
    Given User records the following book:
      | title                     | price | category | discount | authors         | available |
      | Szegény Gélyi János lovai | 2100  | novella  | 0        | Mikszáth Kálmán | true      |
    Given User records the following book:
      | title                                | price | category | discount | authors           | available |
      | Esti Kornál és a bolgár kalauz esete | 1990  | novella  | 0        | Kosztolányi Dezső | true      |
    When User deletes all books in the category 'novella'
    Then All books deleted from the category 'novella'

  Scenario: Delete all an author all books
    Given User records the author 'J. K. Rowling'
    Given User records the following book:
      | title                          | price | category | discount | authors       | available |
      | Harry Potter és a bölcsek köve | 4600  | novel    | 0        | J. K. Rowling | true      |
    Given User records the following book:
      | title                           | price | category | discount | authors       | available |
      | Harry Potter és a félvér herceg | 5000  | novel    | 0        | J. K. Rowling | true      |
    When User deletes all books from the author 'J. K. Rowling'
    Then All books deleted from the author 'J. K. Rowling'

  Scenario: Cannot upload an image bigger than 1MB
    Given User records the following book:
      | title                          | price | category | discount | authors       | available |
      | Harry Potter és a bölcsek köve | 4600  | novel    | 0        | J. K. Rowling | true      |
    When User cannot upload the big image to the book 'Harry Potter és a bölcsek köve'
