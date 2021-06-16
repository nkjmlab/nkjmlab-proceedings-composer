package org.nkjmlab.easychair;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.nkjmlab.util.csv.CsvUtils;

public class PRIMA {

  static List<Map<String, String>> papers = CsvUtils
      .readColumnNameMapList(CsvUtils.createDefaultTsvConfig(), new File("submissions.tsv"));
  static List<Map<String, String>> pcs =
      CsvUtils.readColumnNameMapList(CsvUtils.createDefaultCsvConfig(), new File("pcs.csv"));
  static List<Map<String, String>> assignments = CsvUtils
      .readColumnNameMapList(CsvUtils.createDefaultCsvConfig(), new File("assignments.csv"));

  public static void main(String[] args) {

    assignments.stream().forEach(line -> {
      List<String> tmp = new ArrayList<>();
      String id = line.get("id");
      tmp.add(id);

      tmp.add(getTitle(id));
      tmp.add(getAuthors(id));

      line.entrySet().forEach(entry -> {
        if (entry.getValue().equals("*")) {
          String lastName = entry.getKey();
          String s = lastName;
          if (notOPc(lastName)) {
            s += "(" + getRole(lastName) + ")";
          }
          s += ":" + getOrg(lastName);
          tmp.add(s);
        }
      });
      System.out.println(String.join("\t", tmp));
    });

  }

  private static String getOrg(String lastName) {
    try {
      return getPc(lastName).get("org");
    } catch (Exception e) {
      return "unknown";
    }
  }

  private static String getRole(String lastName) {
    try {
      return getPc(lastName).get("role");
    } catch (Exception e) {
      return "unknown";
    }
  }

  private static boolean notOPc(String lastName) {
    try {
      return !getPc(lastName).get("role").equals("ordinary PC member");
    } catch (Exception e) {
      return false;
    }
  }

  private static String getAuthors(String id) {
    return getPaper(id).get("authors");
  }

  private static String getTitle(String id) {
    return getPaper(id).get("title");
  }

  private static Map<String, String> getPc(String lastName) {
    for (Map<String, String> pc : pcs) {
      if (pc.get("last").equals(lastName)) {
        return pc;
      }
    }
    return null;
  }

  private static Map<String, String> getPaper(String id) {
    for (Map<String, String> paper : papers) {
      if (paper.get("id").equals(id)) {
        return paper;
      }
    }
    return null;
  }
}
