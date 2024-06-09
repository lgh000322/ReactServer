package org.zerock.mallapi.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.zerock.mallapi.dto.PageRequestDto;
import org.zerock.mallapi.dto.PageResponseDto;
import org.zerock.mallapi.dto.TodoDto;
import org.zerock.mallapi.service.TodoService;

import java.util.Map;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/todo")
public class TodoController {
    private final TodoService todoService;

    @GetMapping("/{tno}")
    public TodoDto get(@PathVariable(name = "tno") Long tno) {
        return todoService.get(tno);
    }

    @GetMapping("/list")
    public PageResponseDto<TodoDto> list(PageRequestDto pageRequestDto) {
        return todoService.getList(pageRequestDto);
    }

    @PostMapping
    public Map<String, Long> register(@RequestBody TodoDto todoDto) {
        log.info("todoDto: {}", todoDto.toString());

        Long tno = todoService.register(todoDto);

        return Map.of("TNO", tno);
    }

    @PutMapping("/{tno}")
    public Map<String, String> modify(@PathVariable(name = "tno") Long tno,
                                      @RequestBody TodoDto todoDto) {
        todoDto.setTno(tno);

        todoService.modify(todoDto);

        return Map.of("RESULT", "SUCCESS");
    }

    @DeleteMapping("/{tno}")
    public Map<String, String> remote(@PathVariable(name = "tno") Long tno) {
        todoService.remove(tno);

        return Map.of("RESULT", "SUCCESS");
    }
}
