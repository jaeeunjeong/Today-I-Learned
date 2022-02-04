/**
 * 가장 작은 것만 찾기위해 재귀/for문을 이용해서 값을 구한다.
 */
public class SelectionSort {
	public static void init(int[] arr) {
		selectionSortByRecursive(arr, 0);
	}

	public static void selectionSortByRecursive(int[] arr, int start) {
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
		selectionSortByRecursive(arr, start + 1);

	}

	public static void swap(int idx, int start, int[] arr) {
		int temp = arr[idx];
		arr[idx] = arr[start];
		arr[start] = temp;
	}

	public static void print(int[] arr) {
		for (int x : arr)
			System.out.print(x + " ");
	}

	public static void selectionSortByLoop(int[] arr) {
		for (int i = 0; i < arr.length - 1; i++) {
			int max = arr[i];
			int idx = i;
			for (int j = i + 1; j < arr.length; j++) {
				if (max < arr[j]) {
					idx = j;
					max = arr[j];
				}
			}
			max = arr[i];// temp의 의미임
			arr[i] = arr[idx];
			arr[idx] = max;
		}
	}

	public static void main(String[] args) {
		int[] arr = { 20, 22, 1, 26, 11, 23 };
		// init(arr);
		selectionSortByLoop(arr);
		print(arr);
	}
}
