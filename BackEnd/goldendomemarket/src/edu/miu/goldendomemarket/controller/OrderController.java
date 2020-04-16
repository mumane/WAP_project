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

import edu.miu.goldendomemarket.domain.Order;
import edu.miu.goldendomemarket.service.serviceImpl.OrderServiceImpl;
import edu.miu.goldendomemarket.util.Database;
import edu.miu.goldendomemarket.util.SendEmail;

/**
 * Servlet implementation class OrderController
 */
@WebServlet("/api/order")
public class OrderController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public OrderController() {
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
		boolean session = request.getParameterMap().containsKey("session");
		if (session) {
			OrderServiceImpl orderService = new OrderServiceImpl((Database) getServletContext().getAttribute("db"));
			response.setContentType("text/html");
			String result = null;
			if (request.getParameterMap().containsKey("id")) {
				Order order = orderService.findById(Integer.parseInt(request.getParameter("id")));
				result = new Gson().toJson(order);
			} else {
				// provide list of order that the student made
				if (request.getParameter("session").equals("student")) {
					String user_id = request.getParameter("user");
					List<Order> order = orderService.findByUser(Integer.valueOf(user_id));
					result = new Gson().toJson(order);
				} else {
					// provides list of all orders
					List<Order> order = orderService.findAll();
					result = new Gson().toJson(order);
				}
			}
			PrintWriter out = response.getWriter();
			System.out.println(result);
			out.print(result);
			out.flush();
		} else {
			PrintWriter out = response.getWriter();
			out.print("unauthorized user");
			out.flush();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		OrderServiceImpl orderService = new OrderServiceImpl((Database) getServletContext().getAttribute("db"));
		response.setContentType("text/html");
		Gson gson = new Gson();
		StringBuilder sb = new StringBuilder();
		String s;
		while ((s = request.getReader().readLine()) != null) {
			sb.append(s);
		}
		Order order = (Order) gson.fromJson(sb.toString(), Order.class);
		Order result;
		if (orderService.findById(order.getOrderId()) == null) {
			result = orderService.save(order);
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
		OrderServiceImpl orderService = new OrderServiceImpl((Database) getServletContext().getAttribute("db"));
		response.setContentType("text/html");
		Gson gson = new Gson();
		StringBuilder sb = new StringBuilder();
		String s;
		while ((s = request.getReader().readLine()) != null) {
			sb.append(s);
		}
		Order order = (Order) gson.fromJson(sb.toString(), Order.class);
		System.out.println(order.toString());
		Order result;
		if (orderService.findById(order.getOrderId()) != null) {
			Order tobeupdated = orderService.findById(order.getOrderId());
			tobeupdated.setStatus(order.getStatus());
			result = orderService.update(tobeupdated, tobeupdated.getOrderId());
			SendEmail sc=(SendEmail) getServletContext().getAttribute("emailsend");
			String to=result.getAccount().getEmail();
			String sub="Golden Market Update on your Orders";
			String msg="Your Order Item "+result.getItem().getItemName()+ " which was ordered on "+ result.getOrderDate()+" is "+result.getStatus();
			sc.sendEmail(to, sub, msg);
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
		OrderServiceImpl orderService = new OrderServiceImpl((Database) getServletContext().getAttribute("db"));
		response.setContentType("text/html");
		Gson gson = new Gson();
		StringBuilder sb = new StringBuilder();
		String s;
		while ((s = request.getReader().readLine()) != null) {
			sb.append(s);
		}
		Order order = (Order) gson.fromJson(sb.toString(), Order.class);
		PrintWriter out = response.getWriter();
		if (orderService.findById(order.getOrderId()) != null) {
			orderService.delete(order.getOrderId());
			out.print(true);
		} else {
			out.print(false);
		}
		out.flush();
	}

}
