# Process

## 정의
실행중인 프로그램

## 프로세스의 상태
- Run : 프로세스가 생성된 상태
- Ready : CPU에 의해 실행되기를 기다리는 상태
- Run : CPU가 프로세스의 명령어를 해독하는 상태
- Wating : CPU에 의해 프로세스가 실행되기를 기다리는 상태
- Blocked : CPU가 실행되다가 I/O 등의 이유로 대기 중인 상태
- Terminated : 종료된 프로세스

## PCB (**Process Control Block)**
- 프로세스의 상태를 담고있는 Block

## Context
- 프로세스의 상태
    - 진행율
- PCB에서 관리한다.

## Context Switching
- CPU가 진행중인 프로세스를 교체하는 작업
## Scheduler
- 장기
    - 어떤 프로세스를 CPU에 올릴지 정하는 스케줄러
- 단기
    - 어떤 프로세스를 CPU가 실행할지 정하는 스케줄러
- 중기
    - 효율적인 사용을 위해 프로세스를 메모리에서 옮기는 스케줄러
