package entity;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

/**
 * kd树的节点
 * @author double
 * @data2013-03-21
 */
public class Node implements Externalizable {
	private Instance ins;	//本节点划分的样本
	private int splitFeatureID;	//划分的特征id
	private Node[] children;	//儿子节点
	
	public double getSplitFeatureValue() {
		return ins.getFeature(splitFeatureID);
	}
	
	public Instance getIns() {
		return ins;
	}
	public void setIns(Instance ins) {
		this.ins = ins;
	}
	public int getSplitFeatureID() {
		return splitFeatureID;
	}
	public void setSplitFeatureID(int splitFeatureID) {
		this.splitFeatureID = splitFeatureID;
	}
	public Node[] getChildren() {
		return children;
	}
	public void setChildren(Node[] children) {
		this.children = children;
	}
	public void setDistance(double dis) {
		ins.setDistance(dis);
	}
	public double getDistance() {
		return ins.getDistance();
	}

	@Override
	public void readExternal(ObjectInput in) throws IOException,
			ClassNotFoundException {
		// TODO Auto-generated method stub
		this.ins = (Instance) in.readObject();
		this.splitFeatureID = in.readInt();
		int childrenNum = in.readInt();
		if (childrenNum != 0) {
			children = new Node[2];
			for (int i = 0; i < childrenNum; ++i) {
				children[i] = (Node) in.readObject();
			}
		}
	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		// TODO Auto-generated method stub
		out.writeObject(ins);
		out.writeInt(splitFeatureID);
		int childrenNum = 0;
		if (children != null) {
			childrenNum = 2;
			if (children[1] != null) {
				childrenNum = 2;
			}
		}
		out.writeInt(childrenNum);
		for (int i = 0; i < childrenNum; ++i) {
			out.writeObject(children[i]);
		}
	}
	
	
}
