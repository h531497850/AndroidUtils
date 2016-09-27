package android.hqs.tool;

public class SortTool {
	
	/**
	 * 插入排序
	 * @param unsorted
	 */
	public static void insertion(int[] unsorted) {
		for (int i = 1; i < unsorted.length; i++) {
			if (unsorted[i - 1] > unsorted[i]) {
				int temp = unsorted[i];
				int j = i;
				while (j > 0 && unsorted[j - 1] > temp) {
					unsorted[j] = unsorted[j - 1];
					j--;
				}
				unsorted[j] = temp;
			}
		}
	}
	
	/**
	 * 快速排序
	 * @param unsorted
	 * @param low
	 * @param high
	 */
	public static void quick(int[] unsorted, int low, int high) {
		int loc = 0;
		if (low < high) {
			loc = partition(unsorted, low, high);
			quick(unsorted, low, loc - 1);
			quick(unsorted, loc + 1, high);
		}
	}
	private static int partition(int[] unsorted, int low, int high) {
		int pivot = unsorted[low];
		while (low < high) {
			while (low < high && unsorted[high] > pivot)
				high--;
			unsorted[low] = unsorted[high];
			while (low < high && unsorted[low] <= pivot)
				low++;
			unsorted[high] = unsorted[low];
		}
		unsorted[low] = pivot;
		return low;
	}
	
	/**
	 * 桶排序
	 * 1),已知其区间,例如[1..10],学生的分数[0...100]等</br>
	 * 2),如果有重复的数字,则需要 List<int>数组,这里举的例子没有重复的数字
	 * @param unsorted 待排数组
	 * @param maxNumber 待排数组中的最大数,如果可以提供的话
	 * @return
	 */
	public static int[] bucket(int[] unsorted, int maxNumber) {
		if (maxNumber == 0) {
			maxNumber = 99;	
		}
		int[] sorted = new int[maxNumber + 1];
		for (int i = 0; i < unsorted.length; i++) {
			sorted[unsorted[i]] = unsorted[i];
		}
		return sorted;
	}
	
	/**
	 * 基数排序</br>
	 * 约定:待排数字中没有0,如果某桶内数字为0则表示该桶未被使用,输出时跳过即可
	 * @param unsorted 待排数组
	 * @param array_x 桶数组第一维长度
	 * @param array_y 桶数组第二维长度
	 */
	public static void radix(int[] unsorted, int array_x, int array_y){
		
	}
	
	/**
	 * 鸽巢排序
	 * @param unsorted 待排数组
	 * @param maxNumber 待排数组中的最大数,如果可以指定的话
	 * @return
	 */
	public static int[] pogeon(int[] unsorted, int maxNumber) {
		if (maxNumber == 0) {
			maxNumber = 10;
		}
		/*
         * pogeonHole[10] = 4; 的含意是
         * 在待排数组中有4个10出现,同理其它
         */
        int[] pogeonHole = new int[maxNumber + 1];
        for (@SuppressWarnings("unused") int i : pogeonHole) {
			i++;
		}
        return pogeonHole;
    }


}
