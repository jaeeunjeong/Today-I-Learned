## gitflow 러프하게 정리
git을 master/ release/ dev/ branch/ hotfix 이런 방식으로 구현하는데. 
이슈를 통해서 branch를 만들고, push 해서 하는 방식의 flow!

## git 명령어
- git merge --abort : 진행 중인 머지 과정 취소하기
- 잘못된 커밋을 롤백하기
  1. git log
      - log history 확인하여 잘못된 커밋내용 확인
  2. `git reset HEAD^`
      - 제일 첫번째 커밋(커밋날짜 역순-가장최근 커밋)을 지운다.
      - 여러 커밋을 지워야하는 경우 git reset HEAD~2 와 같이 숫자를 기입한다. ( ← 최근 2번째 커밋까지 지움)
      - git log를 보면 지워진 히스토리 확인가능
  3. `git reset {commit id}`
      - 내가 되돌리고싶은 커밋 id 까지 reset

  4. `git push -f origin {branchName}`
      - origin branch에 해당 내용을 푸시하면 커밋 로그는 사라진다. (-f 옵션 필수 : 강제푸시)
      - 또는 `git push -f` (현재 바라보고 있는 HEAD에 커밋 로그 없이 바로 푸시)
- commit author 바꾸기
  1. `git log` 입력한 후 바꾸려느 커밋의 commit_id 값을 확인한다.
  2. `git rebase -i commit_id` 1.에서 확인하 commit_id 입력한다.
  3. 에디터 상단에 보이는 **pick** 를 **e** 또는 **edit**로 변경 후 `:wq!`로 나온다. !이 때 변경한 **e** 갯수 기억!
  4. `git commit --amend --author="jaeeunjeong <jejeong000@gmail.com>"` 이러한 방식으로 원하는 작성자로 변경 
  5. 에디터가 뜰 수도 있는데 이때 커밋 메시지 변경도 가능하다.
  6. `git rebase --continue`를 입력한 후, **4**의 과정을 **3에 e를 변경한 횟수**만큼 반복한다.
  7. 
``
% git rebase --continue.
Successfully rebased and.updated refs/heads/main.
``. 
`git rebase --continue`를 입력하였을 떄, ``Successfully rebased and updated refs/heads/main.`` 메시지가 보인다면 모든 커밋 기록을 수정 완료한 것.
8.`git push -f origin {변경을 원하는 브랜치}`를 통해 변경을 완료하여 push한다.
