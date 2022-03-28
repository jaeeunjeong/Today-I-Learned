public class BubbleSort {
	public static void bubbleSort(int[] arr) {
		bubbleSort(arr, arr.length);
	}

	public static void bubbleSort(int[] arr, int last) {
		if (last > 0) {
			for (int i = 1; i < last; i++) {
				if (arr[i - 1] > arr[i])
					swap(arr, i - 1, i);
			}
			bubbleSort(arr, last - 1);
		}
	}

	public static void swap(int[] arr, int src, int target) {
		int temp = arr[src];
		arr[src] = arr[target];
		arr[target] = temp;
	}

	public static void main(String[] args) {
		int[] arr = new int[] { 20, 22, 03, 29, 12, 19 };
		bubbleSort(arr);
		for (int a : arr)
			System.out.println(a);
	}
}
