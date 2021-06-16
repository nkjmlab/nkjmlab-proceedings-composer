package org.nkjmlab.easychair;

import java.util.function.BiFunction;

public class SelectionSort {
  public static void main(String[] args) {
    Object[] data = {3, 9, 4, 7, 2, 5, 1, 8, 0, 6};
    printArray(data);

    sort(data, (Object i, Object j) -> {
      return true;
    });

    printArray(data);
  }

  private static void sort(Object[] data, BiFunction<Object, Object, Boolean> comparator) {
    for (int i = 0; i < data.length - 1; i++) {
      int min = i;
      for (int j = i + 1; j < data.length; j++) {
        if (comparator.apply(data[j], data[min])) {
          min = j;
        }
      }
      swap(i, min, data);
    }

  }

  private static void swap(int i, int min, Object[] data) {
    if (i == min) {
      return;
    }
    Object tmp = data[min];
    data[min] = data[i];
    data[i] = tmp;
  }

  private static void printArray(Object[] n) {
    for (Object i : n) {
      System.out.print(i + " ");
    }
    System.out.println();
  }
}
