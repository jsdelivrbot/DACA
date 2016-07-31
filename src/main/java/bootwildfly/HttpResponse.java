package bootwildfly;

import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class HttpResponse {

	private JSONObject obj;
	private HttpStatus status;
	
	public HttpResponse() {
		this.obj = new JSONObject();
		this.status = HttpStatus.OK;
	}
	
	public HttpResponse put(String key, Object value) {
		this.obj.put(key, value);
		return this;
	}
	
	public HttpResponse status(HttpStatus status) {
		this.status = status;
		return this;
	}
	
	public ResponseEntity<String> build() {
		return new ResponseEntity<String>(this.obj.toString(), this.status);
	}

}
