package com.qinb.web;

import java.io.IOException;
import java.sql.Connection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.qinb.dao.DiaryDao;
import com.qinb.model.Diary;
import com.qinb.util.DbUtil;
import com.qinb.util.StringUtil;


public class DiaryServlet extends HttpServlet{

	
	DbUtil dbUtil=new DbUtil();
	DiaryDao diaryDao=new DiaryDao();
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		String action=request.getParameter("action");
		if("show".equals(action)){
			diaryShow(request,response);
		}else if("preSave".equals(action)){
			diaryPreSave(request,response);
		}else if("save".equals(action)){
			diarySave(request,response);
		}else if("delete".equals(action)){
			diaryDelete(request,response);
		}
	}
	
	private void diaryShow(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
		String diaryId=request.getParameter("diaryId");
		Connection con=null;
		try{
			con=dbUtil.getCon();
			Diary diary=diaryDao.diaryShow(con, diaryId);
			request.setAttribute("diary", diary);
			request.setAttribute("mainPage", "diary/diaryShow.jsp");
			request.getRequestDispatcher("mainTemp.jsp").forward(request, response);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try {
				dbUtil.closeCon(con);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void diaryPreSave(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
		String diaryId=request.getParameter("diaryId");
		Connection con=null;
		try{
			if(StringUtil.isNotEmpty(diaryId)){
				con=dbUtil.getCon();
				Diary diary=diaryDao.diaryShow(con, diaryId);
				request.setAttribute("diary", diary);
			}
			request.setAttribute("mainPage", "diary/diarySave.jsp");
			request.getRequestDispatcher("mainTemp.jsp").forward(request, response);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try {
				dbUtil.closeCon(con);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}
	
	private void diarySave(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
		String title=request.getParameter("title");
		String content=request.getParameter("content");
		String typeId=request.getParameter("typeId");
		String diaryId=request.getParameter("diaryId");
		
		Diary diary=new Diary(title,content,Integer.parseInt(typeId));
		if(StringUtil.isNotEmpty(diaryId)){
			diary.setDiaryId(Integer.parseInt(diaryId));
		}
		Connection con=null;
		try {
			con=dbUtil.getCon();
			int saveNums;
			if(StringUtil.isNotEmpty(diaryId)){
				saveNums=diaryDao.diaryUpdate(con, diary);	
			}else{
				saveNums=diaryDao.diaryAdd(con, diary);				
			}
			if(saveNums>0){
				request.getRequestDispatcher("main?all=true").forward(request, response);
			}else{
				request.setAttribute("diary", diary);
<<<<<<< HEAD
				request.setAttribute("error", "错误");
=======
				request.setAttribute("error", "����ʧ��");
>>>>>>> bootstra/web
				request.setAttribute("mainPage", "diary/diarySave.jsp");
				request.getRequestDispatcher("mainTemp.jsp").forward(request, response);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				dbUtil.closeCon(con);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private void diaryDelete(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
		String diaryId=request.getParameter("diaryId");
		Connection con=null;
		try{
			con=dbUtil.getCon();
			diaryDao.diaryDelete(con, diaryId);
			request.getRequestDispatcher("main?all=true").forward(request, response);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try {
				dbUtil.closeCon(con);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}