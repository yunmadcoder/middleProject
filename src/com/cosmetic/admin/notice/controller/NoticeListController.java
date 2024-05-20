package com.cosmetic.admin.notice.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cosmetic.admin.notice.service.INoticeService;
import com.cosmetic.admin.notice.service.NoticeServiceImpl;
import com.cosmetic.admin.notice.vo.NoticeVO;

@WebServlet("/notice.do")
public class NoticeListController extends HttpServlet{
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		INoticeService service = NoticeServiceImpl.getInstance();
		int currentPage =1;
		currentPage = req.getParameter("page") == null ? 1 : Integer.parseInt(req.getParameter("page"));
		
		//전체글 갯수를 얻기위한 서비스 객체 필요
		int totalCount = service.countList(); //전체 글갯수
		
		//화면에 표시될 페이지 수 
		int perPage = 5;
		
		//한 페이지당 출력할 글 갯수 
		int perList = 10; 
		
		//전체 페이지수 = 전체 글갯수 (totalCount) / 페이지당 글 갯수(perList)
		int totalPage =(int)Math.ceil((double)totalCount / (double)perList);
		
		System.out.println(">>>" + totalPage);
		// 페이지 당 출력할 게시글 범위(start ~ end)값 구하기
		// start = (currentPage - 1) * perList +1;
		// 1페이지 일때 : (1-1)*5+1 = 1
		// 2페이지 일때 : (2-1)*5+1 = 6
		// 3페이지 일때 : (3-1)*5+1 = 11
		// 4페이지 일때 : (4-1)*5+1 = 16
		// 5페이지 일때 : (5-1)*5+1 = 21
		int start = (currentPage - 1) * perList + 1;
		
		// end = start + perList -1;
		// 1페이지 일때 : 1 + 5 - 1 = 5
		// 2페이지 일때 : 6 + 5 - 1 = 10
		// 3페이지 일때 : 11 + 5 - 1 = 15
		// 4페이지 일때 : 16 + 5 - 1 = 20
		// 5페이지 일때 : 21 + 5 - 1 = 25
		int end = start + perList - 1;
		if (end > totalCount)
			end = totalCount; // 전체 글갯수를 초과하는 end범위 값 조절
		
		// 화면에 표시될 페이지 범위 구하기(startPage ~ endPage)
		int startPage = ((currentPage - 1) / perPage * perPage) + 1;
		
		int endPage = startPage + perPage -1;
		if(endPage > totalPage) endPage = totalPage;
		
	 	Map<String, Object> map = new HashMap<String, Object>();
	 	map.put("start", start);
	 	map.put("end", end);
		//범위 별 게시물 정보 가져오기
		List<NoticeVO> list = service.noticeList(map);
		
//		List<CustServiceVO> list = service.noticeList();
		
		req.setAttribute("list", list);
		req.setAttribute("sPage", startPage);
		req.setAttribute("ePage", endPage);
		req.setAttribute("cPage", currentPage);
		req.setAttribute("totalCount", totalCount);
		req.setAttribute("ttPage", totalPage);
		
		req.getRequestDispatcher("/main/service/notice_list.jsp").forward(req, resp);
		
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
	}
}
