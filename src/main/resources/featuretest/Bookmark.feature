Feature: Bookmark testing
  Test if the bookmark feature is working

  Background:
    Given User data is deleted
    Given Adam hires Mike
    Given Mike sign up and creates a password

  Scenario: Sue can bookmark a book
    Given Sue sign up and creates a password
    Given Adam records the following book:
      | title          | price | category | discount | authors       | available |
      | Egri csillagok | 5000  | novel    | 0        | Gárdonyi Géza | true      |
    Then Sue bookmarks the book: 'Egri csillagok'

  Scenario: Sue can get all bookmarked books
    Given Sue sign up and creates a password
    Given Adam records the following book:
      | title          | price | category | discount | authors       | available |
      | Egri csillagok | 5000  | novel    | 0        | Gárdonyi Géza | true      |
    Given Adam records the following book:
      | title                          | price | category | discount | authors       | available |
      | Harry Potter és a bölcsek köve | 4600  | novel    | 0        | J. K. Rowling | true      |
    Given Sue bookmarks the book: 'Egri csillagok'
    Given Sue bookmarks the book: 'Harry Potter és a bölcsek köve'
    Then Sue can get all bookmarks

  Scenario: Sue can delete a bookmark
    Given Sue sign up and creates a password
    Given Adam records the following book:
      | title          | price | category | discount | authors       | available |
      | Egri csillagok | 5000  | novel    | 0        | Gárdonyi Géza | true      |
    Given Adam records the following book:
      | title                          | price | category | discount | authors       | available |
      | Harry Potter és a bölcsek köve | 4600  | novel    | 0        | J. K. Rowling | true      |
    Given Sue bookmarks the book: 'Egri csillagok'
    Given Sue bookmarks the book: 'Harry Potter és a bölcsek köve'
    Then Sue can delete the bookmark for 'Egri csillagok'

  Scenario: Users without sign in cannot bookmark a book
    Given Adam records the following book:
      | title          | price | category | discount | authors       | available |
      | Egri csillagok | 5000  | novel    | 0        | Gárdonyi Géza | true      |
    Then Anyone cannot bookmark the book: 'Egri csillagok'

  Scenario: Users without sign in cannot get the bookmarks
    When Anyone cannot get the bookmarks

  Scenario: Users without sign in cannot delete a bookmark
    Given Adam records the following book:
      | title          | price | category | discount | authors       | available |
      | Egri csillagok | 5000  | novel    | 0        | Gárdonyi Géza | true      |
    Then Anyone cannot delete the bookmark for 'Egri csillagok'