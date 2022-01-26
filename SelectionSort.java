
public class SelectionSort {
	/**
	 * 가장 작은 것을 발견 할 떄까지 재귀적으로 호출한다.
	 */
	public static void init(int[] arr) {
		selectionSort(arr, 0);
	}

	public static void selectionSort(int[] arr, int start) {
		if (start == arr.length)
			return;

		int min = arr[start];
		int min_idx = start;
		for (int i = start; i < arr.length; i++) {
			int cur = arr[i];
			if (min > cur) {
				min = cur;
				min_idx = i;
			}
		}
		swap(min_idx, start, arr);
		selectionSort(arr, start + 1);

	}

	public static void swap(int idx, int start, int[] arr) {
		int temp = arr[idx];
		arr[idx] = arr[start];
		arr[start] = temp;
	}

	public static void print(int[] arr) {
		for (int x : arr)	System.out.print(x + " ");
	}

	public static void main(String[] args) {
		int[] arr = { 20, 22, 1, 26, 11, 23 };
		init(arr);
		print(arr);
	}
}
