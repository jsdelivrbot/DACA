package services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.Model;

public class Repository<T extends Comparable<T>, V extends Model<T>> {

	protected Map<T, V> data;
	private Long count;
	
	protected Repository() {
		this.data = new HashMap<T, V>();
		this.count = new Long(0);
	}
	
	public V findById(T id) {
		return this.data.get(id);
	}

	public List<V> findAll() {
		List<V> result = new ArrayList<V>();
		for (T key : this.data.keySet()) {
			result.add(this.findById(key));
		}
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public V save(V model) {
		if (model.getId() == null) {
			model.setId((T)this.count++);
		}
		return this.data.put(model.getId(), model);
	}
	
	public V delete(T id) {
		return this.data.remove(id);
	}

	public void clear() {
		this.data.clear();
		this.count = 0L;
	}
	
}
