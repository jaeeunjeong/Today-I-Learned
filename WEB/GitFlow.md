## gitflow 러프하게 정리
git을 master/ release/ dev/ branch/ hotfix 이런 방식으로 구현하는데. 
이슈를 통해서 branch를 만들고, push 해서 하는 방식의 flow!

## git 명령어
-  git merge --abort : 진행 중인 머지 과정 취소하기
- 잘못된 커밋을 롤백하기
  - git log
      - log history 확인하여 잘못된 커밋내용 확인
  - `git reset HEAD^`
      - 제일 첫번째 커밋(커밋날짜 역순-가장최근 커밋)을 지운다.
      - 여러 커밋을 지워야하는 경우 git reset HEAD~2 와 같이 숫자를 기입한다. ( ← 최근 2번째 커밋까지 지움)
      - git log를 보면 지워진 히스토리 확인가능
  - `git reset {commit id}`
      - 내가 되돌리고싶은 커밋 id 까지 reset

  - `git push -f origin {branchName}`
      - origin branch에 해당 내용을 푸시하면 커밋 로그는 사라진다. (-f 옵션 필수 : 강제푸시)
      - 또는 `git push -f` (현재 바라보고 있는 HEAD에 커밋 로그 없이 바로 푸시)
