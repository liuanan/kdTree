package entity;

import java.util.ArrayList;
import java.util.List;

/**
 * 语料在内存里面的存储
 * @author double
 * @data 1013-03-15
 * @version 0.1
 */
public class Corpus {
	private List<Instance> instanceList = null;
	private int featureSize = 0;
	
	public Corpus() {
		instanceList = new ArrayList<Instance>();
	}
	
	public void addInstance(Instance ins) {
		instanceList.add(ins);
	}
	
	/**
	 * 获取第index个样本
	 * @param index	索引
	 * @return
	 */
	public Instance getInstance(int index) {
		return instanceList.get(index);
	}
	
	/**
	 * 获取第instanceIndex个样本的第featureIndex维特征值
	 * @param instanceIndex
	 * @param featureIndex
	 * @return
	 */
	public Double getFeature(int instanceIndex, int featureIndex) {
		return instanceList.get(instanceIndex).getFeature(featureIndex);
	}

	/**
	 * 获取第index个样本的label
	 * @param index
	 * @return
	 */
	public String getLabel(int index) {
		return instanceList.get(index).getLabel();
	}
	public int getFeatureSize() {
		return featureSize;
	}

	public void setFeatureSize(int featureSize) {
		this.featureSize = featureSize;
	}
	
	public int getInstanceSize() {
		return instanceList.size();
	}
	
	public List<Instance> getInstanceList() {
		return instanceList;
	}
}
