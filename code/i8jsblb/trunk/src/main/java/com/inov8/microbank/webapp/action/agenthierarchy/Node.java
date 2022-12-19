package com.inov8.microbank.webapp.action.agenthierarchy;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

import javax.swing.tree.TreeNode;

import com.google.common.collect.Iterators;


public class Node implements TreeNode{

	private Long id;
	private String name;
	private List<Node> areaNodes = new ArrayList<Node>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<Node> getAreaNodes() {
		return areaNodes;
	}

	public void setAreaNodes(List<Node> areaNodes) {
		this.areaNodes = areaNodes;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public TreeNode getChildAt(int childIndex) {
		return this.areaNodes.get(childIndex);
	}

	@Override
	public int getChildCount() {
		return areaNodes.size();
	}

	@Override
	public TreeNode getParent() 
	{
		return null;
	}

	@Override
	public int getIndex(TreeNode node) {
		return areaNodes.indexOf(node);
	}

	@Override
	public boolean getAllowsChildren() {
		return true;
	}

	@Override
	public boolean isLeaf() {
		return areaNodes.isEmpty();
	}

	@Override
	public Enumeration<Node> children() {
		return Iterators.asEnumeration(areaNodes.iterator());
	}
	
	public Iterator<Node> childrenIterator() {
		return areaNodes.iterator();
	}
	
	public void addChild(Node node)
	{
		this.areaNodes.add(node);
	}
	
}
