# File and File System  

## File : 데이터를 단순 저장한 형태 

비휘발성의 보조기억장치(하드디스크)에 저장한다. 

이름을 통해서 접근. 

Linux System 같은 경우에는 장치를 관리하기 위해 파일이라는 이름을 이용하여 관리를 한다.(device special file)

Operation의 종류 : create. Delete. Read, write, reposition, open, close. 
### reposition(lseek) 
file을 읽거나 쓰기 위해 사용하려 할 떄, 현재 사용중인 부분에서 완전 다른 부분을 탐색하여 포인터를 달리하여 파일에 접근해야할 경우, 경로를 다시 탐색해야하는데 이 때 사용하는 연산.
### open/close
파일을 사용하거나, 더 이상 사용하지 않을 때 쓰는 연산
#### open :
 메모리에 올릴 때, 디스크에서 올리는 것이 아니라, metadata를 올린다.

## File attribute = metadata 

파일의 내용이외의 정보로, 파일을 관리하기 위해 사용하는 정보. 

Ex)파일이 생성된 시간, 파일의 크기, 파일의 이름, 파일의 유형,접근 권한등 

## File System 

file을 관리하기 위한 소프트웨어.
파일 자체내용 + metadata 다 저장한다.
계층적으로 저장한다 : root에서부터 탐색.
파일을 파일 시스템에 저장할 때, 디렉토리에 저장하고 그 디렉토리의 시작점에는 root가 있다.
file 저장 방법이나 보호 등을 결정한다.

## directory
파일을 관리하기 위한 공간 파일.
그 공간에 어떤 파일들로 되어 있는지에 대한 정보가 파일의 내용.
metadata를 주로 저장하는데, 전부다 저장될 수도 있고, 일부만 저장 될 수도 있다.(file System에 따라 바뀜)
operator : search for a file, create a file, delete a file, list a directory, rename a file,traverse the file system.
## Partition
OS가 실제로 보는 디스크.
하나의 physical disk에는 여러 개의 partition을 둬서 다양하게 사용하는 것이 일반적.
여러 개의 물리적를 합쳐서 하나의 partition으로 구성하기도 한다.
논리적 디스크에 swaparea나 file system을 설치하는 용도로 사용 가능.
# Directory and Logical Disk  

# open( ) 
File의 metadata를 memory에 올려놓는 것.
file에는 metadata나 내용이 저장되어 있는데, metadata에는 파일의 저장 위치(pointer)가 저장되어있을테니 이 정보를 이용하여 사용하면 된다.
## open((/a/b/c")) 이 수행되는 과정.
1. 사용자 메모리 영역에서 open을 수행하고 싶다고 System call을 보낸다.
2. cpu 제어권이 OS로 넘어가서, global table로 open을 관리하기 시작한다.
3. cpu가 root의 metadata를 알기 떄문에, root를 open한다.
=> metadata에는 파일의 위치 정보들이 존재.
4. root directory를 통해서 a의 metadata를 알 수 있고, 그 정보를 통해, a를 메모리에 올린다.
5. a는 b의 metadata를 알고 있고, 그 안에 있는 b의 위치정보를 바탕으로 b를 가져와서 메모리에 올린다.
6. 메모리에 올라간 b에서 c를 찾을 수 있다. c는 디렉토리가 아니기 때문에 b에는 per-process file descriptor table이 있다. 이 테이블을 바탕으로 read system call에 요청한다.
7. 해당 discription에 대응하는 파일의 meta data에 open 파일을 따라간 다음 file의 위치 정보가 어디있는지 metadata가 갖고 있고, 시작 위치부터 읽기 시작.
8. 해당 메모리에 전달하면 끝.
9. 내용을 읽어서 사용자에게 주는 것이 아니라, os에서 먼저 일부 읽은 다음, 사용자에게 copy하여 전달한다.
### Buffer chach
동일한 파일의 동일한 크기를 요청할 때 유용하게 사용하기 위해 os를 일부 copy한다.
### virtual memory와의 차이점
#### LRU, LFU 사용 가능
File에서는 buffer caching이 꼭 system call을 이용한다. 이 말은 cpu 권한이 os에 넘어가서 사용한다는 것인데, 이전 작업에 대한 정보들이 남아있음을 알 수 있다. 따라서, File system의 buffer caching환경에서는 LRU/LFU 사용 가능하다.
### 다양한 테이블
#### per = process file descriptor table : file discriptor table은 process 마다 따로 존재한다. -> 추가 정리
#### system-wide open file table :  system wide로 전체 시스템에 한 개로 관리되지만 각각의 프로그램들이 파일의 offset을 프로그램 별로 관리하기 위한 공간(table)은 따로 존재. -> 추가 정리
##### metadata가 disk에 있으면, 기본적인 정보만 metadata인데, memory에 올려놓으면, 현ㅐ 프로세스가 파일의 어느 위치에 접근하는지에 대한 offset에 대한 메타데이터도 필요하다.
이 정보는 달라질 수 있기 떄문에, system wide하게 하나만 주어지지만 offset이 다를 수도 있기 때문에 별도로 가져야한다. -> system-wide open file table인가.
# File Protection 
## File protection vs Memory protection
file : 다양한 유형의 사용자가 접근 가능하고, read/write/execute
memory : os만 접근해서 사용하고 read/write이기 떄문에, 크게 사용하지 않음.
## Access control Matrix
Access contril list : list를 이용하여 사용자 유형을 결정.
Capabillity : 사용자별로 접근 권한을 가진 파일들만 나열.
파일에 대한 접근을 요청하면 list는 capabillity권한을 따라가며 탐색한다.
=> 모든 사용자의 접근을 제한 할 수 있지만, over head가 크다.
## Grouping
사용자 그룹을 세 가지(owner, group, public)로 나눠서 표시하고 동일 그룹에 해당하는 파일들에 한해 명령어를 수행한다.(read/write/execute)
권한이 있으면 1, 없으면 0으로 표시
## password
모든 파일이나 디렉토리, 접근마다 password를 둔다.

