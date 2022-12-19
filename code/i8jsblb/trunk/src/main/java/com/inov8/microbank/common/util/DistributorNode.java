package com.inov8.microbank.common.util;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

import javax.swing.tree.TreeNode;

import com.google.common.collect.Iterators;

/**
 * Usecase: Common For Tree Component
 * Date: 
 * Author: Rashid Mahmood
 * Email:  rashid.mahmood@its.ws
 */

public class DistributorNode implements TreeNode
{
    private String label;
    private String value;
    private List<RegionNode> secondLevelNodes = new ArrayList<RegionNode>();

    public void setLabel(String label)
    {
        this.label = label;
    }

    public String getLabel()
    {
        return label;
    }

    public void setSecondLevelNodes(List<RegionNode> secondLevelNodes)
    {
        this.secondLevelNodes = secondLevelNodes;
    }

    public List<RegionNode> getSecondLevelNodes()
    {
        return secondLevelNodes;
    }
    
    public String getType()
    {
        return "DISTRIBUTOR_NODE";
    }

    public void setValue(String value)
    {
        this.value = value;
    }

    public String getValue()
    {
        return value;
    }

	@Override
	public TreeNode getChildAt(int childIndex) {
		return this.secondLevelNodes.get(childIndex);
	}

	@Override
	public int getChildCount() {
		return secondLevelNodes.size();
	}

	@Override
	public TreeNode getParent() 
	{
		return null;
	}

	@Override
	public int getIndex(TreeNode node) {
		return secondLevelNodes.indexOf(node);
	}

	@Override
	public boolean getAllowsChildren() {
		return true;
	}

	@Override
	public boolean isLeaf() {
		return secondLevelNodes.isEmpty();
	}

	@Override
	public Enumeration<RegionNode> children() {
		return Iterators.asEnumeration(secondLevelNodes.iterator());
	}
	
	public Iterator<RegionNode> childrenIterator() {
		return secondLevelNodes.iterator();
	}
	
	public void addChild(RegionNode regionNode)
	{
		this.secondLevelNodes.add(regionNode);
	}
}
