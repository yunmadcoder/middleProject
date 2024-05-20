package com.cosmetic.product.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.cosmetic.common.vo.AtchFileDetailVO;
import com.cosmetic.product.service.IProdService;
import com.cosmetic.product.service.IReviewService;
import com.cosmetic.product.service.ProdServiceImpl;
import com.cosmetic.product.service.ReviewServiceImpl;
import com.cosmetic.product.vo.ProdVO;
import com.cosmetic.product.vo.ReviewVO;

import lombok.Data;

@WebServlet("/prodDetail.do")
public class ProdDetailController extends HttpServlet {
	
	IReviewService reviewService = ReviewServiceImpl.getInstance();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		String prodNo = req.getParameter("prodNo");

		IProdService prodService = ProdServiceImpl.getInstance();
		ProdVO pv = prodService.getProduct(prodNo);
		
		Cookie[] cookies = req.getCookies();
		Cookie itemCookie = findCookie(cookies, "recentItem");
		
		ServletContext ctx = req.getServletContext();
		
		List<String> imgList = (List<String>) ctx.getAttribute("imgList") == null
				? new ArrayList<>() : (List<String>) ctx.getAttribute("imgList");
		
		if (itemCookie == null) {
		    itemCookie = new Cookie("recentItem", prodNo);
		    imgList.add(pv.getProdImg());
		} else {
		    String[] items = itemCookie.getValue().split(",");
		    if (!Arrays.asList(items).contains(prodNo)) {
		        if (items.length >= 5) {
		            itemCookie.setValue(removeOldItem(items) + "," + prodNo);
		            if(imgList.size() >= 5) {
		            	imgList.remove(0);
		            }	
		            imgList.add(pv.getProdImg());
		            
		        } else {
		            itemCookie.setValue(itemCookie.getValue() + "," + prodNo);
		            imgList.add(pv.getProdImg());
		        }
		    }
		}
		
		ctx.setAttribute("imgList", imgList);
		itemCookie.setMaxAge(60 * 60);
		
		System.out.println(itemCookie.getName());
		System.out.println(itemCookie.getValue());
		System.out.println("IMGLIST >> " + imgList);
		
		resp.addCookie(itemCookie);

		req.setAttribute("prodvo", pv);

		List<ReviewVO> reviewList = ReviewList(prodNo);
		ReviewVO reviewDetail = reviewDetail(prodNo);

		req.setAttribute("reviewList", reviewList);
		req.setAttribute("reviewDetail", reviewDetail);

		req.getRequestDispatcher("/shop/detail.jsp").forward(req, resp);

	}

	private String removeOldItem(String[] item) {
		return String.join(",", Arrays.copyOfRange(item, 1, item.length));
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);

	}

	public List<ReviewVO> ReviewList(String prodNo) {

		List<ReviewVO> reviewList = reviewService.displayAllReview(prodNo);
		
		return reviewList;

	}
	
	public ReviewVO reviewDetail(String prodNo) {

		ReviewVO reviewDetail = reviewService.getProdByReviewCount(prodNo);
		
		return reviewDetail;

	}
	
	private Cookie findCookie(Cookie[] cookies, String name) {
		
		if(cookies != null) {
			for (Cookie cookie : cookies) {
				if(name.equals(cookie.getName())) {
					return cookie;
				}
			}
		}
		
		return null;
	}

}