# File System의 Mounting 
다른 partitioning된 부분에 접근하고자 할 때, mount를 사용한다.
root systemp의 특정 시스템 이름에다가 다른 내용을 마운팅 해주면 그 다른 파일 시스템의 root에 접근하는 모습이 된다.
# Access Methods
직접 접근 : 특정 위치에 접근하는 것이 가능하다.
ex) Harddisk Flash
순차 접근 : 특정 위치에 접근하기 위해서, 순서있게 접근해야하는 것.
ex)Tape
# Allocation of File Data in Disk 
-  sector : Disk에 file을 저장할 때는 동일한 크기로 저장하는 데 그 단위가 sector.
# Contiguous Allocation 
하나의 파일이 디스크 상에서 연속해서 저장되는 방식
임의의 크기의 파일을 블럭단위로 나눠서 저장.
## 장점
- Fast I/O
- 한 번에 seek/lotation으로 많은 데이터를 가져올 수 있다. 
- 프로세스의 swapper 용도로 유용
- 연속적 공간이기 때문에, 공간보다 속도가 더 중요하게 여겨질 때, 사용.
- real time
- 직접 접근이 가능(연속 접근이 가능하기 때문에, 시작 위치를 알면 연산을 통해 접근하고 싶은 곳블럭의 위치를 알 수 있다.)

## 단점
- extenal fragmentation 발생(파일의 크기가 균일하지 않기 때문에, free block가 있을 수 있다. 새로운 파일이 있는데 그 파일이 마땅치 않다면 사용이 불가능하다. 비어있어도 활용 불가.)
- 파일이 커져도 공간이 넉넉하지 않으면 사용이 불가능 할 수 있음. -> 미리 넉넉한 공간을 할당하여 해결 -> 비어있는 공간(free block 발생 = inner fragmentation)
# Linked Allocation 
hole을 방지하기 위해 등장
linked list 구조
파일의 내용이 없으면 끝났다는 표시로 종료.
파일의 시작위치만 디렉토리가 갖고있고, 꼬리 물기 하듯 다음 위치를 파악한다.
## 장점
- inner/extenal fragmentation 없음.
## 단점
- 직접 접근이 불가능
- 시간이 오래 걸림 : 디스크헤드의 이동이 많기 때문
- reliability 문제 : bad sector가 생기면, 유실되는 데이터가 생기기 때문에 리스트가 끊긴다. 
- pointer를 위한 공간때문에 공간의 효율성이 떨어진다. -> FAT로 해결
### FAT = File Allocation Table
포인터를 별도의 위치에 보관하여 단점을 보완
# Indexed Allocation 
디렉토리에 대한 정보를 인덱스를 이용하여 위치 정보를 관리한다.
하나의 블럭에 대한 관련된 위치 정보를 관리한다.
## 장점 
 - hole 문제, 순차 접근 문제 해결
