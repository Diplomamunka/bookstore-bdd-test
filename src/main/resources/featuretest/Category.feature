Feature: Category testing
  Testing if the categories api is working fine

  Background:
    Given User data is deleted
    Given Adam hires Mike
    Given Mike sign up and creates a password

  Scenario: Get all default categories
    Given The default categories
    Then Anyone can read the following categories:
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

  Scenario: Adam adds a new category
    When Adam adds the category 'folktale'
    Then Anyone can read the previously created category 'folktale'

  Scenario: Mike adds a new category
    When Mike adds the category 'folktale'
    Then Anyone can read the previously created category 'folktale'

  Scenario: Customers don't have the rights to add a new category
    Given Sue sign up and creates a password
    Then Sue doesn't have the rights to add the category 'folktale'

  Scenario: Users without sign in cannot add a new category
    When Anyone cannot add the category 'folktale'

  Scenario: Adam cannot create two category with the same name
    Given The default categories
    Given Mike adds the category 'folktale'
    When Adam cannot create the category 'folktale'
    Then Anyone can read the following categories:
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

  Scenario: Mike cannot create two category with the same name
    Given The default categories
    Given Adam adds the category 'folktale'
    When Mike cannot create the category 'folktale'
    Then Anyone can read the following categories:
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

  Scenario: Adam deletes a category if not having any book
    Given Mike adds the category 'folktale'
    When Adam deletes the created category 'folktale'
    Then Anyone can read the following categories:
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

  Scenario: Mike deletes a category if not having any book
    Given Adam adds the category 'folktale'
    When Mike deletes the created category 'folktale'
    Then Anyone can read the following categories:
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

  Scenario: Customers don't have the rights to delete a category
    Given Sue sign up and creates a password
    Given Adam adds the category 'folktale'
    Then Sue doesn't have the rights to delete the category 'folktale'

  Scenario: Users without sign in cannot delete a category
    Given Adam adds the category 'folktale'
    Then Anyone is not able to delete the category 'folktale'

  Scenario: Adam cannot delete category if it has a book
    Given Mike adds the category 'novella'
    Given Adam records the following book:
      | title               | price | category | discount | authors         | available |
      | Bede Anna tartozása | 2000  | novella  | 0        | Mikszáth Kálmán | true      |
    When Adam cannot delete the category 'novella'
    Then Anyone can read the previously recorded book 'Bede Anna tartozása'
    Then Anyone can read the previously created category 'novella'

  Scenario: Mike cannot delete category if it has a book
    Given Adam adds the category 'novella'
    Given Mike records the following book:
      | title               | price | category | discount | authors         | available |
      | Bede Anna tartozása | 2000  | novella  | 0        | Mikszáth Kálmán | true      |
    When Mike cannot delete the category 'novella'
    Then Anyone can read the previously recorded book 'Bede Anna tartozása'
    Then Anyone can read the previously created category 'novella'