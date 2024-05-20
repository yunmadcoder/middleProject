package com.cosmetic.product.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import com.cosmetic.common.dao.IAtchFileDao;
import com.cosmetic.common.service.AtchFileServiceImpl;
import com.cosmetic.common.service.IAtachFileService;
import com.cosmetic.common.vo.AtchFileDetailVO;
import com.cosmetic.product.service.IProdService;
import com.cosmetic.product.service.ProdServiceImpl;
import com.cosmetic.product.vo.ProdVO;
import com.cosmetic.util.OthersUtil;

/**
 * Servlet implementation class ProdUpdateController
 */
@MultipartConfig
@WebServlet("/prodUpdate.do")
public class ProdUpdateController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public ProdUpdateController() {
		super();

	}

	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		String prodNo = req.getParameter("prodNo");

		IProdService ps = ProdServiceImpl.getInstance();

		ProdVO pv = ps.getProduct(prodNo);

		req.setAttribute("pv", pv);

//		if (pv.getFileNo() > 0) {
//			IAtachFileService fileServie = AtchFileServiceImpl.getInstance();
//
//			AtchFileDetailVO atchfilVO = new AtchFileDetailVO();
//
//			atchfilVO.setFileDetNo(pv.getFileNo());
//
//			List<AtchFileDetailVO> atchFileList = fileServie.getAtchFileList(atchfilVO);
//
//			req.setAttribute("atchFileList", atchFileList);
//		}

		req.getRequestDispatcher("/admin/updateForm.jsp").forward(req, resp);

	}

	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		req.setCharacterEncoding("UTF-8");

		long prodNo = Long.parseLong(req.getParameter("prodNo"));
		String prodName = req.getParameter("prodName");
		long prodPrice = Long.parseLong(req.getParameter("prodPrice"));
		long prodCateNo = Long.parseLong(req.getParameter("prodCateNo"));
		String prodContent = req.getParameter("prodContent");
		long brNo = Long.parseLong(req.getParameter("brNo"));
		String fileNo = req.getParameter("fileNo");

		IProdService ps = ProdServiceImpl.getInstance();

		IAtachFileService fileService = AtchFileServiceImpl.getInstance();
		AtchFileDetailVO atchFileDetailVO = new AtchFileDetailVO();
		atchFileDetailVO = fileService.saveAtchFileList(req.getParts());

		ProdVO pv = new ProdVO();

//		Collection<Part> parts = new ArrayList<Part>();
//		String prodImg = "";
//		for (Part part : req.getParts()) {
//
//			String fileName = part.getSubmittedFileName();
//
//			if (fileName != null && !fileName.equals("")) {
//
//				if ("thumbFile".equals(part.getName())) {
//
//					String mimeType = req.getServletContext().getMimeType(part.getSubmittedFileName());
//
//					InputStream is = part.getInputStream();
//					byte[] imageData = new byte[is.available()];
//					is.read(imageData);
//					is.close();
//
//					prodImg = Base64.getEncoder().encodeToString(imageData);
//					prodImg = "data:" + mimeType + ";base64," + prodImg;
//
//				} else {
//
//					parts.add(part);
//
//				}
//			}
//		}

		pv.setProdNo(prodNo);
		pv.setProdName(prodName);
		pv.setProdPrice(prodPrice);
		pv.setProdCateNo(prodCateNo);
		pv.setBrNo(brNo);
		pv.setProdContent(prodContent);
		/* pv.setProdImg(prodImg); */
		if (atchFileDetailVO != null) {
			pv.setFileNo(atchFileDetailVO.getFileNo());
		}

		int cnt = (ps.modifyProduct(pv));
		System.out.println("CNT>>" + cnt);

		String msg = "";
		if (cnt > 0) {
			msg = "성공";
		} else {
			msg = "실패";
		}
		req.getSession().setAttribute("msg", msg);

		// 목록화면으로 리다이렉팅
		resp.sendRedirect(req.getContextPath() + "/prodList.do");

	}

}