- 인덱스를 통해 접근하기 때문에, 직접 접근도 가능하다.(시작 블럭을 알고 그 안에서 연산을 통해!)
## 단점
- small file의 경우 공간 낭비
- 파일이 너무큰 경우 하나의 block로 저장 할 수 없다.
### bigfile의 경우
문제점 : 파일의 내용이 너무 커서 인덱스 블럭 하나로 부족하다.
#### solve1. linked scheme 
인덱스의 마지막 내용에 도달하면 위치 정보가 저장된 다른 인덱스블럭을 가리켜서 위치 정보를 사용한다.
#### solve2. multi level index 
하나의 인덱스 블럭을 직접 가리키는 것이 아니라 2단계 테이블 페이징이르 사용하듯 사용
단점 : 인덱스를 위한 공간 낭비가 생김.
# UNIX 파일시스템의 구조 
## boot block 
부팅을 위해 필요한 부분
커널 위치를 찾아서 메모리에 올려서 부트를 수행한다.
어느 os건 제일 처음에 실행되어야 하기 때문에 반드시 존재한다.
## super block
file system에 대한 총체적인 정보를 담고 있는 block.
블럭의 비어있는지 여부나, 실제 사용여부를 확인 할 수 있다.
inode block, data block 구간 정보 관리.
## inode list
### inode 
실제 디렉토리는 모든 metadata를 갖고 있는 것이 아니며, 일부분만 갖고 있고, 별도로 따로 보관하는 정보 -> 분명하게 적을 것. 모호 ㅠㅠ
### inode list
파일 하나당 inode 하나씩 관리한다.
파일 이름을 제외한 metadate를 관리한다.
### indirect
유닉스에서 파일의 위치 정보를 구성하기 위해 사용하는 것.
저쟝 용랑이 크면 하나의 level을 둬서 indirect 접근을 한다.
single, double, triple...
## data block
실제 내용을 보관.
# FAT File System 
## FAT 
메타데이터의 위치 정보만 보관하는 것. 그외의 정보는 디렉토리가 갖고 있다.
디스크가 관리하는 갯수만큼 metadata의 위치 정보에 대한 배열을 갖는다.(배열 구조)
직접 접근이 가능 : 배열 구조이기때문에 위치를 알 수 있다.
FAT 배열의 크기는 data block의 크기와 동일.
reliability : 여러 개의 복제본을 둠.

## root directory
....?
# Free-Space Management 
비어있는 block을 관리하기 위함.
## bit map = bit vector
bit를 둬서 free block과 아닌 block를 구분.
0 : 비어있는 값
1 : sector가 저장된 공간
bit를 저장하기 위한 공간이 필요하다.
연속적인 공간을 찾는 것이 쉽다.
한꺼번에 많은 양을 가져올 수 있다.
## LinkedList
free block들을 리스트의 형태로 묶어서 관리하는 것.
포인터를 이용하여 저장하기 때문에 비어있는 블럭끼리 사용한다.
bit를 사용하지 않기 때문에 공간 낭비가 없다.
연속적인 값을 찾기가 어렵다.
## Grouping
linked List의 변형으로, 포인터를 별도로 둬서 첫 번째 free block이 n개의 pointer을 갖고,
n-1 pointer는 free data block을 가리킨다.
마지막 pointer가 가리키는 block은 또 다시 n pointer을 가진다.
어떠한 free block가 index역할을 하여, 또 다른 free block들의 포인터를 저장하여 사용한다.
## counting
프로그램이 연속적으로 사용한 후에 연속적으로 반납한다는 것에서 착안.
프로그램을 연속적으로 사용하기 위해 빈 블럭의 첫번째 위치랑 몇개가 비어있는지 관리하여 사용.
# Directory Implementation 
directory는 file의 meta data를 관리하기 위한 특수 file.
이러한 directory의 관리 방법을 알아보자.
## linear list
파일의 meta data나 이름을 순차적으로 저장한다.
고정된 크기의 배열로 관리한다.
간단한 구현.
순차적으로 찾기 때문에 시간이 많이 걸린다.
## hash table
- hash 함수를 이용하여 저장.
- hash의 결과값을 entry로 파일의 이름과 meta data를 저장하여 사용한다.
### hash
hash 함수를 통해 값이 변형되면 그 구간의 값을 이용해서 검색하는 방식
 - hash 특유의 collision이 발생할 수 있다
