package com.inov8.microbank.webapp.action.agenthierarchy;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ActionEvent;


@ManagedBean(name="testClass")
@SessionScoped
public class TestClass {

	private String name;
	private boolean flag;
	private Numbers number = new Numbers();
	
	public String printHello()
	{
		System.out.println("Hello");
		return null;
	}
	
	public void printHelloByEvent(ActionEvent e)
	{
		System.out.println("Hello by Event");
	}
	
	public Numbers getNumber() {
		return number;
	}


	public void setNumber(Numbers number) {
		this.number = number;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public boolean isFlag() {
		return flag;
	}


	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	public String print()
	{
		System.out.println("Name is: " + this.name);
		return "submit";
	}
	
	public String addNumbers()
	{
		this.number.setResult(String.valueOf(Integer.valueOf(this.getNumber().getFirstNumber()) + Integer.valueOf(this.getNumber().getSecondNumber())));
		return "submit";
	}
	
	public TestClass() {
		super();
		this.flag = Boolean.TRUE;
		// TODO Auto-generated constructor stub
	}

	
	
	
}
