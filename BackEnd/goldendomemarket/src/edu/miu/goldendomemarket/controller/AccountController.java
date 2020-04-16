package edu.miu.goldendomemarket.controller;
/** 
* @author Anene Terefe
* @author Hanna Workneh
* created April, 2020
*/ 
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import edu.miu.goldendomemarket.domain.Account;
import edu.miu.goldendomemarket.service.serviceImpl.AccountServiceImpl;
import edu.miu.goldendomemarket.util.Database;
import edu.miu.goldendomemarket.util.PasswordHashing;

/**
 * Servlet implementation class AccountController
 */
@WebServlet(description = "For managing accounts", urlPatterns = { "/api/account" })
public class AccountController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AccountController() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		AccountServiceImpl accService = new AccountServiceImpl((Database) getServletContext().getAttribute("db"));
		response.setContentType("text/html");
		String result = null;
		if (request.getParameterMap().containsKey("id")) {
			Account acc = accService.findById(Integer.parseInt(request.getParameter("id")));
			result = new Gson().toJson(acc);
		} else {
			List<Account> acc = accService.findAll();
			result = new Gson().toJson(acc);
		}
		PrintWriter out = response.getWriter();
		out.print(result);
		out.flush();

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		AccountServiceImpl accService = new AccountServiceImpl((Database) getServletContext().getAttribute("db"));
		response.setContentType("text/html");
		Gson gson = new Gson();
		StringBuilder sb = new StringBuilder();
		String s;
		while ((s = request.getReader().readLine()) != null) {
			sb.append(s);
		}
		Account acc = (Account) gson.fromJson(sb.toString(), Account.class);
		Account result;
		if (accService.findById(acc.getAccountId()) == null) {
			String pass = acc.getPassword();
			acc.setPassword(PasswordHashing.hashpassword(pass));
			result = accService.save(acc);
		} else {
			result = null;
		}
		PrintWriter out = response.getWriter();
		String json = new Gson().toJson(result);
		out.print(json);
		out.flush();

	}

	/**
	 * @see HttpServlet#doPut(HttpServletRequest, HttpServletResponse)
	 */
	protected void doPut(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		AccountServiceImpl accService = new AccountServiceImpl((Database) getServletContext().getAttribute("db"));
		response.setContentType("text/html");
		Gson gson = new Gson();
		StringBuilder sb = new StringBuilder();
		String s;
		while ((s = request.getReader().readLine()) != null) {
			sb.append(s);
		}
		Account acc = (Account) gson.fromJson(sb.toString(), Account.class);
		Account result;
		if (accService.findById(acc.getAccountId()) != null) {
			result = accService.update(acc, acc.getAccountId());
		} else {
			result = null;
		}
		PrintWriter out = response.getWriter();
		String json = new Gson().toJson(result);
		out.print(json);
		out.flush();

	}

	/**
	 * @see HttpServlet#doDelete(HttpServletRequest, HttpServletResponse)
	 */
	protected void doDelete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		AccountServiceImpl accService = new AccountServiceImpl((Database) getServletContext().getAttribute("db"));
		response.setContentType("text/html");
		Gson gson = new Gson();
		StringBuilder sb = new StringBuilder();
		String s;
		while ((s = request.getReader().readLine()) != null) {
			sb.append(s);
		}
		Account acc = (Account) gson.fromJson(sb.toString(), Account.class);
		PrintWriter out = response.getWriter();
		if (accService.findById(acc.getAccountId()) != null) {
			accService.delete(acc.getAccountId());
			out.print(true);
		} else {
			out.print(false);
		}
		out.flush();

	}

}