
public class QuickSort {
	public static void quickSort(int[] arr) {
		quickSort(arr, 0, arr.length - 1);
	}

	public static void quickSort(int[] arr, int start, int end) {
		// 나눈 파티션의 오른쪽의 첫번째 값
		int part2 = partition(arr, start, end);
		// 하나인 경우는 정렬할 필요가 없다.
		if (start < part2 - 1) {
			quickSort(arr, start, part2 - 1);
		}
		if (part2 < end) {
			quickSort(arr, part2, end);
		}
	}

	public static int partition(int[] arr, int start, int end) {
		int pivot = arr[(start + end) / 2];
		while (start <= end) {
			while (arr[start] < pivot) // 오름 차순으로 잘 정렬된 경우
				start++;
			while (arr[end] > pivot) // 오름 차순으로 잘 정렬된 경우
				end--;
			if (start <= end) {
				swap(arr, start, end);
				start++;
				end--;
			}
		}
		return start;
	}

	public static void swap(int[] arr, int start, int end) {
		int temp = arr[start];
		arr[start] = arr[end];
		arr[end] = temp;
	}

	public static void print(int[] arr) {
		for (int a : arr)
			System.out.print(a + " ");
	}

	public static void main(String[] args) {
		int[] arr = { 5,2,7,6,9,4,1,8,3 };
		print(arr);
		quickSort(arr);
		System.out.println();
		print(arr);
	}
}
