package org.zerock.mallapi.repository.search;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.zerock.mallapi.domain.Todo;
import org.zerock.mallapi.dto.PageRequestDto;

import java.util.List;

import static org.zerock.mallapi.domain.QTodo.todo;

@Slf4j
public class TodoSearchImpl extends QuerydslRepositorySupport implements TodoSearch {

    private final JPAQueryFactory queryFactory;

    public TodoSearchImpl(EntityManager em) {
        super(Todo.class);
        queryFactory = new JPAQueryFactory(em);

    }

    @Override
    public Page<Todo> search1(PageRequestDto pageRequestDto) {
        log.info("search1");

        JPAQuery<Todo> query = queryFactory
                .selectFrom(todo);
                /*.where(todo.title.contains("1"));*/

        Pageable pageable = PageRequest.of(
                pageRequestDto.getPage() - 1,
                pageRequestDto.getSize(),
                Sort.by("tno").descending());

        this.getQuerydsl().applyPagination(pageable, query);
        List<Todo> content = query.fetch();

        Long total = queryFactory
                .select(todo.count())
                .from(todo)
                /*.where(todo.title.contains("1"))*/
                .fetchOne();

        return new PageImpl<>(content, pageable, total);
    }
}
