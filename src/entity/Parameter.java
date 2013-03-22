package entity;

/**
 * 模型的超参数
 * @author double
 * @data 2013-03-13
 * @version 0.1
 */
public class Parameter {
	private String saveFile;				//模型保存路径
	private String  trainDataFile;			//训练数据文件名
	private boolean isNormalization = false;		//是否对特征做归一化处理
	
	public boolean isLegal() {
		if (saveFile == null) {
			System.out.println("请指定参数saveFile!");
			return false;
		}
		if (trainDataFile == null) {
			System.out.println("请指定参数trainDataFile!");
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
