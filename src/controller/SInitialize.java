package controller;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import beans.User;

/**
 * Servlet implementation class SInitialize
 */
public class SInitialize extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public SInitialize() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(true);
		response.setHeader("Cache-Control","no-cache, max-age=0, must-revalidate, no-store");
		System.out.println("I am in servlet SInitialize");

		
/*
		if (null == session.getAttribute("devices")) {
			System.out.println("Attribute devices is set");
			Devices devices = ComputerShop.getInstance().getDevices();
			session.setAttribute("devices", devices);
		}

		if (null == session.getAttribute("components")) {
			System.out.println("Attribute components is set");
			Components components = ComputerShop.getInstance().getComponents();
			session.setAttribute("components", components);
		}

		if (null == session.getAttribute("categories")) {
			System.out.println("Attribute categories is set");
			Categories categories = ComputerShop.getInstance().getCategories();
			session.setAttribute("categories", categories);
		}

		Users users = ComputerShop.getInstance().getUsers();
		if (null == session.getAttribute("users")) {
			System.out.println("Attribute users is set");
			session.setAttribute("users", users);
		}

		RequestDispatcher disp = getServletContext().getRequestDispatcher(
				"/mainPage.jsp");
		disp.forward(request, response);*/
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}