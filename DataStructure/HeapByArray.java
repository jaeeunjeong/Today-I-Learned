
public class HeapByArray {
	static int MAX_SIZE = 100;
	static int heap[];
	static int heapSize = 0;

	void heapInit() {
		heap = new int[MAX_SIZE];
		heapSize = 0;
	}

	int heapPush(int value) {
		if (heapSize + 1 > MAX_SIZE) {
			System.out.println("QUEUE IS PULL");
			return 0;
		}

		// 마지막 노드에 값 추가
		heap[heapSize] = value;

		// 마지막 노드에 추가한 값을 올바른 위치로 옮긴다.
		int current = heapSize;
		// 부모와 자식의 우선 순위를 비교
		while (current > 0 && heap[current] < heap[(current - 1) / 2]) {
			int temp = heap[(current - 1) / 2];
			heap[(current - 1) / 2] = heap[current];
			heap[current] = temp;
			current = (current - 1) / 2;
		}
		heapSize += 1;
		return 1;
	}

	int heapPop() {
		if (heapSize <= 0)
			return -1;

		int value = heap[0];
		heapSize = heapSize - 1;
		heap[0] = heap[heapSize];
		int current = 0;
		while (current * 2 + 1 < heapSize) {
			int child;
			if (current * 2 + 2 == heapSize)
				child = current * 2 + 1;
			else
				child = heap[current * 2 + 1] < heap[current * 2 + 2] ? current * 2 + 1 : current * 2 + 2;

			if (heap[current] < heap[child])
				break;

			int temp = heap[current];
			heap[current] = heap[child];
			heap[child] = temp;
		}
		return value;
	}

	public static void main(String[] args) {
		HeapByArray heap = new HeapByArray();
		heap.heapInit();
		int[] arr = { 20, 22, 2, 3, 1, 37 };
		for (int i = 0; i < 6; i++) {
			heap.heapPush(arr[i]);
		}
		for (int i = 0; i < 6; i++) {
			System.out.println(heap.heapPop());
		}
	}
}
