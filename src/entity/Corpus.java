package entity;

import java.util.ArrayList;
import java.util.List;

/**
 * �������ڴ�����Ĵ洢
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
	 * ��ȡ��index������
	 * @param index	����
	 * @return
	 */
	public Instance getInstance(int index) {
		return instanceList.get(index);
	}
	
	/**
	 * ��ȡ��instanceIndex�������ĵ�featureIndexά����ֵ
	 * @param instanceIndex
	 * @param featureIndex
	 * @return
	 */
	public Double getFeature(int instanceIndex, int featureIndex) {
		return instanceList.get(instanceIndex).getFeature(featureIndex);
	}

	/**
	 * ��ȡ��index��������label
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
