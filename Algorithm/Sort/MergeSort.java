/**
 * MergeSort
 * 분할 정복을 이용.
 * 가장 작은 단위로 쪼개서 그 값을 바꾼 후 다시 합쳐준다.
 */
public class MergeSort {
	// merge init
	public static void mergeSort(int[] arr1) {
		int[] arr2 = new int[arr1.length];
		mergeSort(arr1, arr2, 0, arr1.length - 1);
	}

	/**
	 * 작은 단위로 나눠주기.
	 * @param arr1
	 * @param arr2
	 * @param start
	 * @param end
	 */
	public static void mergeSort(int[] arr1, int[] arr2, int start, int end) {
		if (start < end) {
			int mid = (start + end) / 2;
			mergeSort(arr1, arr2, start, mid);
			mergeSort(arr1, arr2, mid + 1, end);
			merge(arr1, arr2, start, mid, end);
		}
	}

	/**
	 * 본격적으로 정렬하면서 합쳐주기.
	 * @param arr1
	 * @param arr2
	 * @param start
	 * @param mid
	 * @param end
	 */
	public static void merge(int[] arr1, int[] arr2, int start, int mid, int end) {
		//copy
		for (int i = start; i <= end; i++) {
			arr2[i] = arr1[i];
		}

		int part1 = start;
		int part2 = mid + 1;
		int idx = start;

		while (part1 <= mid && part2 <= end) {
			if (arr2[part1] <= arr2[part2]) {
				arr1[idx] = arr2[part1];
				part1++;
			} else {
				arr1[idx] = arr2[part2];
				part2++;
			}
			idx++;
		}
		//앞쪽 배열에 데이터가 남은 경우 붙여주기.
		//뒤쪽 배열은 이미 남아있어서 상관 없음(덮어쓰기하듯 코딩함)
		for (int i = 0; i <= mid - part1; i++)
			arr1[idx + i] = arr2[part2 + i];
	}

	public static void print(int[] arr) {
		for (int data : arr)
			System.out.print(data + " ");
		System.out.println();
	}

	public static void main(String[] args) {
		int[] arr = { 5, 5, 2, 6, 2, 6, 7 };
		print(arr);
		mergeSort(arr);
		print(arr);
	}
}
