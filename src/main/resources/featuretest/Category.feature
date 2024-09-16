Feature: Category testing
  Testing if the categories api is working fine

  Background:
    Given User data is deleted

  Scenario: Get all default categories
    Given The default categories
    Then User can read the following categories:
      | drama      |
      | romantic   |
      | novel      |
      | crime      |
      | fantasy    |
      | humor      |
      | sci-fi     |
      | adventure  |
      | fairy tale |
      | juvenile   |

  Scenario: Adding a new category
    When User adds the category 'folktale'
    Then User can read the previously created category 'folktale'

  Scenario: User cannot create two category with the same name
    Given The default categories
    Given User adds the category 'folktale'
    When User cannot create the category 'folktale'
    Then User can read the following categories:
      | drama      |
      | romantic   |
      | novel      |
      | crime      |
      | fantasy    |
      | humor      |
      | sci-fi     |
      | adventure  |
      | fairy tale |
      | juvenile   |
      | folktale   |

  Scenario: Deleting a category if not having any book
    Given User adds the category 'folktale'
    When User deletes the created category 'folktale'
    Then User can read the following categories:
      | drama      |
      | romantic   |
      | novel      |
      | crime      |
      | fantasy    |
      | humor      |
      | sci-fi     |
      | adventure  |
      | fairy tale |
      | juvenile   |

  Scenario: Cannot delete category if it has a book
    Given User adds the category 'novella'
    Given User records the following book:
      | title               | price | category | discount | authors         | available |
      | Bede Anna tartozása | 2000  | novella  | 0        | Mikszáth Kálmán | true      |
    When User cannot delete the category 'novella'
    Then User can read the previously recorded book 'Bede Anna tartozása'
    Then User can read the previously created category 'novella'