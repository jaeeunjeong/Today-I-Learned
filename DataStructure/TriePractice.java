public class TriePractice {
	public static void main(String[] args) {
		 _Trie trie = new _Trie();
		 trie.insert("send");
		 trie.insert("mail");
		 trie.insert("hello");
		 trie.insert("new");
		 System.out.println(trie.find("receive"));
		 System.out.println(trie.find("hello"));
		 System.out.println(trie.find("new"));
		 System.out.println(trie.find("world"));
	}
}

class _Trie {
	boolean end;
	_Trie[] tries = new _Trie[26];

	_Trie() {
		end = false;
		for (int i = 0; i < 26; i++) {
			tries[i] = null;
		}
	}
	void insert(String str) {
		insert(str, 0);
	}
	
	boolean find(String str) {
		return find(str, 0);
	}

	void insert(String str, int idx) {
		if(str.length() == idx) {//종료인지 확인
			end = true;
			return;
		}
		int cur = str.charAt(idx) - 'a';
		if(tries[cur] == null) tries[cur] = new _Trie();
		tries[cur].insert(str, idx+1);
	}
	
	boolean find(String str, int idx) {
		if(str.length() == idx) {//종료인지 확인
			if(end == true) return true;
			return false;
		}
		int cur = str.charAt(idx) - 'a';
		if(tries[cur] == null) return false;
		return tries[cur].find(str, idx+1);
	}
}
