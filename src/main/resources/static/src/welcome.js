import {inject} from 'aurelia-framework';
import {initialize} from 'aurelia-pal-browser';
import 'fetch';
import {HttpClient} from 'aurelia-http-client';
initialize();

let httpClient = new HttpClient();

export class Welcome {
	
	  stats = {};
	
	  activate() {
	    return new HttpClient()
	      .get('stats')
	      .then(response => this.stats = response.content);
	  }
}
