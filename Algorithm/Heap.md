# HEAP
**complete binary tree** 구조로 되어 최상위 루트에 최솟값/ 최댓값을 저장하고 그 연산을 빠르게 하도록 고안된 자료 구조.  
모든 노드에 대해 부모와 자식 간의 관계를 맺으며 일부 값들만 정렬이 되어 있어 가장 큰/ 가장 작은 몇 개의 값들만 답을 알고자 할 때 많이 사용된다.

## 0 based index vs 1 based index
완전 이진트리의 특성상 인덱스의 규칙을 이용해서 접근할 수 있다.  
```
                         1
            2                         3
      4          5             6            7
   8   9      10   11      12    13      14    15
```
위의 모습과 같은 형태로 연산을 통해 배열이 인덱스에 접근할 수 있다.  

여기서, 루트를 1로 할 건지 0으로 할건지에 따라 자식 노드의 연산이 달라진다.
### 0 based index
부모의 인덱스 = (자식의 인덱스 - 1) /2
왼쪽 자식의 인덱스 = (부모의 인덱스) * 2 + 1  
오른쪽 자식의 인덱스 = (부모의 인덱스) * 2 + 2  
### 1 based index
부모의 인덱스 = (자식의 인덱스) /2  
왼쪽 자식의 인덱스 = (부모의 인덱스) * 2  
오른쪽 자식의 인덱스 = (부모의 인덱스) * 2 + 1  

## 시간복잡도
트리의 높이를 N이라고 하면 계속해서 절반씩 나눠가면서 탐색하고  
삽입(push), 삭제(pop)의 모두 최악의 경우에 Root 노드부터 Leaf 노드 모두를 비교하기에 **O(logN)** 이 된다.

## 배열을 이용한 heap 구현
```
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
		while (current > 0) {
			int parents = (current - 1) / 2;
			
			if(heap[current] >= heap[parents]) break;
			
			int temp = heap[parents];
			heap[parents] = heap[current];
			heap[current] = temp;
			
			current = parents;
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
			System.out.print(heap.heapPop()+" ");
		}
	}
}
```
