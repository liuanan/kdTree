package entity;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.List;

/**
 * 样本中包含的信息
 * @author double
 * @data 1013-03-13
 * @version 0.1
 */
public class Instance implements Externalizable {
	private List<Double> features;
	private String label;
	private String predictLabel;
	private List<Instance> neighbour;
	private double distance = -1;
	
	public Instance() {
		
	}
	public void normalization(List<Double> list) {
		for (int i = 0; i < features.size(); ++i) {
			double maxDis = list.get(2*i+1) - list.get(2*i);
			double newFeature = features.get(i);
			if (maxDis != 0) {
				newFeature = (features.get(i)-list.get(2*i))/maxDis;
			}
			features.set(i, newFeature);
		}
	}
	public Instance(List<Double> features, String label) {
		this.setFeatures(features);
		this.setLabel(label);
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public List<Double> getFeatures() {
		return features;
	}
	public void setFeatures(List<Double> features) {
		this.features = features;
	}
	public Double getFeature(int index) {
		return features.get(index);
	}
	public int getFeatureSize() {
		return features.size();
	}
	public String getPredictLabel() {
		return predictLabel;
	}
	public void setPredictLabel(String predictLabel) {
		this.predictLabel = predictLabel;
	}
	
	public String toString() {
		return label+"\t"+predictLabel;
	}
	public List<Instance> getNeighbour() {
		return neighbour;
	}
	public void setNeighbour(List<Instance> neighbour) {
		this.neighbour = neighbour;
	}
	public double getDistance() {
		return distance;
	}
	public void setDistance(double distance) {
		this.distance = distance;
	}
	@Override
	public void readExternal(ObjectInput in) throws IOException,
			ClassNotFoundException {
		// TODO Auto-generated method stub
		this.features = new ArrayList<Double>();
		int num = in.readInt();
		while((num--) != 0) {
			features.add(in.readDouble());
		}
		label = (String) in.readObject();
	}
	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		// TODO Auto-generated method stub
		out.writeInt(features.size());
		for (Double feature : features) {
			out.writeDouble(feature);
		}
		out.writeObject(label);
	}
}
