package org.zerock.mallapi.repository;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.mallapi.domain.Todo;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@Slf4j
class TodoRepositoryTest {

    @Autowired
    private TodoRepository todoRepository;

    @BeforeEach
    public void before() {
        for (int i = 0; i < 100; i++) {
            Todo todo = Todo.builder()
                    .title("title" + i)
                    .content("content" + i)
                    .dueDate(LocalDate.of(2023, 12, 30))
                    .build();

            todoRepository.save(todo);
        }
    }

    @Test
    public void test1() throws Exception {
        assertThat(todoRepository).isNotNull();
        log.info(todoRepository.getClass().getName());
    }

    @Test
    public void testInsert() throws Exception {
        Todo todo = Todo.builder()
                .title("title")
                .content("content")
                .dueDate(LocalDate.of(2023, 12, 30))
                .build();

        Todo savedTodo = todoRepository.save(todo);

        log.info(savedTodo.toString());
        assertThat(todo).isEqualTo(savedTodo);
    }

    @Test
    public void testRead() throws Exception {
        Long tno = 1L;
        Todo todo = Todo.builder()
                .title("title")
                .content("content")
                .dueDate(LocalDate.of(2023, 12, 30))
                .build();

        Todo savedTodo = todoRepository.save(todo);
        Optional<Todo> result = todoRepository.findById(tno);

        Todo findTodo = result.orElseThrow();

        assertThat(savedTodo).isEqualTo(findTodo);
    }

    @Test
    public void testUpdate() throws Exception {
        Long tno = 1L;
        Todo todo = Todo.builder()
                .title("title")
                .content("content")
                .dueDate(LocalDate.of(2023, 12, 30))
                .build();

        Todo savedTodo = todoRepository.save(todo);
        Optional<Todo> result = todoRepository.findById(tno);

        Todo findTodo = result.orElseThrow();
        findTodo.setComplete(true);
        findTodo.setContent("변경된 content");
        findTodo.setTitle("변경된 title");
        todoRepository.save(findTodo);
    }

    @Test
    public void testPage() throws Exception {
        Todo todo = Todo.builder()
                .title("title")
                .content("content")
                .dueDate(LocalDate.of(2023, 12, 30))
                .build();

        Todo savedTodo = todoRepository.save(todo);

        Pageable pageable = PageRequest.of(0, 10, Sort.by("tno").descending());
        Page<Todo> findAll = todoRepository.findAll(pageable);
        log.info(String.valueOf(findAll.getTotalElements()));
        log.info(findAll.getContent().toString());

    }

   /* @Test
    public void testSearch1() throws Exception {
        Page<Todo> todos = todoRepository.search1();

        log.info("현재 페이지의 할 일 수: {}", todos.getNumberOfElements());
        log.info("총 할 일 수: {}", todos.getTotalElements());
        log.info("총 페이지 수: {}", todos.getTotalPages());
        log.info("현재 페이지 번호: {}", todos.getNumber());
        log.info("첫 번째 페이지 여부: {}", todos.isFirst());
        log.info("마지막 페이지 여부: {}", todos.isLast());
        log.info("다음 페이지 존재 여부: {}", todos.hasNext());
        log.info("이전 페이지 존재 여부: {}", todos.hasPrevious());

        todos.getContent().forEach(todo -> log.info("할 일: {}", todo));

    }*/

}