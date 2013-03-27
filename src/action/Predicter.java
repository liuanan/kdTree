package action;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import entity.Instance;
import entity.Model;
import entity.Node;

/**
 * Ԥ����
 * @author double
 * @data 2013-03-20
 */
public class Predicter {
	/**
	 * ����ģ��Ԥ��ins��ֵ�����������ins��
	 * @param ins	��������
	 * @param model	ģ��
	 */
	public static void predict(Instance ins, Model model) {
		if (model.isNormalization()) {
			ins.normalization(model.getNormalization());
		}
		Stack<Node> searchSpace = new Stack<Node>();	//�����ѯ���Ľڵ㣬��ջ����Ľڵ������һ�����ӽڵ�û�б���ѯ��
		List<Instance> neighbourList = new ArrayList<Instance>();	//��������ڵ�
		double minDis = initSearchSpace(neighbourList, ins, model.getRoot(), searchSpace, 0);
		while (searchSpace.size() != 0) {
			Node tmp = searchSpace.pop();
			if (tmp.getChildren() == null) {
				continue;
			}
			int splitFeatureID = tmp.getSplitFeatureID();
			
			//���ýڵ������һ�����Ӷ�Ӧ����������Ŀ���Ϊ���ģ��Ե�ǰ��̾���Ϊ�뾶�ĳ������ཻ
			if (minDis >= Math.abs(tmp.getSplitFeatureValue()-neighbourList.get(0).getFeature(splitFeatureID))) {
				//ins��tmp�������ռ��У������Ҷ���
				if (ins.getFeature(splitFeatureID) <= tmp.getSplitFeatureValue()) {
					if (tmp.getChildren()[1] != null) {
						minDis=initSearchSpace(neighbourList, ins, tmp.getChildren()[1], searchSpace, minDis);
					}
				}
				else {	//���������
					minDis=initSearchSpace(neighbourList, ins, tmp.getChildren()[0], searchSpace, minDis);
				}
			}
		}
		ins.setNeighbour(neighbourList);
		ins.setPredictLabel(neighbourList.get(0).getLabel());
	}
	
	/**
	 * ���������ڵ�ľ���
	 * @param ins1	�ڵ�1���������
	 * @param ins2	�ڵ�2���������
	 * @return	�����ڵ�ľ����ƽ��
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
	 * ��ѯins��nodeΪ���ڵ�������������е�Ҷ�ڵ�λ�ã��Ѳ�ѯ·��ѹ��searchSpace�����Ҹ��µ�ǰ��̾���
	 * @param nn �����
	 * @param ins	Ŀ��ڵ�
	 * @param node	��ǰ�����ڵ�
	 * @param searchSpace	ջ
	 * @param minDis	��С����
	 * @return	���º����С����
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
			//nodeû���Ҷ��ӵ�ʱ�򣬱���ݹ�Ĳ��������
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
