package com.example.util.paginator;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;

public class PageRender<T> {

	private String url;
	private Page<T> page;
	private int totalPages;
	private int elementsPerPage;
	private int currentPage;
	private List<PageItem> pages;

	public PageRender(String url, Page<T> page) {

		this.url = url;
		this.page = page;
		this.pages = new ArrayList<PageItem>();

		this.elementsPerPage = page.getSize();
		this.totalPages = page.getTotalPages();
		this.currentPage = page.getNumber() + 1;
	
		int from, until;
		if (totalPages <= elementsPerPage) {
			from = 1;
			until = totalPages;
		} else {
			if (currentPage <= elementsPerPage / 2) {
				from = 1;
				until = elementsPerPage;
			} else if (currentPage >= totalPages - elementsPerPage / 2) {
				from = totalPages - elementsPerPage + 1;
				until = elementsPerPage;
			} else {
				from = currentPage - elementsPerPage / 2;
				until = elementsPerPage;
			}
		}
		for (int i = 0; i < until; i++) {
			pages.add(new PageItem(from + i, currentPage == from + i));
		}
	}

	public String getUrl() {
		return url;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public List<PageItem> getPages() {
		return pages;
	}

	public boolean isFirst() {
		return page.isFirst();
	}

	public boolean isLast() {
		return page.isLast();
	}

	public boolean isHasNext() {
		return page.hasNext();
	}

	public boolean isHasPrevious() {
		return page.hasPrevious();
	}

}
