class LinkedList {
	static int index = 0;
	private ListNode head;

	static class ListNode {
		int data;
		ListNode next = null;
	}

	LinkedList() {
		this.head = null;
	}

	void add(int d) {
		if(head == null) {
			head = new ListNode();
			head.data = d;
			return;
		}
		ListNode end = new ListNode();
		end.data = d;
		ListNode header = head;
		while (header.next != null) {
			header = header.next;
		}
		header.next = end;
	}

	ListNode remove(int positionToRemove) {
		if(head == null) return head;
		if(positionToRemove == 0) {
			head = head.next;
			return head;
		}
		ListNode node = head;
		for (int i = 0; i < positionToRemove - 1; i++) {
			if(head == null) return head;
			node = node.next;
		}
		node.next = node.next.next;
		return head;
	}

	boolean contain(int data) {
		boolean result = false;
		ListNode node = head;
		while(node.next != null) {
			if(node.data == data) return true;
			node = node.next;
		}
		return result;
	}
	
	void retrieve() {
		if(head == null) {
			System.out.println("null");
			return;
		}
		ListNode node = head;
		while(node.next != null) {
			System.out.print(node.data+"->");
			node = node.next;
		}
		System.out.println(node.data);
	}
}
