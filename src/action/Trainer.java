package action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import entity.Corpus;
import entity.Instance;
import entity.Model;
import entity.Node;
import entity.Parameter;

/**
 * 建树
 * 
 * @author double
 * @data 2013-03-20
 */
public class Trainer {

	private static Parameter p;
	private static Corpus corpus;

	public static Model train(String args) {
		setParameter(args);
		try {
			System.err.println("Load train data...");
			corpus = CorpusReader.readCorpus(p.getTrainDataFile());
			System.err.println("Load train data OK! total "
					+ corpus.getInstanceSize() + " instance!");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Node root = new Node();
		List<Integer> instanceList = new ArrayList<Integer>();
		for (int i = 0; i < corpus.getInstanceSize(); ++i) {
			instanceList.add(i);
		}
		System.err.println("Training...");
		buildKDTree(root, instanceList, 0, 0, corpus.getInstanceSize() - 1);
		Model model = new Model();
		model.setNormalization(p.isNormalization());
		if (p.isNormalization()) {	//归一化处理
			model.setNormalization(normalization());
		}
		model.setRoot(root);
		System.err.println("Train Ok!");
		return model;
	}

	/**
	 * 特征归一化
	 * 
	 * @return
	 */
	private static List<Double> normalization() {
		List<Double> tmp = new ArrayList<Double>();
		boolean init = false;
		for (Instance ins : corpus.getInstanceList()) {
			if (init) {
				for (int i = 0; i < ins.getFeatureSize(); ++i) {
					double tmpFeatureValue = ins.getFeature(i);
					if (tmpFeatureValue < tmp.get(2 * i)) {
						tmp.set(2 * i, tmpFeatureValue);
					} else if (tmpFeatureValue > tmp.get(2 * i + 1)) {
						tmp.set(2 * i + 1, tmpFeatureValue);
					}
				}
			} else {
				for (int i = 0; i < ins.getFeatureSize(); ++i) {
					double tmpFeatureValue = ins.getFeature(i);
					tmp.add(tmpFeatureValue);
					tmp.add(tmpFeatureValue);
				}
				init = true;
			}
		}
		for (Instance ins : corpus.getInstanceList()) {
			ins.normalization(tmp);
		}
		return tmp;
	}

	/**
	 * 递归构建子树
	 * 
	 * @param node
	 *            节点
	 * @param instanceList
	 *            所有训练样例的id值
	 * @param deep
	 *            节点的深度
	 * @param begingInstanceID
	 *            节点中第一个样本在instanceList中的位置
	 * @param endInstanceID
	 *            节点中最后一个样本在instanceList中的位置
	 */
	private static void buildKDTree(Node node, List<Integer> instanceList,
			int deep, int begingInstanceID, int endInstanceID) {
		if (begingInstanceID == endInstanceID) {	//只有一个元素
			node.setIns(corpus.getInstance(instanceList.get(begingInstanceID)));
			return;
		}
		int splitFeatureID = getSplitFeatureID(deep);
		int tBegin = begingInstanceID;
		int tEnd = endInstanceID;
		int middle = (tBegin + tEnd + 1) / 2;
		int tMiddle;

		// 查找中位数对于的样本
		// 按照特征值调整instanceList，使得instanceList前半段对应的特征值都小于等于中位数，后半段对应的特征值大于中位数
		// 利用了快排的思想
		do {
			tMiddle = tBegin;
			int tmp = instanceList.get(tEnd);
			instanceList.set(tEnd, instanceList.get((tEnd + tBegin) / 2)); // 增加基准元素的随机性？？
			instanceList.set((tEnd + tBegin) / 2, tmp);
			for (int i = tBegin; i < tEnd; ++i) {
				if (corpus.getFeature(instanceList.get(i), splitFeatureID) <= corpus
						.getFeature(instanceList.get(tEnd), splitFeatureID)) {
					tmp = instanceList.get(i);
					instanceList.set(i, instanceList.get(tMiddle));
					instanceList.set(tMiddle, tmp);
					++tMiddle;
				}
			}
			tmp = instanceList.get(tEnd);
			instanceList.set(tEnd, instanceList.get(tMiddle));
			instanceList.set(tMiddle, tmp);
			if (tMiddle < middle) {
				tBegin = tMiddle + 1;
			} else if (tMiddle > middle) {
				tEnd = tMiddle - 1;
			}
		} while (tMiddle != middle);
		
		//处理多个样本的特征值和中位数相同的情况，如果按这个特征值分裂，那么这些样本必须都在左子树
		boolean flag = false;
		while (middle + 1 <= endInstanceID
				&& corpus.getFeature(instanceList.get(middle+1), splitFeatureID).equals(corpus
						.getFeature(instanceList.get(middle), splitFeatureID))) {
			++middle;
			flag = true;
		}
		if (middle + 1 <= endInstanceID && flag) {
			--middle;
		}
		
		node.setIns(corpus.getInstance(instanceList.get(middle)));
		node.setSplitFeatureID(splitFeatureID);
		Node[] children = new Node[2];
		children[0] = new Node();
		children[1] = new Node();
		
		// 递归建立左子树
		buildKDTree(children[0], instanceList, deep + 1, begingInstanceID,
				middle - 1);
		// 递归建立右子树
		if (middle != endInstanceID) {
			buildKDTree(children[1], instanceList, deep + 1, middle + 1,
					endInstanceID);
		} else { // 右子树可能为空
			children[1] = null;
		}
		node.setChildren(children);
	}

	private static int getSplitFeatureID(int deep) {
		return deep % corpus.getFeatureSize();
	}

	private static void setParameter(String args) {
		p = new Parameter();
		String[] array = args.split("\\s+");
		for (int i = 0; i < array.length; ++i) {
			if (array[i].startsWith("-")) {
				if (array[i].equals("-o") || array[i].equals("-m")) { // 模型输出文件
					p.setSaveFile(array[i + 1]);
				} else if (array[i].equals("-n")) { // 是否是bool型特征
					p.setNormalization(Boolean.parseBoolean(array[i + 1]));
				} else if (array[i].equals("-i")) { // 训练数据文件
					p.setTrainDataFile(array[i + 1]);
				} else {
					--i;
				}
				++i;
			}
		}
	}
}
