# Hash Table
검색하고자 하는 Key 값을 **Hash** 라는 함수를 이용해서 배열의 인덱스로 환산하여 검색을 수행할 수 있는 자료 구조
- 해시 함수 : 특정한 규칙을 이용하여 고유의 값을 만들어준다.  
  -> 따라서 해시 함수는 고유의 값을 갖도록 구현해야하는데,  
  아래에서는 LinkedList를 이용해서 값을 구하기 때문에 index만 고유하도록 구현한다.
## LinkedList를 이용해서 HashTable 구현하기
> 구현 코드
```
import java.util.LinkedList;

class HashTable {
	class Node { // 1. HashTable에 사용될 Node 구현
		String key;
		String value;
		
		public Node(String key, String value) {
			this.key = key;
			this.value = value;
		}
		
		void value(String value) {
			this.value = value;
		}
		
		String value() {
			return value;
		}
	}
	
	LinkedList<Node>[] data; // 2. 해시코드 같은 인덱스들은 리스트를 이용해서 관리해준다.
	
	HashTable(int size) {
		this.data = new LinkedList[size];
	}

	/**
	 * key를 일정한 규칙을 이용하여 해시코드로 바꿔준다.
	*/
	int getHashCode(String key) {
		int hashcode = 0;
		for (char c : key.toCharArray())
			hashcode += c;
		return hashcode;
	}

	/**
	 * key를 갖고 index를 가져온다.
	*/
	int convertToIndex(int hashcode) {
		return hashcode % data.length;
	}
	
	/**
	 * hashcode에 맵핑된 index를 통해 가져온 list에서 key에 맞는 값을 가져온다.
	*/
	Node searchKey(LinkedList<Node> list, String key) {
		if (list == null)
			return null;
		for (Node node : list)
			if (node.key.equals(key))
				return node;
		return null;
	}
	
	/**
	 * hashtable에 값을 넣어준다.
	*/
	void put(String key, String value) {
		int hashcode = getHashCode(key);
		int index = convertToIndex(hashcode);
		LinkedList<Node> list = data[index];
		
		if (list == null) {
			list = new LinkedList<>();
			data[index] = list;
		}else if(!get(key).equals("NOT FOUND")) {
			System.out.println("Already Key is in the HashTable");
			return;
		}
		Node node = searchKey(list, key);
		if (node == null) {
			list.addLast(new Node(key, value));
		} else {
			node.value(value);
		}
	}
	
	/**
	 * hashtable에서 값을 가져온다.
	*/
	String get(String key) {
		int hashcode = getHashCode(key);
		int index = convertToIndex(hashcode);
		LinkedList<Node> list = data[index];
		Node node = searchKey(list, key);
		return node == null ? "NOT FOUND" : node.value;
	}
}
public class HashTableByLinkedList {
	public static void main(String[] args) {
		HashTable hashTable = new HashTable(4);
		hashTable.put("plus", "+");
		hashTable.put("minus", "-");
		hashTable.put("multiple", "x");
		hashTable.put("divide", "/");
		System.out.println(hashTable.get("multiple"));
		System.out.println(hashTable.get("multi"));
		hashTable.put("multiple", "*");
		System.out.println(hashTable.get("multiple"));
	}
}

```
> 결과
```
x
NOT FOUND
Already Key is in the HashTable
x
```

## hash와 관련된 알고리즘
- 라빈 카프(Rabin-Karp) 알고리즘
