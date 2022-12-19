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

public class RegionNode implements TreeNode
{
    private String label;
    private String value;
    private List<FranchiseNode> thirdLevelNodes = new ArrayList<FranchiseNode>();

    public void setLabel(String label)
    {
        this.label = label;
    }

    public String getLabel()
    {
        return label;
    }

    public void setThirdLevelNodes(List<FranchiseNode> thirdLevelNodes)
    {
        this.thirdLevelNodes = thirdLevelNodes;
    }

    public List<FranchiseNode> getThirdLevelNodes()
    {
        return thirdLevelNodes;
    }
    
    public String getType()
    {
        return "REGION_NODE";
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
		return this.thirdLevelNodes.get(childIndex);
	}

	@Override
	public int getChildCount() {
		return thirdLevelNodes.size();
	}

	@Override
	public TreeNode getParent() 
	{
		return null;
	}

	@Override
	public int getIndex(TreeNode node) {
		return thirdLevelNodes.indexOf(node);
	}

	@Override
	public boolean getAllowsChildren() {
		return true;
	}

	@Override
	public boolean isLeaf() {
		return thirdLevelNodes.isEmpty();
	}

	@Override
	public Enumeration<FranchiseNode> children() {
		return Iterators.asEnumeration(thirdLevelNodes.iterator());
	}
	
	public Iterator<FranchiseNode> childrenIterator() {
		return thirdLevelNodes.iterator();
	}
	
	public void addChild(FranchiseNode franchiseNode)
	{
		this.thirdLevelNodes.add(franchiseNode);
	}
}
