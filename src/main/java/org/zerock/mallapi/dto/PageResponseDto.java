package org.zerock.mallapi.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


@Data
public class PageResponseDto<E> {

    private List<E> dtoList;

    private List<Integer> pageNumberList;

    private PageRequestDto pageRequestDto;

    private boolean prev,next;

    private int totalCount;

    private int prevPage,nextPage,totalPage,current;

    @Builder(builderMethodName = "withAll")
    public PageResponseDto(List<E> dtoList, PageRequestDto pageRequestDto, long total) {
        this.dtoList = dtoList;
        this.pageRequestDto = pageRequestDto;
        this.totalCount = (int) total;

        //끝페이지
        int end = (int) (Math.ceil(pageRequestDto.getPage() / 10.0)) * 10;//현재 페이지/10

        int start = end - 9;

        //가장 마지막 페이지
        int last = (int) (Math.ceil(totalCount / (double) pageRequestDto.getSize()));

        end = end > last ? last : end;

        this.prev = start > 1;

        this.next = totalCount > end * pageRequestDto.getSize();

        this.pageNumberList = IntStream.rangeClosed(start, end).boxed().collect(Collectors.toList());

        this.prevPage = prev ? start - 1 : 0;

        this.nextPage = next ? end + 1 : 0;
    }
}
