package cloud.service.tools.entity.mysql.utils;

public class ClassBuilder {
	private StringBuilder clazz;

	public int whereIndex = 0;

	public ClassBuilder() {
		this.clazz = new StringBuilder();
	}

	public ClassBuilder append(String string) {
		clazz.append(string);
		return this;
	}
	
	public ClassBuilder tab() {
		clazz.append("\t");
		return this;
	}
	
	public ClassBuilder tab(int count) {
		for (int i = 0; i < count; i++) {
			clazz.append("\t");
		}
		return this;
	}
	
	public ClassBuilder enter() {
		clazz.append("\r\n");
		return this;
	}
	
	public ClassBuilder enter(int count) {
		for (int i = 0; i < count; i++) {
			clazz.append("\r\n");
		}
		return this;
	}

	@Override
	public String toString() {
		return clazz.toString();
	}

}
