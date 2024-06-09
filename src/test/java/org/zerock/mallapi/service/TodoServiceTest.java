package org.zerock.mallapi.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.mallapi.domain.Todo;
import org.zerock.mallapi.dto.PageRequestDto;
import org.zerock.mallapi.dto.TodoDto;
import org.zerock.mallapi.repository.TodoRepository;

import java.time.LocalDate;

@SpringBootTest
@Transactional
@Slf4j
@Commit
public class TodoServiceTest {

    @Autowired
    TodoService todoService;

    @Autowired
    TodoRepository todoRepository;

    @BeforeEach
    public void before() {
        for (int i = 0; i < 103; i++) {
            Todo todo = Todo.builder()
                    .title("title" + i)
                    .content("content" + i)
                    .dueDate(LocalDate.of(2023, 12, 30))
                    .complete(false)
                    .build();

            todoRepository.save(todo);
        }
    }

    @Test
    public void testGet() throws Exception{
        Long tno = 50l;
        TodoDto findTodoDto = todoService.get(tno);
        log.info(findTodoDto.toString());
    }
    
    @Test
    public void testRegister() throws Exception{
        TodoDto todoDto = TodoDto.builder()
                .title("Title....")
                .content("Content...")
                .dueDate(LocalDate.of(2023, 12, 31))
                .build();

        log.info("저장된 todo의 기본키번호={}", todoService.register(todoDto));

    }

    @Test
    public void testModify() throws Exception{
        TodoDto todoDto = TodoDto.builder()
                .title("Title....")
                .content("Content...")
                .dueDate(LocalDate.of(2023, 12, 31))
                .build();

        Long registeredId = todoService.register(todoDto);

        TodoDto findTodoDto = todoService.get(registeredId);
        findTodoDto.setTitle("변경된 주제");
        todoService.modify(findTodoDto);
    }

    @Test
    public void testGetList() throws Exception {
        PageRequestDto pageRequestDto = PageRequestDto.builder().page(10).build();
        log.info("테스트={}", todoService.getList(pageRequestDto));
    }
}
