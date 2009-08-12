package exam;
/**
 * JAVA排序算法实现代码-堆(Heap)排序。
 * 
 * 
 */
public class Heap{
  public static int[] heap = { 10, 32, 1, 9, 5, 7, 12, 0, 4, 3 }; // 预设数据数组

  public static void main(String args[]) {
    int i; // 循环计数变量
    int Index = heap.length-2; // 数据索引变量

    System.out.print("排序前:");
    for (i = 1; i < Index-1; i++)
      System.out.printf("%3s", heap[i]);
    System.out.println("");

    HeapSort(Index - 2); // 堆排序

    System.out.print("排序后: ");
    for (i = 1; i < Index - 1; i++)
      System.out.printf("%3s", heap[i]);
    System.out.println("");
  }

  /**
   * 建立堆
   */
  public static void createHeap(int root, int index) {
    int i, leftChild; // 循环计数变量
    int rootValue; // 暂存变量
    boolean createFinished = false; // 判断堆是否建立完成

    leftChild = 2*root; // 子节点的Index
    rootValue = heap[root]; // 暂存Heap的Root 值

    while (leftChild <= index && createFinished) {
      if ( leftChild < index && heap[leftChild] < heap[leftChild + 1]) { // 找最大的子节点
    	  leftChild++;
      }
      if (rootValue >= heap[leftChild]) {
    	  createFinished = true;
      }
      else {
        heap[ leftChild / 2] = heap[ leftChild ]; 
        leftChild = 2*leftChild;
      }
    }
    heap[leftChild/2] = rootValue; 
  }

  public static void HeapSort(int Index) {
    int i, j, Temp;
    // 将二叉树转成Heap
    for (i = (Index / 2); i >= 1; i--)
    	createHeap(i, Index);

    // 开始进行堆排序
    for (i = Index - 1; i >= 1; i--) {
      Temp = heap[i + 1]; // Heap的Root值和最后一个值交换
      heap[i + 1] = heap[1];
      heap[1] = Temp;
      createHeap(1, i); // 对其余数值重建堆

      System.out.print("排序中: ");
      for (j = 1; j <= Index; j++)
        System.out.printf("%3s",heap[j]);
      System.out.println("");
    }
  }
}
