package org.nkjmlab.easychair;

import java.io.File;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.nkjmlab.util.csv.CsvUtils;

public class StatusPrinter {
  static List<Map<String, String>> papers = CsvUtils
      .readColumnNameMapList(CsvUtils.createDefaultTsvConfig(), new File("submissions.tsv"));
  static List<Map<String, String>> scores =
      CsvUtils.readColumnNameMapList(CsvUtils.createDefaultTsvConfig(), new File("status.tsv"));

  public static void main(String[] args) {
    List<Paper> result = scores.stream().map(score -> {
      Paper l = new Paper(score.get("id"), getAuthors(score.get("id")), score.get("title"),
          score.get("decision"));
      return l;
    }).sorted(Comparator.comparing(Paper::getFirstAuthorLastName)
        .thenComparing(Paper::getFirstAuthorLastName)).collect(Collectors.toList());

    print(result, "Regular paper$", "Regular Papers (Main Track)");
    print(result, "Regular paper at SS track", "Regular Papers (Social Science Track)");
    print(result, "Short paper.*", "Short paper (Main Track)");

  }

  private static void print(List<Paper> result, String regex, String sec) {
    System.out.println("<h2>" + sec + "</h2>");
    System.out.println("<ul>");
    result.stream().filter(p -> p.getDecision().matches(regex)).forEach(p -> {
      System.out.println("  <li>" + "<span class='authors'>" + p.getAuthors()
          + "</span>. <span class='title'>" + p.getTitle() + "</span></li>");
    });
    System.out.println("</ul>");
    System.out.println("<hr>");
  }

  private static String getAuthors(String id) {
    for (Map<String, String> paper : papers) {
      if (paper.get("id").equals(id)) {
        return paper.get("authors");
      }
    }
    return null;
  }

}
