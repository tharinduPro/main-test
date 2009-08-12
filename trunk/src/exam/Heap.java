package exam;
/**
 * JAVA排序算法实现代码-堆(Heap)排序。
 * 
 * 
 */
public class Heap{
  public static int[] Heap = { 10, 32, 1, 9, 5, 7, 12, 0, 4, 3 }; // 预设数据数组

  public static void main(String args[]) {
    int i; // 循环计数变量
    int Index = Heap.length; // 数据索引变量

    System.out.print("排序前:");
    for (i = 1; i < Index - 1; i++)
      System.out.printf("%3s", Heap[i]);
    System.out.println("");

    HeapSort(Index - 2); // 堆排序

    System.out.print("排序后: ");
    for (i = 1; i < Index - 1; i++)
      System.out.printf("%3s", Heap[i]);
    System.out.println("");
  }

  /**
   * 建立堆
   */
  public static void CreateHeap(int Root, int Index) {
    int i, j; // 循环计数变量
    int Temp; // 暂存变量
    int Finish; // 判断堆是否建立完成

    j = 2 * Root; // 子节点的Index
    Temp = Heap[Root]; // 暂存Heap的Root 值
    Finish = 0; // 预设堆建立尚未完成

    while (j <= Index && Finish == 0) {
      if (j < Index) // 找最大的子节点
        if (Heap[j] < Heap[j + 1])
          j++;
      if (Temp >= Heap[j])
        Finish = 1; // 堆建立完成
      else {
        Heap[j / 2] = Heap[j]; // 父节点 = 目前节点
        j = 2 * j;
      }
    }
    Heap[j / 2] = Temp; // 父节点 = Root值
  }

  public static void HeapSort(int Index) {
    int i, j, Temp;
    // 将二叉树转成Heap
    for (i = (Index / 2); i >= 1; i--)
      CreateHeap(i, Index);

    // 开始进行堆排序
    for (i = Index - 1; i >= 1; i--) {
      Temp = Heap[i + 1]; // Heap的Root值和最后一个值交换
      Heap[i + 1] = Heap[1];
      Heap[1] = Temp;
      CreateHeap(1, i); // 对其余数值重建堆

      System.out.print("排序中: ");
      for (j = 1; j <= Index; j++)
        System.out.printf("%3s",Heap[j]);
      System.out.println("");
    }
  }
}