## FAT의 meta data 보관
directory 내에 직접 보관하거나 directory에 포인터를 두고 다른 곳에 보관한다.
## long file name 보관
entry는 고정시키고 파일 위치만 관리한다.
파일 이름에 해당하는 필드를 한정시키고, 파일이름이 길다면, 앞부분만 저장한다.
나머지 이름은 포인터를 둬서 디렉토리 파일 저장공간 뒤쪽부터 나머지 이름을 저장한다.

# VFS and NFS 
## VFS
system call Interface
등장 배경 : 파일 시스템에 접근하려면 system call을 해야한다. -> 파일 시스템이 여러 개라면 그 시스템에 맞춰서 system call을 해야할 것 => 비효율적
다양한 File System이 동일한 System Call Interface를 통해 효율적이고 다양하게 접근이 가능하다.
## NFS
Client - Server 구조에서 사용
사용자가 서버에 있는 파일 시스템을 이용하고 싶을 때, 사용자 로컬에는 존재하지 않기 때문에 System Call을 보내도 사용이 불가능하다.
VFS를 통해 확인한 후, 파일 시스템이 존재하지 않으면 NFS module를 이용하여 네트워크 통신을 한 후 서버의 파일을 이용한다.
# Page Cache and Buffer Cache  

file system : 파일의 데이터를 사용자가 요청했을 때, 디스크에서 자기 영역중 일부를 저장해둔게 있으면 바로 거기서 보여줄 수 있는데 그 일부 저장 공간을 buffer cache 라고 한다.
## page cache
virtual memory : 메모리 관리를 할 때 당장 필요한 페이지 프레임만 메모리에 올리고 사용하지 않는 프레임은 내쫓는 방식
page system에서 사용.
swap area
페이지 단위
## Buffer cache
file system : File I/O작업 수행시 디스크의 파일 사스템에서 OS가 대신 파일의 내용을 읽어서 자신의 메모리에 카피해 놓고 전달하는 한다.
이때 카피하는 곳이 buffer cache.
OS가 파일을 요청할 떄, buffer cache에 내용이 있으면 그 내용을 copy하여 사용자에게 넘겨준다.
파일 입출력을 위한 IO 수행 -> 버퍼에 그 내용이 있는지 확인 -> 없으면 디스크에 요청해서 읽어오고 일부 내용을 버퍼에 넣는다.
-> 버퍼에 내용이 있으면 버퍼에 있는 내용을 사용자가 카피하여 사용한다.
## Memory mapped I/O
시스템 콜을 요청한 후에 버퍼에 디스크 주소를 맵핑하여 버퍼에 쓰는 것이 디스크에 쓰는 것과 동일한 결과를 나타내는 것.
페이지 캐시에 카피하고 파일에 맵한다. 
사용자 프로그램이 캐시에다가 쓰는 것이 메모리에 읽고 쓰는 것과 동일.
page fault 가 발생할 수 있음.
커널의 도움없이 메모리에 접근 가능.
## Unified I/O
별도로 구분을 하지 않고 페이지 캐시를 버퍼 캐시로도 쓰고 버퍼 캐시를 페이지 캐시로도 쓰는 것.
# 프로그램의 실행 
0. 파일 시스템에 있는 파일을 실행
0. 프로세스가 됨.
0. virtual memory에 독자적 공간이 생성됨
0. 주소 변환을 해주는 하드웨어(MMU)에 의해 물리적 메모리에 올라감
0. 물리적 메모리에 공간이 할당되고 일부는 swapper에 넘어간다. : process의 코드 부분은 rendomly해서 넘어가지 않음.(이미 파일 시스템에 존재)
0. loader가 프로그램을 올릴때, 실행 파일의 코드 부분을 mmap방식을 이용한다.
- mapping이 되어 있으면 메모리에서 가져온다.
- mapping이 안 되어 있으면 page fault가 일어남
    0. 물리적 메모리에 올리고 가상 페이지가 물리적 메모리의 페이지로 맵핑된다.
    0. 파일에다가 꼭 써주고 쫓아내야함
    0. OS를 이용하지 않고 바로 사용 가능!