Feature: Book testing
  Testing if the books api is working fine

  Background:
    Given User data is deleted
    Given Adam hires Mike
    Given Mike sign up and creates a password

  Scenario: Adam can record a book with 6 attributes
    When Adam records the following book:
      | title          | price | category | discount | authors       | available |
      | Egri csillagok | 5000  | novel    | 0        | Gárdonyi Géza | true      |
    Then Anyone can read the previously recorded book 'Egri csillagok'
    Then Anyone can read the previously recorded author 'Gárdonyi Géza'
    Then Anyone can read the previously recorded book 'Egri csillagok' in the books of 'Gárdonyi Géza'

  Scenario: Mike can record a book with 6 attributes
    When Mike records the following book:
      | title          | price | category | discount | authors       | available |
      | Egri csillagok | 5000  | novel    | 0        | Gárdonyi Géza | true      |
    Then Anyone can read the previously recorded book 'Egri csillagok'
    Then Anyone can read the previously recorded author 'Gárdonyi Géza'
    Then Anyone can read the previously recorded book 'Egri csillagok' in the books of 'Gárdonyi Géza'

  Scenario: Adam can record a book with all attributes
    Given Mike records the author 'Gárdonyi Géza'
    When Adam records the following book:
      | title          | price | category | short description                                                                     | discount | authors       | available | release date | tags                        |
      | Egri csillagok | 5000  | novel    | A novel about the topic how the turkish empire wanted to conquer a castle in Hungary. | 0        | Gárdonyi Géza | true      | 1899         | törökök, háború, megszállás |
    Then Anyone can read the previously recorded book 'Egri csillagok'
    Then Anyone can read the previously recorded book 'Egri csillagok' in the books of 'Gárdonyi Géza'

  Scenario: Mike can record a book with all attributes
    Given Adam records the author 'Gárdonyi Géza'
    When Mike records the following book:
      | title          | price | category | short description                                                                     | discount | authors       | available | release date | tags                        |
      | Egri csillagok | 5000  | novel    | A novel about the topic how the turkish empire wanted to conquer a castle in Hungary. | 0        | Gárdonyi Géza | true      | 1899         | törökök, háború, megszállás |
    Then Anyone can read the previously recorded book 'Egri csillagok'
    Then Anyone can read the previously recorded book 'Egri csillagok' in the books of 'Gárdonyi Géza'

  Scenario: :Adam can record a book with multiple authors
    Given Mike records the author 'Beatriz Williams'
    When Adam records the following book:
      | title           | price | category | discount | authors                                      | available |
      | The Glass Ocean | 3000  | novel    | 0        | Lauren Willig, Karen White, Beatriz Williams | true      |
    Then Anyone can read the previously recorded book 'The Glass Ocean'
    Then Anyone can read the following authors:
      | Beatriz Williams |
      | Lauren Willig    |
      | Karen White      |
    Then Anyone can read the previously recorded book 'The Glass Ocean' in the books of
      | Beatriz Williams |
      | Lauren Willig    |
      | Karen White      |

  Scenario: Mike can record a book with multiple authors
    Given Adam records the author 'Beatriz Williams'
    When Mike records the following book:
      | title           | price | category | discount | authors                                      | available |
      | The Glass Ocean | 3000  | novel    | 0        | Lauren Willig, Karen White, Beatriz Williams | true      |
    Then Anyone can read the previously recorded book 'The Glass Ocean'
    Then Anyone can read the following authors:
      | Beatriz Williams |
      | Lauren Willig    |
      | Karen White      |
    Then Anyone can read the previously recorded book 'The Glass Ocean' in the books of
      | Beatriz Williams |
      | Lauren Willig    |
      | Karen White      |

  Scenario: Customers don't have the rights to record a book
    Given Adam records the author 'Gárdonyi Géza'
    Given Sue sign up and creates a password
    Then Sue doesn't have the rights to record the following book:
      | title          | price | category | discount | authors       | available |
      | Egri csillagok | 5000  | novel    | 0        | Gárdonyi Géza | true      |

  Scenario: Users without sign in cannot record a book
    When Anyone cannot record the following book:
      | title          | price | category | discount | authors       | available |
      | Egri csillagok | 5000  | novel    | 0        | Gárdonyi Géza | true      |


  Scenario: Adam uploads an image of the previously recorded book
    Given Mike records the following book:
      | title          | price | category | discount | authors       | available |
      | Egri csillagok | 5000  | novel    | 0        | Gárdonyi Géza | true      |
    When Adam uploads an image of the book 'Egri csillagok'
    Then Anyone can download the image of the book 'Egri csillagok'

  Scenario: Mike uploads an image of the previously recorded book
    Given Adam records the following book:
      | title          | price | category | discount | authors       | available |
      | Egri csillagok | 5000  | novel    | 0        | Gárdonyi Géza | true      |
    When Mike uploads an image of the book 'Egri csillagok'
    Then Anyone can download the image of the book 'Egri csillagok'

  Scenario: Customers don't have the rights to upload image to books
    Given Sue sign up and creates a password
    Given Adam records the following book:
      | title          | price | category | discount | authors       | available |
      | Egri csillagok | 5000  | novel    | 0        | Gárdonyi Géza | true      |
    Then Sue doesn't have the rights to upload image to the book 'Egri csillagok'

  Scenario: Users without sign in cannot upload image to books
    Given Adam records the following book:
      | title          | price | category | discount | authors       | available |
      | Egri csillagok | 5000  | novel    | 0        | Gárdonyi Géza | true      |
    Then Anyone cannot upload image to the book 'Egri csillagok'

  Scenario: Get all books
    Given Adam records the following book:
      | title          | price | category | discount | authors       | available |
      | Egri csillagok | 5000  | novel    | 0        | Gárdonyi Géza | true      |
    Given Mike records the following book:
      | title                          | price | category | discount | authors       | available |
      | Harry Potter és a bölcsek köve | 4600  | novel    | 0        | J. K. Rowling | true      |
    Then Anyone can read the following books:
      | Egri csillagok                 |
      | Harry Potter és a bölcsek köve |

  Scenario: Adam updates the previously recorded book
    Given Mike records the following book:
      | title          | price | category | discount | authors       | available |
      | Egri csillagok | 5000  | novel    | 0        | Gárdonyi Géza | true      |
    When Adam adds the following description to book 'Egri csillagok': 'A novel about the topic how the turkish empire wanted to conquer a castle in Hungary.'
    Then Anyone can read the previously modified book 'Egri csillagok'

  Scenario: Mike updates the previously recorded book
    Given Adam records the following book:
      | title          | price | category | discount | authors       | available |
      | Egri csillagok | 5000  | novel    | 0        | Gárdonyi Géza | true      |
    When Mike adds the following description to book 'Egri csillagok': 'A novel about the topic how the turkish empire wanted to conquer a castle in Hungary.'
    Then Anyone can read the previously modified book 'Egri csillagok'

  Scenario: Adam updates the previously recorded book's category
    Given Mike records the following book:
      | title          | price | category | discount | authors       | available |
      | Egri csillagok | 5000  | fantasy  | 0        | Gárdonyi Géza | true      |
    When Adam changes the category of the book 'Egri csillagok' to 'novel'
    Then Anyone can read the previously modified book 'Egri csillagok'

  Scenario: Mike updates the previously recorded book's category
    Given Adam records the following book:
      | title          | price | category | discount | authors       | available |
      | Egri csillagok | 5000  | fantasy  | 0        | Gárdonyi Géza | true      |
    When Mike changes the category of the book 'Egri csillagok' to 'novel'
    Then Anyone can read the previously modified book 'Egri csillagok'

  Scenario: Customers don't have to rights to update a book
    Given Sue sign up and creates a password
    Given Adam records the following book:
      | title          | price | category | discount | authors       | available |
      | Egri csillagok | 5000  | fantasy  | 0        | Gárdonyi Géza | true      |
    Then Sue doesn't have the rights to update the description of the book 'Egri csillagok': 'A novel about the topic how the turkish empire wanted to conquer a castle in Hungary.'

  Scenario: Users without sign in cannot update a book
    Given Adam records the following book:
      | title          | price | category | discount | authors       | available |
      | Egri csillagok | 5000  | fantasy  | 0        | Gárdonyi Géza | true      |
    Then Anyone cannot update the description of the book 'Egri csillagok': 'A novel about the topic how the turkish empire wanted to conquer a castle in Hungary.'

  Scenario: Adam deletes a book
    Given Mike records the following book:
      | title          | price | category | discount | authors       | available |
      | Egri csillagok | 5000  | novel    | 0        | Gárdonyi Géza | true      |
    When Adam deletes the recorded book 'Egri csillagok'
    Then Anyone can read the previously recorded author 'Gárdonyi Géza'
    Then Anyone cannot read the book 'Egri csillagok'

  Scenario: Mike deletes a book
    Given Adam records the following book:
      | title          | price | category | discount | authors       | available |
      | Egri csillagok | 5000  | novel    | 0        | Gárdonyi Géza | true      |
    When Mike deletes the recorded book 'Egri csillagok'
    Then Anyone can read the previously recorded author 'Gárdonyi Géza'
    Then Anyone cannot read the book 'Egri csillagok'

  Scenario: Customers doesn't have the rights to delete a book
    Given Sue sign up and creates a password
    Given Adam records the following book:
      | title          | price | category | discount | authors       | available |
      | Egri csillagok | 5000  | novel    | 0        | Gárdonyi Géza | true      |
    Then Sue doesn't have the rights to delete the book 'Egri csillagok'

  Scenario: Users without sign in cannot delete a book
    Given Adam records the following book:
      | title          | price | category | discount | authors       | available |
      | Egri csillagok | 5000  | novel    | 0        | Gárdonyi Géza | true      |
    Then Anyone cannot delete the book 'Egri csillagok'

  Scenario: Adam deletes all books in a category
    Given Mike adds the category 'novella'
    Given Adam records the following book:
      | title                     | price | category | discount | authors         | available |
      | Szegény Gélyi János lovai | 2100  | novella  | 0        | Mikszáth Kálmán | true      |
    Given Mike records the following book:
      | title                                | price | category | discount | authors           | available |
      | Esti Kornál és a bolgár kalauz esete | 1990  | novella  | 0        | Kosztolányi Dezső | true      |
    When Adam deletes all books in the category 'novella'
    Then All books deleted from the category 'novella'

  Scenario: Mike deletes all books in a category
    Given Mike adds the category 'novella'
    Given Adam records the following book:
      | title                     | price | category | discount | authors         | available |
      | Szegény Gélyi János lovai | 2100  | novella  | 0        | Mikszáth Kálmán | true      |
    Given Mike records the following book:
      | title                                | price | category | discount | authors           | available |
      | Esti Kornál és a bolgár kalauz esete | 1990  | novella  | 0        | Kosztolányi Dezső | true      |
    When Mike deletes all books in the category 'novella'
    Then All books deleted from the category 'novella'

  Scenario: Adam deletes an author's all books
    Given Adam records the author 'J. K. Rowling'
    Given Mike records the following book:
      | title                          | price | category | discount | authors       | available |
      | Harry Potter és a bölcsek köve | 4600  | novel    | 0        | J. K. Rowling | true      |
    Given Adam records the following book:
      | title                           | price | category | discount | authors       | available |
      | Harry Potter és a félvér herceg | 5000  | novel    | 0        | J. K. Rowling | true      |
    When Adam deletes all books from the author 'J. K. Rowling'
    Then All books deleted from the author 'J. K. Rowling'

  Scenario: Mike deletes an author's all books
    Given Adam records the author 'J. K. Rowling'
    Given Mike records the following book:
      | title                          | price | category | discount | authors       | available |
      | Harry Potter és a bölcsek köve | 4600  | novel    | 0        | J. K. Rowling | true      |
    Given Adam records the following book:
      | title                           | price | category | discount | authors       | available |
      | Harry Potter és a félvér herceg | 5000  | novel    | 0        | J. K. Rowling | true      |
    When Mike deletes all books from the author 'J. K. Rowling'
    Then All books deleted from the author 'J. K. Rowling'

  Scenario: Adam cannot upload an image bigger than 1MB
    Given Adam records the following book:
      | title                          | price | category | discount | authors       | available |
      | Harry Potter és a bölcsek köve | 4600  | novel    | 0        | J. K. Rowling | true      |
    When Adam cannot upload the big image to the book 'Harry Potter és a bölcsek köve'

  Scenario: Mike cannot upload an image bigger than 1MB
    Given Adam records the following book:
      | title                          | price | category | discount | authors       | available |
      | Harry Potter és a bölcsek köve | 4600  | novel    | 0        | J. K. Rowling | true      |
    When Mike cannot upload the big image to the book 'Harry Potter és a bölcsek köve'