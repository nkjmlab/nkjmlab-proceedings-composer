package org.nkjmlab.easychair;

public class Paper {

  private String authors;
  private String id;
  private String decision;
  private String title;

  public Paper(String id, String authors, String title, String decision) {
    this.id = id;
    this.authors = authors;
    this.title = title;
    this.decision = decision;
  }

  public String getAuthors() {
    return authors;
  }

  public String getId() {
    return id;
  }

  public String getDecision() {
    return decision;
  }

  public String getTitle() {
    return title;
  }

  public String getFirstAuthorLastName() {
    return getLastName(getFirstAuthor());
  }

  public String getFirstAuthorFirstName() {
    return getFirstName(getFirstAuthor());
  }

  private String getLastName(String name) {
    String[] names = name.split(" ");
    return names[names.length - 1];
  }

  private String getFirstName(String name) {
    String[] names = name.split(" ");
    return names[0];
  }

  private String getFirstAuthor() {
    return authors.split(",|and")[0];
  }

}
