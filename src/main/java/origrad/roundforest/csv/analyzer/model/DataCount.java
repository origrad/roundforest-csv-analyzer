package origrad.roundforest.csv.analyzer.model;

public class DataCount implements Comparable<Long> {

	String data;
	Long count;

	public DataCount(String data, Long count) {
		super();
		this.data = data;
		this.count = count;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public Long getCount() {
		return count;
	}

	public void setCount(Long count) {
		this.count = count;
	}

	@Override
	public int compareTo(Long o) {
		return count.compareTo(o);
	}

}
