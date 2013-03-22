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
		Stack<Node> searchSpace = new Stack<Node>();
		List<Instance> neighbourList = initSearchSpace(ins, model, searchSpace);
		double minDis = neighbourList.get(0).getDistance();
		while (searchSpace.size() != 0) {
			Node tmp = searchSpace.pop();
			if (tmp.getChildren() == null) {
				continue;
			}
			int splitFeatureID = tmp.getSplitFeatureID();
			if (minDis >= Math.abs(tmp.getSplitFeatureValue()-neighbourList.get(0).getFeature(splitFeatureID))) {
				//ins是tmp的左后代空间中
				if (ins.getFeature(splitFeatureID) > tmp.getSplitFeatureValue()) {
					if (tmp.getChildren()[1] != null) {
						double tmpDis = disCount(tmp.getChildren()[1].getIns(), ins);
						if (tmpDis < minDis) {
							tmp.getChildren()[1].setDistance(tmpDis);
							minDis = tmpDis;
							neighbourList.clear();
							neighbourList.add(tmp.getChildren()[1].getIns());
						}
						else if (tmpDis == minDis) {
							neighbourList.add(tmp.getChildren()[1].getIns());
						}
						searchSpace.push(tmp.getChildren()[1]);
					}
				}
				else {
					double tmpDis = disCount(tmp.getChildren()[0].getIns(), ins);
					if (tmpDis < minDis) {
						tmp.getChildren()[0].setDistance(tmpDis);
						minDis = tmpDis;
						neighbourList.clear();
						neighbourList.add(tmp.getChildren()[0].getIns());
					}
					else if (tmpDis == minDis) {
						neighbourList.add(tmp.getChildren()[0].getIns());
					}
					searchSpace.push(tmp.getChildren()[0]);
				}
			}
		}
		ins.setNeighbour(neighbourList);
		ins.setPredictLabel(neighbourList.get(0).getLabel());
	}
	
	private static double disCount(Instance ins1, Instance ins2) {
		double dis = 0;
		for (int i = 0; i < ins1.getFeatureSize(); ++i) {
			double t = ins1.getFeature(i)-ins2.getFeature(i);
			dis += t*t;
		}
//		dis = Math.sqrt(dis);
		return dis;
	}
	private static List<Instance> initSearchSpace(Instance ins, Model model, Stack<Node> searchSpace) { 
		Node node = model.getRoot();
		List<Instance> nn = new ArrayList<Instance>();
		double minDis = 0;
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
		return nn;
	}
}
