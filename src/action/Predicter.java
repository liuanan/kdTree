package action;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import entity.Instance;
import entity.Model;
import entity.Node;

/**
 * 预测类
 * @author double
 * @data 2013-03-20
 */
public class Predicter {
	/**
	 * 整个模型预测ins的值，结果保存在ins中
	 * @param ins	测试样本
	 * @param model	模型
	 */
	public static void predict(Instance ins, Model model) {
		if (model.isNormalization()) {
			ins.normalization(model.getNormalization());
		}
		Stack<Node> searchSpace = new Stack<Node>();	//保存查询过的节点，在栈里面的节点必须有一个儿子节点没有被查询过
		List<Instance> neighbourList = new ArrayList<Instance>();	//保存最近节点
		double minDis = initSearchSpace(neighbourList, ins, model.getRoot(), searchSpace, 0);
		while (searchSpace.size() != 0) {
			Node tmp = searchSpace.pop();
			if (tmp.getChildren() == null) {
				continue;
			}
			int splitFeatureID = tmp.getSplitFeatureID();
			
			//检查该节点的另外一个儿子对应的区域，与以目标点为球心，以当前最短距离为半径的超球体相交
			if (minDis >= Math.abs(tmp.getSplitFeatureValue()-neighbourList.get(0).getFeature(splitFeatureID))) {
				//ins是tmp的左后代空间中，搜索右儿子
				if (ins.getFeature(splitFeatureID) <= tmp.getSplitFeatureValue()) {
					if (tmp.getChildren()[1] != null) {
						minDis=initSearchSpace(neighbourList, ins, tmp.getChildren()[1], searchSpace, minDis);
					}
				}
				else {	//搜索左儿子
					minDis=initSearchSpace(neighbourList, ins, tmp.getChildren()[0], searchSpace, minDis);
				}
			}
		}
		ins.setNeighbour(neighbourList);
		ins.setPredictLabel(neighbourList.get(0).getLabel());
	}
	
	/**
	 * 计算两个节点的距离
	 * @param ins1	节点1保存的样本
	 * @param ins2	节点2保存的样本
	 * @return	两个节点的距离的平方
	 */
	private static double disCount(Instance ins1, Instance ins2) {
		double dis = 0;
		for (int i = 0; i < ins1.getFeatureSize(); ++i) {
			double t = ins1.getFeature(i)-ins2.getFeature(i);
			dis += t*t;
		}
//		dis = Math.sqrt(dis);
		return dis;
	}
	
	/**
	 * 查询ins在node为根节点的树（子树）中的叶节点位置，把查询路径压入searchSpace，并且更新当前最短距离
	 * @param nn 最近邻
	 * @param ins	目标节点
	 * @param node	当前搜索节点
	 * @param searchSpace	栈
	 * @param minDis	最小距离
	 * @return	更新后的最小距离
	 */
	private static double initSearchSpace(List<Instance> nn, Instance ins, Node node, Stack<Node> searchSpace, double minDis) { 
		while (node != null) {
			if (nn.size() == 0) {
				minDis = disCount(node.getIns(), ins);
				node.setDistance(minDis);
				nn.add(node.getIns());
			}
			else {
				double tmpDis = disCount(node.getIns(), ins);
				node.setDistance(tmpDis);
				if (tmpDis < minDis) {
					nn.clear();
					nn.add(node.getIns());
					minDis = tmpDis;
				}
				else if (tmpDis == minDis) {
					nn.add(node.getIns());
				}
			}
			searchSpace.push(node);
			if (node.getChildren() == null) {
				break;
			}
			//node没有右儿子的时候，必须递归的查找左儿子
			if (node.getChildren()[1] == null || ins.getFeature(node.getSplitFeatureID()) <= node.getIns().getFeature(node.getSplitFeatureID())) {
				node = node.getChildren()[0];
			}
			else if (node.getChildren()[1] != null) {
				node = node.getChildren()[1];
			}
		}
		return minDis;
	}
}
