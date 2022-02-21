import java.util.*;

public class TriePracticeByMap {
	public static void main(String[] args) {
		Trie trie = new Trie();
		trie.insert("send");
		trie.insert("mail");
		trie.insert("hello");
		trie.insert("new");
		System.out.println(trie.search("receive"));
		System.out.println(trie.search("hello"));
		System.out.println(trie.search("new"));
		System.out.println(trie.search("world"));
	}
}

class Trie {
	Node rootNode = new Node();

	void insert(String str) {
		Node node = this.rootNode;

		for (int i = 0; i < str.length(); i++) {
			node = node.childNode.computeIfAbsent(str.charAt(i), key -> new Node());
		}
		node.endOfWord = true;
	}
	
	boolean search(String str) {
		Node node = this.rootNode;
		
		for (int i = 0; i < str.length(); i++) {
			node = node.childNode.getOrDefault(str.charAt(i), null);
			if(node == null) {
				return false;
			}
		}
		
		return node.endOfWord;
	}

}

class Node {
	Map<Character, Node> childNode = new HashMap<Character, Node>();
	boolean endOfWord;
}
