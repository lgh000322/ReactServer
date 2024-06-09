package org.zerock.mallapi.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.zerock.mallapi.domain.Todo;
import org.zerock.mallapi.dto.PageRequestDto;
import org.zerock.mallapi.dto.PageResponseDto;
import org.zerock.mallapi.dto.TodoDto;
import org.zerock.mallapi.repository.TodoRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class TodoServiceImpl implements TodoService {

    private final TodoRepository todoRepository;

    @Override
    public TodoDto get(Long tno) {
        Optional<Todo> result = todoRepository.findById(tno);

        Todo todo = result.orElseThrow();

        return entityToDto(todo);
    }

    @Override
    public Long register(TodoDto todoDto) {
        Todo todo = dtoToEntity(todoDto);
        Todo result = todoRepository.save(todo);

        return result.getTno();
    }

    @Override
    public void modify(TodoDto todoDto) {
        Optional<Todo> result = todoRepository.findById(todoDto.getTno());
        Todo todo = result.orElseThrow();

        todo.setTitle(todoDto.getTitle());
        todo.setContent(todoDto.getContent());
        todo.setComplete(todoDto.getComplete());
        todo.setDueDate(todoDto.getDueDate());

    }

    @Override
    public void remove(Long tno) {
        todoRepository.deleteById(tno);
    }

    @Override
    public PageResponseDto<TodoDto> getList(PageRequestDto pageRequestDto) {
        Page<Todo> result = todoRepository.search1(pageRequestDto);

        List<TodoDto> dtoList = result
                .get()
                .map(todo -> entityToDto(todo))
                .collect(Collectors.toList());

        PageResponseDto<TodoDto> responseDto = PageResponseDto.<TodoDto>withAll()
                .dtoList(dtoList)
                .pageRequestDto(pageRequestDto)
                .total(result.getTotalElements())
                .build();

        return responseDto;
    }
}
