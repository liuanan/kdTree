package entity;

/**
 * ģ�͵ĳ�����
 * @author double
 * @data 2013-03-13
 * @version 0.1
 */
public class Parameter {
	private String saveFile;				//ģ�ͱ���·��
	private String  trainDataFile;			//ѵ�������ļ���
	private boolean isNormalization = false;		//�Ƿ����������һ������
	
	public boolean isLegal() {
		if (saveFile == null) {
			System.out.println("��ָ������saveFile!");
			return false;
		}
		if (trainDataFile == null) {
			System.out.println("��ָ������trainDataFile!");
			return false;
		}
		return true;
	}
	public String getSaveFile() {
		return saveFile;
	}
	public void setSaveFile(String saveFile) {
		this.saveFile = saveFile;
	}
	public String getTrainDataFile() {
		return trainDataFile;
	}
	public void setTrainDataFile(String trainDataFile) {
		this.trainDataFile = trainDataFile;
	}
	public boolean isNormalization() {
		return isNormalization;
	}
	public void setNormalization(boolean isNormalization) {
		this.isNormalization = isNormalization;
	}
}
