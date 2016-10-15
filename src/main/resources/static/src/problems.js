import {inject} from 'aurelia-framework';
import {initialize} from 'aurelia-pal-browser';
import 'fetch';
import {HttpClient} from 'aurelia-http-client';
initialize();

let httpClient = new HttpClient();

export class Problems {
	
	  problems = [];
	
	  activate() {
	    return new HttpClient()
	      .get('problem')
	      .then(response => this.problems = response.content);
	  }
}
