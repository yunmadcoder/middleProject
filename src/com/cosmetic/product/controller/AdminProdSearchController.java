package com.cosmetic.product.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cosmetic.product.service.IProdService;
import com.cosmetic.product.service.ProdServiceImpl;

@WebServlet("/prodSearchaAdmin.do")
public class AdminProdSearchController extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		req.getSession(false).removeAttribute("cate");
		req.getSession(false).removeAttribute("search");

		/*
		 * 로그아웃 시에 세션에서 "cate"와 "search" 속성을 지워 사용자 정보를 클리어하는 역할을함. 예를 들어, 사용자가 로그인 후 특정
		 * 카테고리를 선택했거나 검색어를 입력했을 때, 로그아웃 시 이전에 선택한 정보를 지워 보안 및 개인 정보 보호를 강화할 수 있다.
		 * req.getSession(false).removeAttribute("cate");는 "cate" 속성을 제거하려는데, 만약 세션이
		 * 존재하지 않으면 새로운 세션을 만들지 않고 null을 반환
		 * 
		 */

		IProdService prodService = ProdServiceImpl.getInstance();
		String cate = req.getParameter("cate"); // 하나의 값에만, 여러개의 value값은 getParameterValues();
		String search = req.getParameter("search");

		System.out.println("cate<<" + cate);

		Map<String, Object> searchMap = new HashMap<String, Object>();

		searchMap.put("cate", cate);

		if (cate.equals("PROD_NAME")) {
			searchMap.put("PROD_NAME", search);
		} else if (cate.equals("BR_NO")) {
			searchMap.put("BR_NO", search);
		} else if (cate.equals("PROD_CATE_NO")) {
			searchMap.put("PROD_CATE_NO", search);
		} else {
			System.out.println("doesn't work.");
		}
		if (search != null && !search.equals("")) {
			searchMap.put("search", search);
		}

		int currentPage = 1;
		currentPage = req.getParameter("page") == null ? 1 : Integer.parseInt(req.getParameter("page"));
		int totalCount = prodService.searchCountList(searchMap);

		int perPage = 2;

		int perList = 10;
		int totalPage = (int) Math.ceil((double) totalCount / (double) perList);

		int start = (currentPage - 1) * perList + 1;
		int end = start + perList - 1;
		if (end > totalCount)
			end = totalCount;

		int startPage = ((currentPage - 1) / perPage * perPage) + 1;

		int endPage = startPage + perPage - 1;
		if (endPage > totalPage)
			endPage = totalPage;

		searchMap.put("start", start);
		searchMap.put("end", end);

		List<Map<String, Object>> prodSearchList = prodService.prodAdminListSearch(searchMap);

		System.out.println(searchMap);

		req.setAttribute("prodSearchList", prodSearchList);

		
		req.setAttribute("sPage", startPage);
		req.setAttribute("ePage", endPage);
		req.setAttribute("cPage", currentPage);
		req.setAttribute("ttPage", totalPage);
		req.setAttribute("totalCount", totalCount);

		System.out.println("totalcount" + totalCount);

		req.getRequestDispatcher("/admin/goodsList2.jsp").forward(req, resp);

	}
}
