- Flux는 Mono의 리스트 형태이다.

```java
@GetMapping("/event/{id}")
    Mono<List<Event>> hello(@PathVariable Long id) {
        Long id1 = 1L;
        Long id2 = 2L;
        List<Event> list = Arrays.asList(new Event(id1, "event" + id1), new Event(id2, "event" + id2));
        return Mono.just(list);
        // 하나의 데이터에만 맵핑
    }

    @GetMapping(value = "/events")
    Flux<Event> hello() {
        Long id1 = 1L;
        Long id2 = 2L;
        return Flux.just(new Event(id1, "event" + id1), new Event(id2, "event" + id2)).log();
    }

    @GetMapping(value = "/events2", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    Flux<Event> hello2() {
        Long id1 = 1L;
        Long id2 = 2L;
        List<Event> list = Arrays.asList(new Event(id1, "event" + id1), new Event(id2, "event" + id2));
        return Flux.fromIterable(list).log();
    }

    @GetMapping(value = "/events3", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    Flux<Event> hello3() {
        Flux<Event> es = Flux.
                <Event, Long>generate(() -> 1L, (id, synchronousSink) -> {
            synchronousSink.next(new Event(id, "value" + id));
            return id + 1;
        }).log()
                .take(5);
        Flux<Long> interval = Flux.interval(Duration.ofSeconds(1)).log();
        return Flux.zip(es, interval).map(tu -> tu.getT1());
    }

    @Data
    @AllArgsConstructor
    public static class Event {
        long id;
        String value;
    }
```

```java
curl http://localhost:8080/events
[{"id":1,"value":"event1"},{"id":2,"value":"event2"}]

curl http://localhost:8080/events2   
data:{"id":1,"value":"event1"}

data:{"id":2,"value":"event2"}

curl http://localhost:8080/events3
data:{"id":1,"value":"value1"}

data:{"id":2,"value":"value2"}

data:{"id":3,"value":"value3"}

data:{"id":4,"value":"value4"}

data:{"id":5,"value":"value5"}
```
