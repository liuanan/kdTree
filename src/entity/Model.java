package entity;

import java.io.Externalizable;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * KD树
 * @author double
 *
 */
public class Model implements Externalizable {
	private Node root;	//根节点
	private List<Double>  normalization;
	private boolean isNormalization;
	
	public Model() {

	}
	public Model(String modelFile) {
		this.root = loadModel(modelFile).getRoot();
	}

	
	public Node getRoot() {
		return root;
	}
	public void setRoot(Node root) {
		this.root = root;
	}
	/**
	 * 保存模型
	 * @param modelFile
	 */
	public void saveModel(String modelFile) {
		try {
			ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(modelFile));
			out.writeObject(this);
			out.flush();
			out.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 读取模型
	 * @param modelFile
	 * @return
	 */
	public static Model loadModel(String modelFile) {
		try {
			ObjectInputStream in = new ObjectInputStream(new FileInputStream(modelFile));
			return (Model) in.readObject();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public void readExternal(ObjectInput in) throws IOException,
			ClassNotFoundException {
		// TODO Auto-generated method stub
		root = (Node) in.readObject();
		isNormalization = in.readBoolean();
		if (isNormalization) {
			normalization = new ArrayList<Double>();
			int num = in.readInt();
			while(num-- != 0) {
				normalization.add(in.readDouble());
			}
		}
	}
	
	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		// TODO Auto-generated method stub
		out.writeObject(root);
		out.writeBoolean(isNormalization);
		if (isNormalization) {
			out.writeInt(normalization.size());
			for (Double d : normalization) {
				out.writeDouble(d);
			}
		}
	}
	public List<Double> getNormalization() {
		return normalization;
	}
	public void setNormalization(List<Double> normalization) {
		this.normalization = normalization;
	}
	public boolean isNormalization() {
		return isNormalization;
	}
	public void setNormalization(boolean isNormalization) {
		this.isNormalization = isNormalization;
	}
}
