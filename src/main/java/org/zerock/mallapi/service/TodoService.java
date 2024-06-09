package org.zerock.mallapi.service;

import org.springframework.transaction.annotation.Transactional;
import org.zerock.mallapi.domain.Todo;
import org.zerock.mallapi.dto.PageRequestDto;
import org.zerock.mallapi.dto.PageResponseDto;
import org.zerock.mallapi.dto.TodoDto;

@Transactional(readOnly = true)
public interface TodoService {
    TodoDto get(Long tno);
    @Transactional
    Long register(TodoDto todoDto);


    @Transactional
    void modify(TodoDto todoDto);

    @Transactional
    void remove(Long tno);

    PageResponseDto<TodoDto> getList(PageRequestDto pageRequestDto);

    default TodoDto entityToDto(Todo todo) {
        return TodoDto.builder()
                .tno(todo.getTno())
                .title(todo.getTitle())
                .content(todo.getContent())
                .complete(todo.getComplete())
                .dueDate(todo.getDueDate())
                .build();
    }

    default Todo dtoToEntity(TodoDto todoDto) {
        return Todo.builder()
                .tno(todoDto.getTno())
                .title(todoDto.getTitle())
                .content(todoDto.getContent())
                .complete(todoDto.getComplete())
                .dueDate(todoDto.getDueDate())
                .build();
    }

}
