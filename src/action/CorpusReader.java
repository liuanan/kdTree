package action;

import java.io.IOException;

import util.IOReader;
import entity.Corpus;

public class CorpusReader {
	
	/**
	 * 从文件中读取样本
	 * @param file 文件路径
	 * @return	样本集合
	 * @throws IOException
	 */
	public static Corpus readCorpus(String file) throws IOException {
		IOReader reader = new IOReader(file);
		Corpus corpus = new Corpus();
		String line = null;
		while ((line = reader.readLine()) != null) {
			corpus.addInstance(InstanceFactory.getInstance(line));
		}
		corpus.setFeatureSize(corpus.getInstance(0).getFeatureSize());
		return corpus;
	}
}
